<template>
  <div>
    <el-alert title="Tips:点击【新增】按钮进行新增员工；点击【编辑】按钮，对员工的部门以及职能信息进行操作！" type="warning"> </el-alert>
    <el-alert title="Tips:权限控制体验：【管理员账号为：admin@outlook.com】、【超级管理员账号为：super@outlook.com】" type="info"> </el-alert>
    <el-card class="card-ctrl">
      <el-row>
        <el-col :span="8" style="text-align: left">
          <el-button type="primary" size="small" @click="onCreate">
            <el-icon><plus /></el-icon>
            新增用户</el-button
          >
        </el-col>
      </el-row>
      <br />
      <el-table v-loading="loading" :data="data" stripe class="table">
        <el-table-column prop="userId" label="用户ID" align="center"></el-table-column>
        <el-table-column prop="userName" label="用户名" align="center"></el-table-column>
        <el-table-column prop="userDepartment" label="部门" align="center"></el-table-column>
        <el-table-column prop="userRole" label="职位" align="center"></el-table-column>
        <el-table-column prop="userPhone" label="电话号码" align="center"></el-table-column>

        <el-table-column label="操作" align="center">
          <template #default="scope">
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
      <role-edit :current-row="posted.userRow" @success="onEditSuccess"></role-edit>
    </el-dialog>
    <el-dialog v-model="add_visible" title="新增员工">
      <role-new @success="onCreateSuccess"></role-new>
    </el-dialog>
  </div>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Minus, Plus, Refresh } from '@element-plus/icons-vue'
import RoleEdit from './rolesEdit.vue'
import RoleNew from './rolesNew.vue'
import Service from './api/index'

export default defineComponent({
  name: 'RoleManage',
  components: {
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
        limit: 10,
        page: 1
      },
      data: [
        //{ userName: '超级管理员', userDepartment:'',userRole:'',userPhone:''},
      ],
      loading: false,
      is_search: false,
      add_visible: false,
      edit_visible: false,
      detail_visible: false,
      posted: {
        userRow: {
          userId: null,
          userName:'',
          userRole: '',
          userDepartment:''
        }
      }
    })
    // 动态计算total;
    const total = computed(() => state.data.length)
    /**
     * @description 请求接口获取当前设置角色，默认始终有超级管理员角色
     */
    const fetchData = async() => {
      state.is_search = false
      const data = {'accessToken':sessionStorage.getItem('accessToken')}
      const adminUserInfo = await Service.postAdminQueryUserList(data)
      if (adminUserInfo.status === 0) {
        state.data = adminUserInfo.data
      }
    }
    const onCurrentChange = () => {
      fetchData()
    }
    const onSizeChange = (val: number) => {
      state.param.limit = val
      fetchData()
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
    const useConfirmDelete = async(row: any) => {
      ElMessageBox.confirm('此操作将删除该员工所有数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
          .then(async() => {
            // 此处执行接口异步删除员工
            const data = {
              userId : row.userId,
              accessToken: sessionStorage.getItem('accessToken')
            }
            const res = await Service.postAdminDeleteUser(data);
            if(res.status === 0) {
              ElMessage({
                type: 'success',
                message: '删除成功'
              })
              fetchData()
            }
            else{
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
    //初始调用
    fetchData()
    return {
      ...toRefs(state),
      total,
      onCurrentChange,
      onSizeChange,
      onCreate,
      onCreateSuccess,
      onEditSuccess,
      onEdit,
      onDelete,
      fetchData
    }
  }
})
</script>
<style lang="stylus" scoped></style>
