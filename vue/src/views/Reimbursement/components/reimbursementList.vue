<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
      <el-form-item>
        <el-button type="primary" @click="onAddReimbursement">提交报销申请</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="filterTableRef" class="table-list" row-key="reimbursement_id" :data="paginatedData" style="width: 100%"
              @filter-change="handleFilterChange">
      <el-table-column
        prop="reimbursement_id"
        label="报销编号"
        sortable
        width="180"
        column-key="reimbursement_id"
      >
      </el-table-column>
      <el-table-column prop="user_id" label="提交用户id" width="180" truncated> </el-table-column>
      <el-table-column prop="amount" label="金额" truncated> </el-table-column>
      <el-table-column prop="description" label="描述" truncated> </el-table-column>
      <el-table-column
          prop="status"
          column-key="status"
          label="状态"
          width="100"
          :filters="status_options"
          :filtered-value="filters.status"
          :filter-multiple="false"
          filter-placement="bottom-end"
      >
        <template #default="scope">
          <el-tag :type="scope.row.status === '已通过' ? 'primary' : 'success'" disable-transitions>{{ scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="submitted_at" label="提交时间" truncated> </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="输入标题字段关键字搜索" />
        </template>
        <template #default="scope">
          <el-button size="mini" @click="modifyPop(scope.row)">修改</el-button>
          <el-button size="mini" @click="detailPop(scope.row)">查看详情</el-button>
          <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>


    <!--    V-MODEL!!!!!-->
    <el-dialog v-model="modifyFormVisible" title="修改报销内容">
      <el-form :model="form">
        <el-form-item label="报销编号" :label-width="formLabelWidth">
          <el-input v-model="form.reimbursement_id" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="提交用户id" :label-width="formLabelWidth">
          <el-input v-model="form.user_id" autosize type="textarea"/>
        </el-form-item>
        <el-form-item label="金额" :label-width="formLabelWidth">
          <el-input v-model="form.amount" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述" :label-width="formLabelWidth">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态" :label-width="formLabelWidth">
          <el-select v-model="form.status" placeholder="Select" style="width: 240px">
            <el-option
                v-for="item in status_options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="提交时间" :label-width="formLabelWidth">
          <el-input v-model="form.submitted_at" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEdit()">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog v-model="detailFormVisible" title="待办事项详情">
      <el-form :model="form">
        <el-form-item label="报销编号&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.reimbursement_id }}
        </el-form-item>
        <el-form-item label="提交用户id&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.user_id }}
        </el-form-item>
        <el-form-item label="金额&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.amount }}
        </el-form-item>
        <el-form-item label="描述&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.description }}
        </el-form-item>
        <el-form-item label="状态&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.status }}
        </el-form-item>
        <el-form-item label="提交时间&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.submitted_at }}
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <el-pagination
      :current-page="currentPage"
      :page-sizes="[5, 10, 15, 20, 25]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="record_cnt"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    >
    </el-pagination>

  </div>
</template>
<script lang="ts">
import {computed, defineComponent, onMounted, reactive, ref, toRefs, watch} from 'vue'
import { useRouter } from 'vue-router'
import permission from '@/directive/permission'
import Service from '../api/index'
import {ElMessage} from "element-plus";

export default defineComponent({
  name: 'reimbursementList',
  directives: {
    permission
  },
  setup() {

    const router = useRouter()
    const filterTableRef = ref()
    const state = reactive({
      tableData: [
        {
          reimbursement_id: '0231554651',
          user_id: '1',
          amount: '1000',
          description: '差旅',
          status: '未完成',
          submitted_at: new Date(),
        },
      ],
      currentPage: 1,
      pageSize: 5,
      search: '',
      modifyFormVisible: false,
      detailFormVisible: false,
      form: {},

      paginatedData: [],
      record_cnt: 0,

      status_options: [
        {
          value:'未审核',
          text:'未审核',
        },
        {
          value:'已通过',
          text:'已通过',
        },
        {
          value:'未通过',
          text:'未通过',
        }
      ],

      filters: {
        status: ''
      },
    })

    const formInline = reactive({
      user: '',
      region: ''
    })
    // const total = computed(() => state.tableData.length)
    // A quick fix here
    const total = 1

    onMounted(() => {
      getReimbursementList()
      filterReimbursementStatus()
      updatePaginatedData()
    })
    // methods
    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('date')
    }
    const filterReimbursementStatus = () => {
      if (sessionStorage.getItem("showPendingReimbursement") === '1') {
        state.filters.status = "未审核";
        console.log("showPendingReimbursement", state.filters.status);
      }
      sessionStorage.setItem("showPendingReimbursement", 0);
    }

    const updatePaginatedData = () => {
      let recordsToFilter = state.tableData;
      console.log("recordsToFilter", recordsToFilter)
      if (state.search) {
        recordsToFilter = recordsToFilter.filter((record) =>
            record.reimbursement_id.toLowerCase().includes(state.search.toLowerCase()) // 忽略大小写
        );
      }

      if (state.filters.status === '未审核') {
        recordsToFilter = recordsToFilter.filter((record) =>
            record.status == state.filters.status
        );
        console.log("执行跳转")
      }

      // 更新分页数据
      // console.log(state.pageSize)
      const start = (state.currentPage - 1) * state.pageSize;
      const end = state.currentPage * state.pageSize;
      state.paginatedData = recordsToFilter.slice(start, end);  // 根据当前页和页大小提取分页数据
      state.record_cnt = recordsToFilter.length;  // 更新总记录数
    };

    const getReimbursementList = () => {
      const role = localStorage.getItem('role')
      if (role != 'admin') {
        try {
          Service.getReimbursementList().then((res) => {
            if (res) {
              state.tableData = []
              var data = res.data
              for (let i = 0; i < data.length; i++) {
                var record = {
                  reimbursement_id: data[i].reimbursement_id,
                  user_id: data[i].user_id,
                  amount: data[i].amount,
                  description: data[i].description,
                  status: data[i].status,
                  submitted_at: data[i].submitted_at,
                }
                state.tableData.push(record)
              }
              updatePaginatedData();
            } else {
              console.log('MISS')
            }
          });
        } catch (err) {
          ElMessage({
            type: 'warning',
            message: err.message
          })
        }
      }else {
        try {
          Service.getAdminReimbursementList().then((res) => {
            if (res) {
              state.tableData = []
              var data = res.data
              for (let i = 0; i < data.length; i++) {
                var record = {
                  reimbursement_id: data[i].reimbursement_id,
                  user_id: data[i].user_id,
                  amount: data[i].amount,
                  description: data[i].description,
                  status: data[i].status,
                  submitted_at: data[i].submitted_at,
                }
                state.tableData.push(record)
              }
            } else {
              console.log('MISS')
            }
          });
        } catch (err) {
          ElMessage({
            type: 'warning',
            message: err.message
          })
        }
      }
    }

    const clearFilter = () => {
      filterTableRef.value.clearFilter()
    }

    const formatter = (row: { address: any }) => row.address
    const filterTag = (value: any, row: { Tag: any }) => row.tag === value
    const filterHandler = (value: any, row: { [x: string]: any }, column: { property: any }) => {
      const { property } = column
      return row[property] === value
    }



    const watchSearch = () => {
      updatePaginatedData();  // 过滤数据并更新分页
    };
    watch(() => state.search, watchSearch);

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
        Service.updateReimbursement(record).then((res) => {
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
    }
    const handleDelete = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      let record = {
        reimbursement_id: row.reimbursement_id
      }
      try {
        Service.deleteReimbursement(record).then((res) => {
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
      console.log(`每页 ${val} 条`)
      state.pageSize = val
      updatePaginatedData();  // 更新分页数据
    }

    const handleCurrentChange = (val: any) => {
      console.log(`当前页: ${val}`)
      state.currentPage = val
      updatePaginatedData();  // 更新分页数据
    }
    const onSubmit = () => {
      // eslint-disable-next-line no-console
      console.log('submit!')
    }
    const handleFilterChange = (filters: any) => {
      state.filters.status = filters.status;   // 只有一个条件在 `filters` 中
      console.log(state.filters.status);
      console.log(filters.status);
      updatePaginatedData(); // 更新分页数据
    }

    const onAddReimbursement = () => {
      // eslint-disable-next-line no-console
      router.replace('/reimbursement/reimbursementAdd')
    }

    return {
      formInline,
      total,
      ...toRefs(state),
      handleCurrentChange,
      handleSizeChange,
      onSubmit,
      onAddReimbursement,
      handleEdit,
      handleDelete,
      filterTableRef,
      resetDateFilter,
      clearFilter,
      formatter,
      filterTag,
      filterHandler,
      modifyPop,
      detailPop,
      filterReimbursementStatus,
      handleFilterChange
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

