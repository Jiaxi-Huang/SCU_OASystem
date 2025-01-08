<template>
  <div v-loading="loading" class="new">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="日志编号">{{ row.id }}</el-descriptions-item>
      <el-descriptions-item label="操作用户编号">{{ row.userId }}</el-descriptions-item>
      <el-descriptions-item label="操作内容">{{ row.logContent }}</el-descriptions-item>
      <el-descriptions-item label="操作时间">{{ row.logDate }}</el-descriptions-item>
    </el-descriptions>
  </div>
</template>
<script lang="ts">
import { computed, defineComponent, reactive, toRef, toRefs } from 'vue'

interface stateTypes {
  loading: Boolean
  form: {
    id:number
    userId:number
    logContent:string
    logDate:string
  }
}
export default defineComponent({
  name: 'LogDetail',
  props: {
    currentRow: {
      type: Object,
      default: () => ({ id:null,userId:null,logContent:'',logDate:''})
    }
  },
  setup(props) {
    // 析构获取 props 属性 basePath
    const currentRow = toRef(props, 'currentRow')
    const state = reactive<stateTypes>({
      loading: false,
      form: {
        id:1,
        userId: 1,
        logContent: '',
        logDate: ''
      }
    })
    const row = computed(() => currentRow.value)
    return {
      ...toRefs(state),
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
