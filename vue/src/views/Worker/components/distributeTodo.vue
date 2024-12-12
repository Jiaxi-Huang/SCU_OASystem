<template>
  <div class="FormInfo">
    <el-row>
      <el-col :offset="1" :span="22">
        <div class="grid-content bg-purple-dark">
          <el-card class="box-card">
            <div style="text-align: left">
              <span>分发待办事项</span>
              <el-divider></el-divider>
            </div>
            <el-form ref="activityForm" style="text-align: left" :model="sizeForm" label-width="80px" size="mini">
              <el-form-item label="标题">
                <el-input v-model="sizeForm.todo_title"></el-input>
              </el-form-item>
              <el-form-item label="内容">
                <el-input v-model="sizeForm.todo_ctnt" autosize type="textarea"/>
              </el-form-item>
              <el-form-item label="截止时间">
                <div>
                  <el-col :span="11">
                    <el-date-picker v-model="sizeForm.date1"
                                    type="date" placeholder="选择日期" style="width: 100%"
                                    value-format="YYYY-MM-DD"
                    ></el-date-picker>
                  </el-col>
                  <el-col class="line" :span="1">&nbsp;&nbsp;&nbsp;&nbsp;</el-col>
                  <el-col :span="12">
                    <el-time-picker v-model="sizeForm.date2" placeholder="选择时间" style="width: 100%"
                                    format="HH:mm" value-format="HH:mm"
                    ></el-time-picker>
                  </el-col>
                </div>
              </el-form-item>
              <el-form-item size="large">
                <el-button type="primary" @click="submitForm">立即创建</el-button>
                <el-button>取消</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script lang="ts">
import {computed, defineComponent, onMounted, reactive, ref} from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Edit, DeleteFilled, Check, ArrowLeft } from '@element-plus/icons-vue'
import Service from "@/views/Worker/api";

export default defineComponent({
  name: 'DistributeTodo',
  components: {
    Edit,
    DeleteFilled,
    Check,
    ArrowLeft
  },
  props: {
    userIds: {
      type: Array,
      default: () => []
    }
  },
  emits: ['success'],
  setup(props,{ emit }) {
    const router = useRouter()
    const sizeForm = reactive({
      todo_title: '',
      todo_ctnt: '',
      date1: '',
      date2: '',
      type: [],
    })
    const activityForm = ref()

    onMounted(() => {
      // eslint-disable-next-line no-console
      console.log('show sizeFormRef.value', activityForm.value)
    })
    // methods
    const submitForm = () => {
      activityForm.value.validate(async(valid: any) => {
        if (valid) {
          const record = {
            todo_title: sizeForm.todo_title,
            todo_ctnt: sizeForm.todo_ctnt,
            todo_ddl: `${sizeForm.date1  } ${  sizeForm.date2}`,
            user_ids: props.userIds,
            accessToken: sessionStorage.getItem('accessToken'),
            todo_fin : '未完成'
          }
          const res = await Service.postManagerDistributeTodo(record)
          if(res.status === 0){
            ElMessage({
              type: 'success',
              message: '分发待办事项成功'
            })
            emit('success')
          }
          else{
            ElMessage({
              type: 'warning',
              message: res.message
            })
          }
        }
      })
    }
    const resetForm = () => {
      activityForm.value.resetFields()
    }
    const handleBack = () => {
      router.go(-1)
    }

    return {
      handleBack,
      sizeForm,
      activityForm,
      submitForm,
      resetForm,
    }
  }
})
</script>

<style lang="stylus" scoped>
.FormInfo{
  margin-top:20px;
  .demo-ruleForm{
    text-align :left;
  }
  .info{
    text-align: left;
    padding-left: 20px;
    margin-bottom: 20px;
    font-size: 12px;
  }
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .box-card {
    width:100%;
  }

  .el-row {
    margin-bottom: 20px;
  }
}
</style>
