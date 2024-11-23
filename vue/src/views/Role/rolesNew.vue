<template>
  <div v-loading="loading" class="new">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="100px" title="新增员工">
      <el-form-item label="员工ID" prop="userId">
        <el-input v-model="form.userId" placeholder="请输入员工ID"></el-input>
      </el-form-item>
      <el-form-item label="员工部门" prop="userDepartment">
        <el-select v-model="form.userDepartment" placeholder="请选择部门">
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
import { defineComponent, reactive, toRefs, ref } from 'vue'

export default defineComponent({
  name: 'RoleNew',
  emits: ['success'],
  setup(props, { emit }) {
    const rules = {
      userId: [
        { required: true, message: '请输入员工ID', trigger: 'blur' },
        { type:'number', message: '请输入有效数字', trigger: 'blur' }
      ],
      userDepartment: [
        { required: true, message: '请选择部门', trigger: 'change' },
      ],
      userRole: [
        { required: true, message: '请输入员工职能', trigger: 'change' },
      ]
    }
    const departments = [
      { value: 'IT', label: '技术部' },
      { value: 'Market', label: '市场部' },
      { value: 'HR', label: '人力资源部' }
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
        roleName: '',
        remark: ''
      },
      loading: false
    })
    /**
     * @description 提交新建角色处理函数
     */
    const submitForm = () => {
      formRef.value.validate((valid: any): boolean => {
        if (valid) {
          emit('success', { ...state.form })
          return true
        }
        // eslint-disable-next-line no-console
        console.log('error submit!!')
        return false
      })
    }
    return {
      submitForm,
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
