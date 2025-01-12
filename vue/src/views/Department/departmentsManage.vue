<template>
  <div>
    <el-card class="card-ctrl">
      <el-row>
        <el-col :span="10" style="text-align: left">
          <el-button type="primary" size="medium" @click="onCreate"><el-icon><plus /></el-icon>新增部门</el-button>
        </el-col>
        <el-col :span="14" style="text-align: right">
          <el-input
              v-model="searchKeyword"
              placeholder="请输入关键词"            style="width: 200px; margin-right: 10px"
          ></el-input>
        </el-col>
      </el-row>
      <br />
      <el-table v-loading="loading" :data="displayData()" stripe class="table">
        <el-table-column prop="Department" label="部门名" align="center" sortable @sort-change="handleSortChange"></el-table-column>

        <el-table-column label="操作" align="center">
          <template #default="scope">
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
    <el-dialog v-model="add_visible" title="新增部门">
      <department-new @success="onCreateSuccess"></department-new>
    </el-dialog>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive, toRefs, computed, onMounted, watch, nextTick} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {InfoFilled, Minus, Plus,} from '@element-plus/icons-vue'
import DepartmentNew from './components/departmentsNew.vue'
import Service from './api/index'

export default defineComponent({
  name: 'DepartmentManage',
  components: {
    InfoFilled,
    DepartmentNew,
    Minus,
    Plus
  },
  setup() {
    const state = reactive({
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
      sortField : '',
      sortOrder : '',
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
     * @description 请求接口获取当前设置角色，默认始终有超级管理员角色
     */
    const fetchData = async () => {
      state.is_search = false
      const data = {'accessToken': sessionStorage.getItem('accessToken')}
      const adminUserInfo = await Service.postAdminQueryDepartmentList(data)
      if (adminUserInfo.status === 0) {
        state.data = adminUserInfo.data
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
        onCurrentChange,
        onSizeChange,
        onCreate,
        onCreateSuccess,
        onDelete,
        onSearch,
        fetchData,
        handleSortChange
      }
    }
})
</script>
<style lang="stylus" scoped></style>
