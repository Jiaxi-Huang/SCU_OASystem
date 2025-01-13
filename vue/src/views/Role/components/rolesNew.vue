<template>
  <div v-loading="loading" class="new">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="100px" title="新增员工">
      <el-form-item label="员工名" prop="userName">
        <el-input v-model="form.userName" placeholder="请输入员工名"></el-input>
      </el-form-item>
      <el-form-item label="员工部门" prop="userDepartment">
        <el-select v-model="form.departmentId" placeholder="请选择部门">
          <el-option v-for="department in departments" :key="department.value" :label="department.label" :value="department.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="员工职位" prop="userRole">
        <el-select v-model="form.userRole" placeholder="请选择职能">
          <el-option v-for="role in roles" :key="role.value" :label="role.label" :value="role.value"></el-option>
        </el-select>
      </el-form-item>
      <el-row class="btn-container">
        <el-button size="mini" type="primary" @click="submitForm()"> <i class="fa fa-plus"> </i> 新增 </el-button>
      </el-row>
    </el-form>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive, toRefs, ref, onMounted} from 'vue'
import Service from '../api'
export default defineComponent({
  name: 'RoleNew',
  emits: ['success'],
  setup(props, { emit }) {
    const rules = {
      userName: [
        { required: true, message: '请输入员工名', trigger: 'blur' },
      ],
      departmentId: [
        { required: true, message: '请选择部门', trigger: 'change' },
      ],
      userRole: [
        { required: true, message: '请输入员工职能', trigger: 'change' },
      ]
    }
    const departments = [
      { value: '1', label: 'HR' },
    ]

    const roles = [
    { value: 'admin', label: '管理员' },
    { value: 'manager', label: '部门经理' },
    { value: 'worker', label: '员工' }
    ]
    const url = `/role/add`
    const formRef = ref()
    // 只将响应式数据进行响应式处理
    const state = reactive({
      form: {
        userName: '',
        departmentId: null,
        userRole: ''
      },
      loading: false
    })
    /**
     * @description 提交新建角色处理函数
     */
    const submitForm = async() => {
      formRef.value.validate(async(valid: any) => {
        if (valid) {
          const data ={
            accessToken : sessionStorage.getItem('accessToken'),
            userName: state.form.userName,
            userDepartment: departments.find((item: any) => item.value === state.form.departmentId)?.label,
            userRole: state.form.userRole,
            departmentId: state.form.departmentId,
          }
          const res = await Service.postAdminAddUser(data)
          if(res.status ===0) {
            emit('success')
          }
        }
      })
    }
    const getDepartments = async() => {
      const res = await Service.postAdminQueryDepartmentList({accessToken: sessionStorage.getItem('accessToken')})
      if(res.status === 0) {
        departments.splice(0,departments.length)
        for(let i = 0; i < res.data.length; i++) {
          departments.push({value: res.data[i].departmentId, label: res.data[i].departmentName})
        }
      }
    }
    onMounted(()=>{
      getDepartments()
    })
    return {
      submitForm,
      getDepartments,
      rules,
      departments,
      roles,
      formRef,
      url,
      ...toRefs(state)
    }
  }
})
</script>
<style lang="stylus" scoped></style>
