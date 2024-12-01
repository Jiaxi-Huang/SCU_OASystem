<template>
  <div class="FormInfo">
    <el-row>
      <el-col :offset="1" :span="22">
        <div class="grid-content bg-purple-dark">
          <el-card class="box-card">
            <div style="text-align: left">
              <span>报销申请</span>
              <el-divider></el-divider>
            </div>
            <el-form ref="activityForm" style="text-align: left" :model="sizeForm" label-width="80px" size="mini">
              <el-form-item label="金额">
                <el-input v-model="sizeForm.amount"></el-input>
              </el-form-item>
              <el-form-item label="内容" :label-width="formLabelWidth">
                <el-input v-model="sizeForm.description" autosize type="textarea"/>
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
import Service from "@/views/Reimbursement/api";

export default defineComponent({
  name: 'AdvanceForm',
  components: {
    Edit,
    DeleteFilled,
    Check,
    ArrowLeft
  },
  setup() {
    const router = useRouter()

    const sizeForm = reactive({
      reimbursement_id: '',
      user_id: '',
      amount: '',
      description: '',
      status: [],
      submitted_at: '',
    })

    const activityForm = ref()

    onMounted(() => {
      // eslint-disable-next-line no-console
      console.log('show sizeFormRef.value', activityForm.value)
    })
    // methods
    const submitForm = () => {
      activityForm.value.validate((valid: any): boolean => {
        if (valid) {
          const currentDateTime = new Date();
          let record = {
            user_id: localStorage.getItem('user_id'),
            amount: sizeForm.amount,
            description: sizeForm.description,
            status: '未完成',
            submitted_at: currentDateTime,
          }
          try {
            Service.addReimbursement(record).then((res) => {
            });
            ElMessage({
              type: 'success',
              message: '提交成功'
            })
          } catch (err) {
            ElMessage({
              type: 'warning',
              message: err.message
            })
            console.log('submit error')
            return false
          }
          sizeForm.reimbursement_id = ''
          sizeForm.user_id = ''
          sizeForm.amount = ''
          sizeForm.description = ''
          sizeForm.status = []
          sizeForm.submitted_at = ''
          return true
        }
        console.log('submit error')
        return false
      })
    }
    const resetForm = () => {
      activityForm.value.resetFields()
    }
    const handleBack = () => {
      router.go(-1)
    }
    const handleEdit = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      tableData[index].edit = true
    }
    /**
     * @description  useXXX写法,代替mixin有待改进的地方
     * */
    const checkEmpty = (row: any) => {
      const result = Object.keys(row).some((key) => row[key] === '')
      return result
    }
    const handleSave = (index: any, row: any): Boolean => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      if (checkEmpty(row)) {
        ElMessage.warning('保存前请完善信息！')
        return false
      }
      // save current row data and update table data;
      tableData[index].edit = false
      tableData[index] = row
      ElMessage({
        type: 'success',
        message: '保存成功'
      })
      return true
    }
    const handleDelete = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      tableData.splice(index, 1)
    }
    // 新增一条记录
    const handleAddRecord = () => {
      tableData.push({
        province: '',
        city: '',
        name: '',
        address: '',
        edit: true
      })
    }
    return {
      handleAddRecord,
      handleEdit,
      handleSave,
      handleDelete,
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
