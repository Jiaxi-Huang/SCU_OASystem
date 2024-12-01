<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
      <el-form-item>
        <el-button type="primary" @click="onAddReimbursement">提交报销申请</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="filterTableRef" class="table-list" row-key="date" :data="tableData.filter((data) => !search || data.title.toLowerCase().includes(search.toLowerCase()))" style="width: 100%">
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
      <el-table-column prop="status" label="状态" truncated> </el-table-column>
      <el-table-column prop="submitted_at" label="提交时间" truncated> </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="输入标题字段关键字搜索" />
        </template>
        <template #default="scope">
          <el-button size="mini" @click="modifyPop(scope.row)">修改</el-button>
          <el-button size="mini" @click="detailPop(scope.row)">查看詳情</el-button>
          <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        label="状态"
        width="100"
        :filters="[
          { text: '已完成', value: '已完成' },
          { text: '未完成', value: '未完成' }
        ]"
        :filter-method="filterStatus"
        filter-placement="bottom-end"
      >
        <template #default="scope">
          <el-tag :type="scope.row.status === '已完成' ? 'primary' : 'success'" disable-transitions>{{ scope.row.status }}</el-tag>
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
          <el-input v-model="form.status" autocomplete="off"></el-input>
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
          ddl: '后台沒打开',
          title: '后台沒打开',
          status: '后台沒打开',
          content: 'NO'
        },
      ],
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
    // const total = computed(() => state.tableData.length)
    // A quick fix here
    const total = 1

    onMounted(() => {
      // eslint-disable-next-line no-console
      getReimbursementList()
    })
    // methods
    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('date')
    }

    const getReimbursementList = () => {
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

