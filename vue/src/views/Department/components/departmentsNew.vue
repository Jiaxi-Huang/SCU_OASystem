<template>
  <div v-loading="loading" class="new">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="100px" title="新增部门">
      <el-form-item label="部门名" prop="departmentName">
        <el-input v-model="form.departmentName" placeholder="请输入部门名"></el-input>
      </el-form-item>
      <el-row class="btn-container">
        <el-button size="mini" type="primary" @click="submitForm()"> <i class="fa fa-plus"> </i> 新增 </el-button>
      </el-row>
    </el-form>
  </div>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, ref } from 'vue'
import Service from '../api'
export default defineComponent({
  name: 'DepartmentNew',
  emits: ['success'],
  setup(props, { emit }) {
    const rules = {
      departmentName: [
        { required: true, message: '请输入部门名', trigger: 'blur' },
      ],
    }
    const formRef = ref()
    // 只将响应式数据进行响应式处理
    const state = reactive({
      form: {
        departmentName: '',
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
            departmentName: state.form.departmentName,
          }
          const res = await Service.postAdminDepartmentAdd(data)
          if(res.status ===0) {
            emit('success')
          }
        }
      })
    }
    return {
      submitForm,
      rules,
      formRef,
      ...toRefs(state)
    }
  }
})
</script>
<style lang="stylus" scoped></style>
