<template>
    <div class="table-container">
      <el-form :inline="true" :model="formInline" class="form-inline">
        <el-form-item>
          <el-button type="primary" @click="onLeaveRequest">请假申请</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="filterTableRef" class="table-list" row-key="date" v-loading="loading" :data="tableData" style="width: 100%">
        <el-table-column prop="leave_submit_at" label="提交日期" sortable width="180" column-key="leave_submit_at"></el-table-column>
        <el-table-column prop="leave_start_time" label="开始时间" width="180" truncated> </el-table-column>
        <el-table-column prop="leave_end_time" label="结束时间" truncated> </el-table-column>
        <el-table-column prop="leave_reason" label="原因" truncated> </el-table-column>
        <el-table-column align="right">
          <template #header>
            <el-input v-model="search" size="small" placeholder="搜索" />
          </template>
          <template #default="scope">
            <el-button size="small" @click="modifyPop(scope.row)">修改</el-button>
            <!---------------------------------------------------- 改成如果状态是未审核才能修改 ----------------------------------------------->
            <el-button size="small" @click="detailPop(scope.row)">查看详情</el-button>
            <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
        <el-table-column
          prop="leave_status"
          label="处理状态"
          width="100"
          :filters="[
            { text: '已通过', value: '已通过' },
            { text: '已拒绝', value: '已拒绝' },
            { text: '未处理', value: '未处理' },
          ]"
          :filter-method="filterStatus"
          filter-placement="bottom-end"
        >
          <template #default="scope">
            <el-tag :type="scope.row.status === '已完成' ? 'primary' : 'success'" disable-transitions>{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
  
  
      <el-dialog v-model="modifyFormVisible" title="修改待办事项">
        <el-form :model="form">
          <el-form-item label="请假记录ID" :label-width="formLabelWidth">
            <el-text class="mx-1" type="info">{{ form.leave_id }}</el-text>
          </el-form-item>
          <el-form-item label="提交用户ID" :label-width="formLabelWidth">
            <el-text class="mx-1" type="info">{{ form.leave_user_id }}</el-text>
          </el-form-item>
          <el-form-item label="开始时间" :label-width="formLabelWidth">
            <el-input v-model="form.leave_start_time" autocomplete="on"></el-input>
          </el-form-item>
          <el-form-item label="结束时间" :label-width="formLabelWidth">
            <el-input v-model="form.leave_end_time" autosize type="textarea"/>
          </el-form-item>
          <el-form-item label="请假原因" :label-width="formLabelWidth">
            <el-input v-model="form.leave_reason" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="处理状态" :label-width="formLabelWidth">
            <el-input v-model="form.leave_reason" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="提交日期" :label-width="formLabelWidth">
            <el-input v-model="form.leave_submit_at" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="modifyFormVisible = false">取消</el-button>
          <el-button type="primary" @click="handleEdit()">确定</el-button>
        </div>
      </el-dialog>
  
      <el-dialog v-model="detailFormVisible" title="待办事项详情">
        <el-form :model="form">
          <el-form-item label="请假记录ID&nbsp;&nbsp;" :label-width="formLabelWidth">
            {{ form.leave_id }}
          </el-form-item>
          <el-form-item label="提交用户ID&nbsp;&nbsp;" :label-width="formLabelWidth">
            {{ form.leave_user_id }}
          </el-form-item>
          <el-form-item label="开始时间&nbsp;&nbsp;" :label-width="formLabelWidth">
            {{ form.leave_start_time }}
          </el-form-item>
          <el-form-item label="结束时间&nbsp;&nbsp;" :label-width="formLabelWidth">
            {{ form.leave_end_time }}
          </el-form-item>
          <el-form-item label="请假原因&nbsp;&nbsp;" :label-width="formLabelWidth">
            {{ form.leave_reason }}
          </el-form-item>
          <el-form-item label="处理状态&nbsp;&nbsp;" :label-width="formLabelWidth">
            {{ form.leave_status }}
          </el-form-item>
          <el-form-item label="提交日期&nbsp;&nbsp;" :label-width="formLabelWidth">
            {{ form.leave_submit_at }}
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="detailFormVisible = false">确定</el-button>
        </div>
      </el-dialog>
  
      <el-pagination
        :hide-on-single-page="true"
        :current-page="currentPage"
        :page-sizes="[5, 10, 15, 20, 25]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
  
    </div>
  </template>
  <script lang="ts">
  import { computed, defineComponent, onMounted, reactive, ref, toRefs } from 'vue'
  import { useRouter } from 'vue-router'
  import permission from '@/directive/permission'
  import Service from '../api/index'
  import {ElMessage} from "element-plus";
  
  export default defineComponent({
    name: 'leaveList',
    directives: {
      permission
    },
    setup() {
  
      const router = useRouter()
      const filterTableRef = ref()
      const state = reactive({
        loading: false,
        tableData: [],
        currentPage: 1,
        pageSize: 5,
        search: '',
        modifyFormVisible: false,
        detailFormVisible: false,
        form: {},
      })
      const formInline = reactive({
        user: '',
        region: ''
      })
      const total = 1
  
      onMounted(() => {
        // eslint-disable-next-line no-console
        getLeaveRecord()
      })
      // methods
      const resetDateFilter = () => {
        filterTableRef.value.clearFilter('date')
      }
  
      const getLeaveRecord = () => {
        try {
          Service.postGetLeaveApproval().then((res) => {
            if (res) {
              state.tableData = []
              console.log(res)
              console.log('postGetLeaveApproval get')
              var data = res.data
              for (let i = 0; i < data.length; i++) {
                var record = {
                  leave_id: data[i].leave_id,
                  leave_user_id: data[i].user_id,
                  leave_start_time: data[i].start_date,
                  leave_end_time: data[i].end_date,
                  leave_reason: data[i].reason,
                  leave_status: data[i].status,
                  leave_submit_at: data[i].submitted_at,
                }
                state.tableData.push(record)
              }
            } else {
              console.log('postGetLeaveApproval RES MISS')
            }
          });
        } catch (err) {
          ElMessage({
            type: 'warning',
            message: err.message
          })
        }
  
      }
  
      const clearFilter = () => {
        filterTableRef.value.clearFilter()
      }
  
      const formatter = (row: { address: any }) => row.address
      const filterTag = (value: any, row: { Tag: any }) => row.tag === value
      const filterStatus = (value: any, row: { status: any }) => row.status === value
      const filterHandler = (value: any, row: { [x: string]: any }, column: { property: any }) => {
        const { property } = column
        return row[property] === value
      }
  
      const modifyPop = (row) => {
        state.modifyFormVisible = true
        state.form = row
      }
  
      const detailPop = (row) => {
        state.detailFormVisible = true
        state.form = row
      }
  
      const handleEdit = () => {
        // eslint-disable-next-line no-console
        state.modifyFormVisible = false
        let record = state.form
        state.form = {}
        try {
          Service.postModifyLeaveApproval(record).then((res) => {
            if (res) {
            } else {
            }
          });
        } catch (err) {
          ElMessage({
            type: 'warning',
            message: err.message
          })
        }
      }
      const handleDelete = (index: any, row: any) => {
        // eslint-disable-next-line no-console
        console.log(index, row)
        let record = {
          leave_id: row.leave_id
        }
        try {
          Service.deleteLeaveApproval(record).then((res) => {
            if (res) {
              // console.log(res)
            } else {
            }
          });
        } catch (err) {
          ElMessage({
            type: 'warning',
            message: err.message
          })
        }
        state.tableData.splice(index, 1)
      }
      const handleSizeChange = (val: any) => {
        // eslint-disable-next-line no-console
        console.log(`每页 ${val} 条`)
        state.pageSize = val
        // request api to change tableData
      }
  
       // 分页数据处理
       // @param data [Array] 需要分页的数据
       //  @param num [Number] 当前第几页
       //  @param size [Number] 每页显示多少条
      // const getList = (data, num, size) => {
      //   let list, start, end
      //   start = (num - 1) * size
      //   end = start + size
      //   list = data.filter((item, index) => {
      //     return index >= start && index < end
      //   })
      //   list.forEach((item, index) => {
      //     item.seq = index + start
      //   })
      //   return list
      // }
  
      const handleCurrentChange = (val: any) => {
        // eslint-disable-next-line no-console
        console.log(`当前页: ${val}`)
        state.currentPage = val
        // request api to change tableData
      }
      const onSubmit = () => {
        // eslint-disable-next-line no-console
        console.log('submit!')
      }
  
      const onLeaveRequest = () => {
        // eslint-disable-next-line no-console
        router.replace('/leaveApproval/leaveRequest')
      }
  
      return {
        formInline,
        total,
        ...toRefs(state),
        handleCurrentChange,
        handleSizeChange,
        onSubmit,
        onLeaveRequest,
        handleEdit,
        handleDelete,
        filterTableRef,
        resetDateFilter,
        clearFilter,
        formatter,
        filterTag,
        filterStatus,
        filterHandler,
        modifyPop,
        detailPop,
      }
    }
  })
  </script>
  <style lang="stylus" scoped>
  .table-container{
      .form-inline{
          margin:15px;
          text-align:left;
      }
      .table-list{
          margin:15px;
      }
  
  }
  </style>
  
