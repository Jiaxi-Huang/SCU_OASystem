<template>
  <div>
    <el-card class="card-ctrl">
      <el-row>
        <el-col :span="10" style="text-align: left">
          <el-button type="primary" size="medium" @click="onCreate"><el-icon><plus /></el-icon>新增用户</el-button>
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
        <el-table-column prop="userDepartment" label="部门" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="userRole" label="职位" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="userPhone" label="电话号码" align="center" sortable @sort-change="handleSortChange"></el-table-column>

        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-tooltip class="item" effect="dark" content="详情" placement="bottom">
              <el-button circle plain type="info" size="small" @click="onDetail(scope.$index, scope.row)">
                <el-icon><InfoFilled /></el-icon>
              </el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="修改" placement="bottom">
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
          :page-sizes="[10, 20, 50]"
          :total="param.total"
          background
          @current-change="onCurrentChange"
          @size-change="onSizeChange"
        >
        </el-pagination>
      </div>
    </el-card>
    <el-dialog v-model="detail_visible" center title="员工信息详情">
      <role-detail :current-row="posted.userRow"></role-detail>
    </el-dialog>
    <el-dialog v-model="statistic_visible" center title="统计信息">
      <role-statistic :data="data"></role-statistic>
    </el-dialog>
    <el-dialog v-model="edit_visible" center :title="posted.userRow.userRole">
      <role-edit :current-row="posted.userRow" @success="onEditSuccess"></role-edit>
    </el-dialog>
    <el-dialog v-model="add_visible" title="新增员工">
      <role-new @success="onCreateSuccess"></role-new>
    </el-dialog>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive, toRefs, computed, onMounted, watch, nextTick} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {Download, Edit, InfoFilled, Minus, Plus, Printer, Refresh, Odometer} from '@element-plus/icons-vue'
import RoleDetail from './components/rolesDetail.vue'
import RoleEdit from './components/rolesEdit.vue'
import RoleNew from './components/rolesNew.vue'
import RoleStatistic from "./components/rolesStatistic.vue"
import Service from './api/index'

export default defineComponent({
  name: 'RoleManage',
  components: {
    InfoFilled,
    Printer,
    Download,
    RoleDetail,
    RoleEdit,
    RoleNew,
    RoleStatistic,
    Edit,
    Minus,
    Plus,
    Refresh,
    Odometer
  },
  setup() {
    const state = reactive({
      url: {
        c: '/role/add',
        r: '/role/list',
        u: '/role/update',
        d: '/role/delete'
      },
      param: {
        total: 0,
        size: 10,
        page: 1
      },
      data: [
        //{ userName: '超级管理员', userDepartment:'',userRole:'',userPhone:''},
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
          userId: null,
          userName: '',
          userRole: '',
          userDepartment: '',
          userEmail: '',
          userPhone: '',
          userAvatar: '',
        }
      },
      sortField : '',
      sortOrder : '',
      userIds: [] as { userId: number }[],
      selectionRows: [] as { userId: number }[], // 假设 userId 是字符串或数字
      searchKeyword: '' // 添加 searchKeyword 变量
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
      state.selectionRows = selection.map(item => item.userId)
      console.log("SelectionRows",state.selectionRows)
    }
    /**
     * @description 请求接口获取当前设置角色，默认始终有超级管理员角色
     */
    const fetchData = async () => {
      state.is_search = false
      const data = {'accessToken': sessionStorage.getItem('accessToken')}
      const adminUserInfo = await Service.postAdminQueryUserList(data)
      if (adminUserInfo.status === 0) {
        state.data = adminUserInfo.data
        state.userIds = adminUserInfo.data.map((item: any) => item.userId)
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
          item.userName.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
          item.userRole.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
          item.userDepartment.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
          item.userPhone.toLowerCase().includes(state.searchKeyword.toLowerCase())
      ).slice((state.param.page-1)*state.param.size, state.param.page*state.param.size) : sortData().slice((state.param.page-1)*state.param.size, state.param.page*state.param.size);
    }
    const onSearch = () => {
      if (state.searchKeyword != '') {
        state.is_search = true
        state.param.page = 1; // 重置页码为第一页
        state.filteredData = state.data.filter(item =>
            item.userName.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
            item.userRole.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
            item.userDepartment.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
            item.userPhone.toLowerCase().includes(state.searchKeyword.toLowerCase())
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
      state.posted.userRow.userRole = row.userRole
      state.posted.userRow.userDepartment = row.userDepartment
      state.posted.userRow.userEmail = row.userEmail
      state.posted.userRow.userPhone = row.userPhone
      state.posted.userRow.userAvatar = row.userAvatar
      state.detail_visible = true
    }
      const onEdit = (index: any, row: any) => {
        console.log('row', row)
        state.posted.userRow.userId = row.userId
        state.posted.userRow.userName = row.userName
        state.posted.userRow.userRole = row.userRole
        state.posted.userRow.userDepartment = row.userDepartment
        state.edit_visible = true
      }
      const useConfirmDelete = async (row: any) => {
        ElMessageBox.confirm('此操作将删除该员工所有数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
            .then(async () => {
              // 此处执行接口异步删除员工
              const data = {
                userId: row.userId,
                accessToken: sessionStorage.getItem('accessToken')
              }
              const res = await Service.postAdminDeleteUser(data);
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
        const userIds = state.selectionRows.length > 0 ? state.selectionRows : state.userIds
        const data ={
          'accessToken': sessionStorage.getItem('accessToken'),
          'user_ids': userIds
        }
        Service.postAdminExportUser(data).then(() => {
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
        const userIds = state.selectionRows.length > 0 ? state.selectionRows : state.userIds
        const data ={
          'accessToken': sessionStorage.getItem('accessToken'),
          'user_ids': userIds
        }
        Service.postAdminPrintUser(data).then(() => {
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
        onEdit,
        onDelete,
        onSearch,
        onExport,
        onPrint,
        onStatistic,
        fetchData,
        handleSortChange
      }
    }
})
</script>
<style lang="stylus" scoped></style>
