<template>
  <div>
    <el-card class="card-ctrl">
      <el-row style="margin: 15px">
        <el-col :span="24" class="page-title-box">
          <h4 class="page-title">当天考勤情况</h4>
          <div class="page-title-right">
            <div style="margin-right: 10px">
              <!-- 在日期选择后触发 fetchData 函数 -->
              <el-date-picker v-model="pickDate" type="date" placeholder="选择日期" @change="fetchData"></el-date-picker>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10" style="text-align: left">
          <el-button type="primary" size="medium" @click="onCreate"><el-icon><plus /></el-icon>新增考勤</el-button>
          <el-button type="info" size="medium" @click="onExport"><el-icon><download /></el-icon>导出列表</el-button>
          <el-button type="info" size="medium" @click="onPrint"><el-icon><printer /></el-icon>打印列表</el-button>
          <el-button type="success" size="medium" @click="onStatistic"><el-icon><odometer /></el-icon>统计信息</el-button>
        </el-col>
        <el-col :span="14" style="text-align: right">
          <el-input
              v-model="searchKeyword"
              placeholder="请输入关键词"            style="width: 200px; margin-right: 10px"
          ></el-input>
        </el-col>
      </el-row>

      <br />

      <el-table v-loading="loading" :data="displayData()" stripe class="table" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="userId" label="用户ID" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="userName" label="用户名" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="department" label="部门" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="role" label="职位" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="checkIn" label="上班打卡时间" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="inLocation" label="上班打卡位置" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="checkOut" label="下班打卡时间" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="outLocation" label="下班打卡位置" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column
            prop="status"
            label="考勤状态"
            width="150"
            :filters="[
            { text: '准时', value: 'On Time' },
            { text: '迟到', value: 'Late' },
            { text: '早退', value: 'Leave Early' },
            { text: '缺席', value: 'Absent' },
            { text: '迟到且缺席', value: 'Late And Leave Early' },
          ]"
            :filter-method="filterStatus"
            filter-placement="bottom-end"
        >
          <template #default="scope">
            <el-tag :type="scope.row.status === 2 ? 'primary' : 'success'" disable-transitions>{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-tooltip class="item" effect="dark" content="详情" placement="bottom">
              <el-button circle plain type="info" size="small" @click="onDetail(scope.$index, scope.row)">
                <el-icon><InfoFilled /></el-icon>
              </el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="信息修改" placement="bottom">
              <el-button circle plain type="primary" size="small" @click="onEdit(scope.$index, scope.row)">
                <el-icon><edit /></el-icon>
              </el-button>
            </el-tooltip>
            <el-tooltip  class="item" effect="dark" content="删除" placement="bottom">
              <el-button circle plain type="danger" size="small" @click="onDelete(scope.$index, scope.row)">
                <el-icon><minus /></el-icon>
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
            :current-page="param.page"
            :page-size="param.size"
            layout="sizes,prev,pager,next,total"
            :page-sizes="[2,10, 20, 50]"
            :total="param.total"
            background
            @current-change="onCurrentChange"
            @size-change="onSizeChange"
        >
        </el-pagination>
      </div>
    </el-card>
    <el-dialog v-model="detail_visible" center title="考勤详情">
      <attendance-detail :current-row="posted.userRow"></attendance-detail>
    </el-dialog>
    <el-dialog v-model="edit_visible" center :title="posted.userRow.userRole">
      <attendance-edit :current-row="posted.userRow" @success="onEditSuccess"></attendance-edit>
    </el-dialog>
    <el-dialog v-model="add_visible" title="新增考勤">
      <attendance-new @success="onCreateSuccess"></attendance-new>
    </el-dialog>
    <el-dialog v-model="statistic_visible" center title="统计信息">
      <attendance-statistic :data="data"></attendance-statistic>
    </el-dialog>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive, toRefs, computed, onMounted, watch} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {Download, Edit, InfoFilled, Minus, Odometer, Plus, Printer, Refresh, Search} from '@element-plus/icons-vue'
import AttendanceDetail from './attendanceDetail.vue'
import AttendanceEdit from './attendanceEdit.vue'
import AttendanceNew from './attendanceNew.vue'
import Service from './api/index'
import AttendanceStatistic from "./attendanceStatistic.vue"



export default defineComponent({
  name: 'AttendanceManage',
  computed: {
    Search() {
      return Search
    },
    Refresh() {
      return Refresh
    }
  },
  components: {
    Odometer,
    AttendanceDetail,
    InfoFilled,
    Printer,
    Download,
    AttendanceEdit,
    AttendanceNew,
    AttendanceStatistic,
    Edit,
    Minus,
    Plus,
    Refresh
  },
  setup() {
    const state = reactive({
      url: {
        c: '/attendance/add',
        r: '/attendance/list',
        u: '/attendance/update',
        d: '/attendance/delete'
      },
      param: {
        total: 0,
        size: 10,
        page: 1
      },
      data: [
        {
          status:'',
          userId:'',
          userName:'',
          department:'',
          role:'',
          userRole:''
        },
      ],
      filteredData: [],
      loading: false,
      is_search: false,
      add_visible: false,
      edit_visible: false,
      detail_visible: false,
      statistic_visible: false,
      posted: {
        userRow: {
          id: null,
          userId:'',
          userName: '',
          userRole: '',
          userDepartment: '',
          checkIn:'',
          checkOut:'',
          inLocation:'',
          outLocation:'',
          status:''
        }
      },
      sortField : '',
      sortOrder : '',
      ids: [] as { id: number }[],
      selectionRows: [] as { id: number }[], // 假设 userId 是字符串或数字
      searchKeyword: '' ,// 添加 searchKeyword 变量
      pickDate:new Date().toISOString().split('T')[0]
    })

    /**
     * @description 对数据进行排序
     */
    const sortData = () => {
      if (!state.sortField) return state.data;

      return [...state.data].sort((a, b) => {
        const fieldA = a[state.sortField];
        const fieldB = b[state.sortField];

        if (fieldA < fieldB) return state.sortOrder === 'asc' ? -1 : 1;
        if (fieldA > fieldB) return state.sortOrder === 'asc' ? 1 : -1;
        return 0;
      });
    }
    /**
     * @description 选择对应列排序
     */
    const handleSortChange = ({ prop, order }: { prop: string, order: 'ascending' | 'descending' | null }) => {
      state.sortField = prop;
      state.sortOrder = order === 'ascending' ? 'asc' : 'desc';
    }
    /**
     * @description 获取选择行的userId列表
     */
    const handleSelectionChange = (selection:any[]) => {
      state.selectionRows = selection.map(item => item.id)
      console.log("SelectionRows",state.selectionRows)
    }
    /**
     * @description 请求接口获取当前设置角色，默认始终有超级管理员角色
     */
    const fetchData = async () => {
      state.is_search = false
      const data = {'accessToken': sessionStorage.getItem('accessToken')}
      console.log(state.pickDate)
      const adminUserInfo = await Service.postAttendanceList(state.pickDate)
      if (adminUserInfo.status === 0) {
        state.data = adminUserInfo.data
        state.ids = adminUserInfo.data.map((item: any) => item.id)
        state.param.total = state.data.length
      }
    }
    const onCurrentChange = (val:number) => {
      state.param.page = val
    }
    const onSizeChange = (val: number) => {
      state.param.page = 1
      state.param.size = val
    }
    const onCreate = () => {
      state.add_visible = true
    }
    const onCreateSuccess = (val: any) => {
      state.add_visible = false
      fetchData()
    }
    const onEditSuccess = () => {
      state.edit_visible = false
      fetchData()
    }
    const displayData = () => {
      return state.is_search ? sortData().filter(item =>
          item.userId.toString().toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
          item.userName.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
          item.role.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
          item.department.toLowerCase().includes(state.searchKeyword.toLowerCase())||
          item.status.toLowerCase().includes(state.searchKeyword.toLowerCase())
      ).slice((state.param.page-1)*state.param.size, state.param.page*state.param.size) : sortData().slice((state.param.page-1)*state.param.size, state.param.page*state.param.size);
    }
    const onSearch = () => {
      if (state.searchKeyword != '') {
        state.is_search = true
        state.param.page = 1; // 重置页码为第一页
        state.filteredData = state.data.filter(item =>
            item.userId.toString().toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
            item.userName.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
            item.role.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
            item.department.toLowerCase().includes(state.searchKeyword.toLowerCase())||
            item.status.toLowerCase().includes(state.searchKeyword.toLowerCase())
        );
        state.param.total = state.filteredData.length
      }
      else{
        state.is_search = false
        state.param.page = 1; // 重置页码为第一页
        state.param.total = state.data.length
      }
    }
    const onDetail = (index: any, row: any) => {
      console.log('row', row)
      state.posted.userRow.userId = row.userId
      state.posted.userRow.userName = row.userName
      state.posted.userRow.userRole = row.role
      state.posted.userRow.userDepartment = row.department
      state.posted.userRow.checkIn = row.checkIn
      state.posted.userRow.inLocation= row.inLocation
      state.posted.userRow.checkOut = row.checkOut
      state.posted.userRow.outLocation = row.outLocation
      state.posted.userRow.status = row.status
      state.detail_visible = true
    }
    const onEdit = (index: any, row: any) => {
      console.log('row', row)
      state.posted.userRow.id = row.id
      state.posted.userRow.userName = row.userName
      state.posted.userRow.checkIn = row.checkIn
      state.posted.userRow.checkOut = row.checkOut
      state.edit_visible = true
    }
    const useConfirmDelete = async (row: any) => {
      ElMessageBox.confirm('此操作将删除该考勤数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
          .then(async () => {
            // 此处执行接口异步删除员工
            console.log(row.id)
            const res = await Service.postDeleteAttendance(row.id);
            if (res.status === 0) {
              ElMessage({
                type: 'success',
                message: '删除成功'
              })
              fetchData()
            } else {
              ElMessage({
                type: 'error',
                message: '删除失败'
              })
            }
          })
          .catch(() => {
            ElMessage({
              type: 'info',
              message: '已取消删除'
            })
          })
    }
    const onDelete = (index: any, row: any) => {
      console.log(index, row)
      useConfirmDelete(row)
    }
    /**
     * @description 导出列表所选行
     */
    const onExport = async() => {
      ElMessageBox.confirm('确定要导出为Excel吗', '温馨提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async() => {
        // 确认后调用获取 PDF 的方法
        const ids = state.selectionRows.length > 0 ? state.selectionRows : state.ids
        const data ={
          'accessToken': sessionStorage.getItem('accessToken'),
          'user_ids': ids
        }
        Service.postAdminExportAttendance(data).then(() => {
          ElMessage({
            type: 'success',
            message: 'Excel 文件正在下载...'
          });
        }).catch(() => {
          ElMessage({
            type: 'error',
            message: '下载失败，请重试'
          });
        });
      }).catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消'
        });
      });
    }
    /**
     * @description 打印列表所选行
     */
    const onPrint = async() => {
      ElMessageBox.confirm('确定要下载PDF吗', '温馨提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async() => {
        // 确认后调用获取 PDF 的方法
        const ids = state.selectionRows.length > 0 ? state.selectionRows : state.ids
        const data ={
          'accessToken': sessionStorage.getItem('accessToken'),
          'user_ids': ids
        }
        Service.postAdminPrintAttendance(data).then(() => {
          ElMessage({
            type: 'success',
            message: 'PDF 文件正在下载...'
          });
        }).catch(() => {
          ElMessage({
            type: 'error',
            message: '下载失败，请重试'
          });
        });
      }).catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消'
        });
      });
    }
    const onStatistic = async()=>{
      state.statistic_visible = true
    }

    const filterStatus = (value: any, row: { status: any }) => row.status === value

    // 使用 watch 监视 searchKeyword 的变化
    watch(() => state.searchKeyword, (newVal) => {
      onSearch()
    })
    //初始调用
    onMounted(() => {
      fetchData()
      displayData()
    })
    return {
      ...toRefs(state),
      displayData,
      handleSelectionChange,
      onCurrentChange,
      onSizeChange,
      onCreate,
      onCreateSuccess,
      onEditSuccess,
      onDetail,
      onStatistic,
      onEdit,
      onDelete,
      onSearch,
      onExport,
      onPrint,
      fetchData,
      handleSortChange,
      filterStatus,
    }
  }
})
</script>
<style lang="stylus" scoped></style>
