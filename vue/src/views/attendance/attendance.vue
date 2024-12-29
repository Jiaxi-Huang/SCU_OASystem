<template>
  <div>
    <el-alert title="Tips:点击【新增】按钮进行新增员工；点击【编辑】按钮，对员工的部门以及职能信息进行操作！" type="warning"> </el-alert>
    <el-alert title="Tips:权限控制体验：【管理员账号为：admin@outlook.com】、【超级管理员账号为：super@outlook.com】" type="info"> </el-alert>
    <el-card class="card-ctrl">
      <el-row style="margin: 15px">

      </el-row>
      <el-row>
        <el-col :span="10" style="text-align: left">
          <el-button type="primary" size="medium" @click="onCheckIn">上班打卡</el-button>
          <el-button type="primary" size="medium" @click="onCheckOut">下班打卡</el-button>
        </el-col>
      </el-row>

      <br />

      <el-table v-loading="loading" :data="displayData()" stripe class="table" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="userId" label="用户ID" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="userName" label="用户名" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="department" label="部门" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="role" label="职位" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="attendanceDate" label="日期" align="center" sortable @sort-change="handleSortChange"></el-table-column>
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
      </el-table>
    </el-card>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive, toRefs, computed, onMounted, watch} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {Download, Edit, Minus, Plus, Printer, Refresh, Search} from '@element-plus/icons-vue'
import RoleEdit from './attendanceEdit.vue'
import RoleNew from './attendanceNew.vue'
import Service from './api/index'


export default defineComponent({
  name: 'RoleManage',
  computed: {
    Search() {
      return Search
    },
    Refresh() {
      return Refresh
    }
  },
  components: {
    Printer,
    Download,
    RoleEdit,
    RoleNew,
    Edit,
    Minus,
    Plus,
    Refresh
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
      posted: {
        userRow: {
          id: null,
          userName: '',
          userRole: '',
          userDepartment: '',
          checkIn:'',
          checkOut:''
        }
      },
      sortField : '',
      sortOrder : '',
      userIds: [] as { userId: number }[],
      selectionRows: [] as { userId: number }[], // 假设 userId 是字符串或数字
      searchKeyword: '' // 添加 searchKeyword 变量
    })
    const onCheckIn = async () => {
      try {
        const confirm = await ElMessageBox.confirm('上班打卡, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        if (confirm) {
          const data = {'accessToken': sessionStorage.getItem('accessToken')};
          const res = await Service.postCheckInAttendance(data.accessToken);

          if (res.status === 0) {
            ElMessage({
              type: 'success',
              message: '上班打卡成功'
            });
            fetchData(); // 重新获取数据
          } else if (res.status === -1) {
            ElMessage({
              type: 'warning',
              message: '已经上班打卡过了'
            });
          } else {
            ElMessage({
              type: 'error',
              message: '打卡失败'
            });
          }
        }
      } catch (error) {
        console.log(error);
        ElMessage({
          type: 'info',
          message: '已取消打卡'
        });
      }
    };


    const onCheckOut = async () => {
      ElMessageBox.confirm('下班打卡, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
          .then(async () => {
            const data = {'accessToken': sessionStorage.getItem('accessToken')}
            const res = await Service.postCheckOutAttendance(data.accessToken);
            if (res.status === 0) {
              ElMessage({
                type: 'success',
                message: '下班打卡成功'
              })
              fetchData()
            }
            else if(res.status === -1){
              ElMessage({
                type: 'warning',
                message: '已经下班打卡过了'
              })
            }
            else{
              ElMessage({
                type: 'error',
                message: '打卡失败'
              })
            }
          })
          .catch(() => {
            ElMessage({
              type: 'info',
              message: '已取消打卡'
            })
          })
    }



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
      const adminUserInfo = await Service.postPersonalAttendance(data)
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
    const onEdit = (index: any, row: any) => {
      console.log('row', row)
      state.posted.userRow.id = row.id
      state.posted.userRow.userName = row.userName
      state.posted.userRow.checkIn = row.checkIn
      state.posted.userRow.checkOut = row.checkOut
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
      onEdit,
      onDelete,
      onSearch,
      onExport,
      onPrint,
      fetchData,
      handleSortChange,
      filterStatus,
      onCheckIn,
      onCheckOut
    }
  }
})
</script>
<style lang="stylus" scoped></style>
