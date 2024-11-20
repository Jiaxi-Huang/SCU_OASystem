<template>
    <div class="FormInfo">
      <el-row>
        <el-col :offset="1" :span="22">
          <div class="grid-content bg-purple-dark">
            <el-card class="box-card">
              <div style="text-align: left">
                <span>请假申请</span>
                <el-divider></el-divider>
              </div>
              <el-form ref="activityForm" style="text-align: left" :model="sizeForm" label-width="80px" size="small">
                <el-form-item label="请假原因">
                  <el-input v-model="sizeForm.leave_reason"></el-input>
                </el-form-item>
                <el-form-item label="开始时间">
                  <el-col :span="11">
                    <el-date-picker v-model="sizeForm.leave_start_time1"
                                    type="date" placeholder="选择日期" style="width: 100%"
                                    value-format="YYYY-MM-DD"
                    ></el-date-picker>
                  </el-col>
                  <el-col class="line" :span="1">&nbsp;&nbsp;&nbsp;&nbsp;-</el-col>
                  <el-col :span="12">
                    <el-time-picker v-model="sizeForm.leave_start_time2" placeholder="选择时间" style="width: 100%"
                                    format="HH:mm" value-format="HH:mm"
                    ></el-time-picker>
                  </el-col>
                </el-form-item>
                <el-form-item label="结束时间">
                  <el-col :span="11">
                    <el-date-picker v-model="sizeForm.leave_end_time1"
                                    type="date" placeholder="选择日期" style="width: 100%"
                                    value-format="YYYY-MM-DD"
                    ></el-date-picker>
                  </el-col>
                  <el-col class="line" :span="1">&nbsp;&nbsp;&nbsp;&nbsp;-</el-col>
                  <el-col :span="12">
                    <el-time-picker v-model="sizeForm.leave_end_time2" placeholder="选择时间" style="width: 100%"
                                    format="HH:mm" value-format="HH:mm"
                    ></el-time-picker>
                  </el-col>
                </el-form-item>
  
                <el-form-item size="large">
                  <el-button type="primary" @click="submitForm">提交申请</el-button>
                  <el-button>取消</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </div></el-col
        >
      </el-row>
    </div>
  </template>
  <script lang="ts">
  import {computed, defineComponent, onMounted, reactive, ref} from 'vue'
  import { useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'
  import { Edit, DeleteFilled, Check, ArrowLeft } from '@element-plus/icons-vue'
  import Service from "@/views/LeaveApproval/api";
import { lowerCase } from 'lodash';
  
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
        leave_start_time1: '',
        leave_start_time2: '',
        leave_end_time1: '',
        leave_end_time2: '',
        leave_reason: [],
        leave_user_id: '',
        leave_status: '未审核',
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
            let leave_submit_at = new Date();
            let record = {
              leave_reason: sizeForm.leave_reason,
              leave_start_time: sizeForm.leave_start_time1 + "-" + sizeForm.leave_start_time2,
              leave_end_time: sizeForm.leave_end_time1 + "-" + sizeForm.leave_end_time2,
              leave_user_id: sizeForm.leave_user_id,
              leave_submit_at: leave_submit_at.getFullYear() + "-" + leave_submit_at.getMonth() + 1 + "-"
                                + leave_submit_at.getDate() + "-" + leave_submit_at.getHours + "-" 
                                + leave_submit_at.getMinutes(),
              //得到当前时间作为提交时间
              leave_status: '未审核',
            }
            try {
              Service.addLeaveApproval(record).then((res) => {
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
            sizeForm.leave_start_time1 = ''
            sizeForm.leave_start_time2 = ''
            sizeForm.leave_end_time1 = ''
            sizeForm.leave_end_time2 = ''
            sizeForm.leave_reason = []
            sizeForm.leave_user_id = ''
            sizeForm.leave_status = '未审核'
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
  