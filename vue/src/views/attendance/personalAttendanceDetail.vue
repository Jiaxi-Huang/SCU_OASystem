<template>
  <div v-loading="loading" class="new">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="日期">{{ row.attendanceDate }}</el-descriptions-item>
      <el-descriptions-item label="上班打卡时间">{{ row.checkIn }}</el-descriptions-item>
      <el-descriptions-item label="上班打卡位置">{{ row.inLocation }}</el-descriptions-item>
      <el-descriptions-item label="下班打卡时间">{{ row.checkOut }}</el-descriptions-item>
      <el-descriptions-item label="下班打卡位置">{{ row.outLocation }}</el-descriptions-item>
      <el-descriptions-item label="考勤状态">{{ row.status }}</el-descriptions-item>
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
  name: 'RoleDetail',
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
