<template>
  <div>
    <el-alert title="Tips:点击【编辑】按钮，进行不同角色的菜单授权操作！" type="warning"> </el-alert>
    <el-alert title="Tips:权限控制体验：【管理员账号为：admin@outlook.com】、【超级管理员账号为：super@outlook.com】" type="info"> </el-alert>
    <el-card class="card-ctrl">
      <el-row>
        <el-col :span="10" style="text-align: left">
          <el-button type="primary" size="medium" @click="onDistributeTodo"><el-icon><plus /></el-icon>分派任务</el-button>
          <el-button type="success" size="medium" @click="onDistributeMeeting"><el-icon><plus /></el-icon>新建会议</el-button>
          <el-button type="info" size="medium" @click="onExport"><el-icon><download /></el-icon>导出列表</el-button>
          <el-button type="info" size="medium" @click="onPrint"><el-icon><printer /></el-icon>打印列表</el-button>
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
        <el-table-column prop="userName" label="用户名" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="userDepartment" label="部门" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="userRole" label="职位" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="userPhone" label="电话号码" align="center" sortable @sort-change="handleSortChange"></el-table-column>

        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-tooltip class="item" effect="dark" content="菜单授权" placement="bottom">
              <el-button circle plain type="primary" size="small" @click="onEdit(scope.$index, scope.row)">
                <el-icon><edit /></el-icon>
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          :current-page="param.page"
          :page-size="param.limit"
          layout="sizes,prev,pager,next,total"
          :page-sizes="[5, 10, 20]"
          :total="total"
          background
          @current-change="onCurrentChange"
          @size-change="onSizeChange"
        >
        </el-pagination>
      </div>
    </el-card>
    <el-dialog v-model="edit_visible" center :title="posted.userRow.userRole">
      <worker-edit :current-row="posted.userRow" @success="onEditSuccess"></worker-edit>
    </el-dialog>
    <el-dialog v-model="todo_visible" center :title="posted.userRow.userRole">
      <distribute-todo :userIds="selectionRows" @success="onDistributeTodoSuccess"></distribute-todo>
    </el-dialog>
    <el-dialog v-model="meeting_visible" center :title="posted.userRow.userRole">
      <distribute-meeting :userIds="selectionRows" @success="onDistributeMeetingSuccess"></distribute-meeting>
    </el-dialog>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive, toRefs, computed, onMounted, watch} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {Download, Edit, Minus, Plus, Printer, Refresh} from '@element-plus/icons-vue'
import WorkerEdit from './workersEdit.vue'
import DistributeTodo from "@/views/Worker/components/distributeTodo.vue";
import DistributeMeeting from "@/views/Worker/components/distributeMeeting.vue";
import Service from './api/index'

export default defineComponent({
  name: 'WorkerManage',
  components: {
    Printer,
    Download,
    WorkerEdit,
    DistributeTodo,DistributeMeeting,
    Edit,
    Minus,
    Plus,
    Refresh
  },
  setup() {
    const state = reactive({
      url: {
        c: '/worker/add',
        r: '/worker/list',
        u: '/worker/update',
        d: '/worker/delete'
      },
      param: {
        limit: 10,
        page: 1
      },
      data: [
        //{ userName: '超级管理员', userDepartment:'',userRole:'',userPhone:''},
      ],
      filteredData: [],
      loading: false,
      is_search: false,
      edit_visible: false,
      detail_visible: false,
      todo_visible: false,
      meeting_visible: false,
      posted: {
        userRow: {
          userId: null,
          userName:'',
          userDepartment:'',
          userRole: ''
        }
      },
      sortField : '',
      sortOrder : '',
      userIds: [] as { userId: number }[],
      selectionRows: [] as { userId: number }[], // 假设 userId 是字符串或数字
      searchKeyword: '' // 添加 searchKeyword 变量
    })
    // 动态计算total;
    const total = computed(() => state.data.length)
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
     * @description 请求接口获取当前设置角色，默认始终有超级管理员角色
     */
    const fetchData = async() => {
      state.is_search = false
      const data = {'accessToken':sessionStorage.getItem('accessToken')}
      const adminUserInfo = await Service.postAdminQueryUserList(data)
      if (adminUserInfo.status === 0) {
        state.data = adminUserInfo.data.map((item: any) => ({
          ...item,
          isSelect: false
        }));
        state.userIds = adminUserInfo.data.map((item: any) => item.userId)
      }
    }
    /**
     * @description 实时更新选择行的userId列表
     */
    const handleSelectionChange = (selection:any[]) => {
      state.selectionRows = selection.map(item => item.userId)
      console.log("SelectionRows",state.selectionRows)
    }
    const onCurrentChange = () => {
      fetchData()
    }
    const onSizeChange = (val: number) => {
      state.param.limit = val
      fetchData()
    }
    const onDistributeTodo = () => {
      state.todo_visible = true
    }
    const onDistributeMeeting = () => {
      state.meeting_visible = true
    }
    const onDistributeTodoSuccess = () => {
      state.todo_visible = false
    }
    const onDistributeMeetingSuccess = () => {
      state.meeting_visible = false
    }
    const onEditSuccess = () => {
      state.edit_visible = false
      fetchData()
    }
    const displayData = ()=>{
      return state.is_search ? sortData().filter(item =>
          item.userName.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
          item.userRole.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
          item.userDepartment.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
          item.userPhone.toLowerCase().includes(state.searchKeyword.toLowerCase())
      ) : sortData();
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
      }
      else{
        state.is_search = false
      }
    }
    /**
     * @description 选择点击编辑授权角色；roleName
     */
    const onEdit = (index: any, row: any) => {
      console.log('row', row)
      state.posted.userRow.userId = row.userId
      state.posted.userRow.userName = row.userName
      state.posted.userRow.userRole = row.userRole
      state.posted.userRow.userDepartment = row.userDepartment
      state.edit_visible = true
    }
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
          'accessToken':sessionStorage.getItem('accessToken'),
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
    // 使用 watch 监视 searchKeyword 的变化
    watch(() => state.searchKeyword, (newVal) => {
      if (newVal !== '') {
        onSearch()
      } else {
        state.is_search = false
      }
    })
    //初始调用
    onMounted(() => {
      fetchData()
      displayData()
    })

    return {
      ...toRefs(state),
      total,
      displayData,
      handleSelectionChange,
      onCurrentChange,
      onSizeChange,
      onDistributeTodo,
      onDistributeTodoSuccess,
      onDistributeMeeting,
      onDistributeMeetingSuccess,
      onEdit,
      onEditSuccess,
      onSearch,
      onExport,
      onPrint,
      fetchData,
      handleSortChange
    }
  }
})
</script>
<style lang="stylus" scoped></style>
