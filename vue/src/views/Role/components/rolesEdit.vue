<template>
  <div v-loading="loading" class="new">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="100px" title="新增员工">
      <el-form-item label="名称">
        <span v-if="row && row.userName">{{ row.userName }}</span>
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
        <el-button size="mini" type="primary" @click="saveData()"> <i class="fa fa-plus"> </i> 修改 </el-button>
      </el-row>
    </el-form>
  </div>
</template>
<script lang="ts">
import { computed, defineComponent, onMounted, watchEffect, reactive, toRef, toRefs } from 'vue'
import { useStore } from '@/store'
import Service from '../api'

interface stateTypes {
  url: String
  purl: String
  loading: Boolean
  form: {
    userName:string
    userDepartment:string
    userRole:string
  }
  menu: {
    loading: Boolean
    url: String
    data: { key: String; label: String }[]
    form: String[]
  }
}
export default defineComponent({
  name: 'RolesEdit',
  props: {
    currentRow: {
      type: Object,
      default: () => ({ userName:'',userDepartment:'',userRole: ''})
    }
  },
  emits: ['success'],

  setup(props, { emit }) {
    // 析构获取 props 属性 basePath
    const currentRow = toRef(props, 'currentRow')
    const store = useStore()
    const lang = computed(() => store.getters['settingsModule/getLangState'])

    const state = reactive<stateTypes>({
      url: `/role/allow`,
      purl: `/role/permissions`,
      loading: false,
      form: {
        userName: '',
        userRole: '',
        userDepartment: ''
      },
      menu: {
        loading: false,
        url: `/menu/list`,
        data: [],
        form: []
      }
    })
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
    const row = computed(() => currentRow.value)
    /**
     * @description 保存当前角色信息
     */
    const saveData = async() => {
      console.log('form is ', state.menu.form)
      //  省略接口：向后端接口传递已经授权菜单名称；  state.menu.form
      const data = {
        accessToken: sessionStorage.getItem('accessToken'),
        userName: state.form.userName,
        userDepartment: state.form.userDepartment,
        userRole: state.form.userRole,
        userId: row.value.userId
      }
      const res = await Service.postAdminUpdateUser(data)
      if(res.status === 0) {
        emit('success')
      }
    }

    // 使用watchEffect 监听所用到的变化时做出的副作用反应；
    watchEffect(() => {
      if (row.value) {
        state.form.userName = row.value.userName
        state.form.userRole = row.value.userRole
        state.form.userDepartment = row.value.userDepartment
      }
    })
    return {
      ...toRefs(state),
      rules,
      departments,
      roles,
      lang,
      row,
      saveData
    }
  }
})
</script>
<style lang="stylus" scoped>
.btns {
  text-align: right;
}

.el-transfer {
  display: inline-block;
  text-align: left;
}

.transfer {
  text-align: center;
}

.card-header {
  text-align: left;
}
</style>
