<template>
  <div v-loading="loading" class="new">
    <div style="display: flex; justify-content: center; align-items: center;">
      <el-image
          v-if="row.userAvatar" style="width: 100px; height: 100px; border-radius: 50%;"
          :src="row.userAvatar"
          fit="cover"
          referrerPolicy="no-referrer"
      />
      <div v-else style="text-align: center;">无头像</div>
    </div>
    <el-descriptions :column="1" border>
      <el-descriptions-item label="员工编号">{{ row.userId }}</el-descriptions-item>
      <el-descriptions-item label="名称">{{ row.userName }}</el-descriptions-item>
      <el-descriptions-item label="员工部门">{{ departmentLabel(row.userDepartment) }}</el-descriptions-item>
      <el-descriptions-item label="员工职位">{{ roleLabel(row.userRole) }}</el-descriptions-item>
      <el-descriptions-item label="邮箱">{{ row.userEmail }}</el-descriptions-item>
      <el-descriptions-item label="手机号码">{{ row.userPhone }}</el-descriptions-item>
    </el-descriptions>
  </div>
</template>
<script lang="ts">
import { computed, defineComponent, reactive, toRef, toRefs } from 'vue'

interface stateTypes {
  loading: Boolean
  form: {
    userId:number
    userName:string
    userDepartment:string
    userRole:string
    userEmail:string
    userPhone:string
    userAvatar:string
  }
}
export default defineComponent({
  name: 'WorkerDetail',
  props: {
    currentRow: {
      type: Object,
      default: () => ({ userId:null,userName:'',userDepartment:'',userRole: '',userPhone:'',userAvatar: ''})
    }
  },
  setup(props) {
    // 析构获取 props 属性 basePath
    const currentRow = toRef(props, 'currentRow')
    const state = reactive<stateTypes>({
      loading: false,
      form: {
        userId: 1,
        userName: '',
        userRole: '',
        userDepartment: '',
        userPhone:'',
        userEmail: '',
        userAvatar: '',
      }
    })
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
    // 方法用于获取部门标签文本
    const departmentLabel = (value: string): string => {
      const dept = departments.find(dept => dept.value === value);
      return dept ? dept.label : '未知';
    }

    // 方法用于获取角色标签文本
    const roleLabel = (value: string): string => {
      const role = roles.find(role => role.value === value);
      return role ? role.label : '未知';
    }
    const row = computed(() => currentRow.value)
    // 使用watchEffect 监听所用到的变化时做出的副作用反应；
    return {
      ...toRefs(state),
      departments,
      roles,
      departmentLabel,
      roleLabel,
      row
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
