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
                <el-form-item label="请假类型">
                  <el-select v-model="sizeForm.leave_type">
                    <el-option
                      v-for="item in leave_type_options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
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
                <el-form-item label="报送">
                  <el-select
                      v-model="sizeForm.notified_user"
                      collapse-tags
                      placeholder="选择要报送的用户"
                      style="width: 240px"
                  >
                    <el-option
                        v-for="item in all_users"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="抄送">
                  <el-select
                      v-model="sizeForm.cc_user"
                      collapse-tags
                      placeholder="选择要抄送的用户"
                      style="width: 240px"
                      multiple
                  >
                    <el-option
                        v-for="item in all_users"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    />
                  </el-select>
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
        leave_id: 0,
        leave_user_id: '',
        leave_start_time1: '',
        leave_start_time2: '',
        leave_end_time1: '',
        leave_end_time2: '',
        leave_type: '',
        leave_reason: '',
        leave_status: '未审核',
        leave_submitted_at: '',
        cc_user: [],
        notified_user: 0,
      })
      const all_users = ref([]);
      const leave_type_options= [
        {
          value: '事假',
          label: '事假',
        },
        {
          value: '病假',
          label: '病假',
        },
        {
          value: '年假',
          label: '年假',
        },
        {
          value: '调休',
          label: '调休',
        },
        {
          value: '婚假',
          label: '婚假',
        },
        {
          value: '产假',
          label: '产假',
        },
        {
          value: '陪产假',
          label: '陪产假',
        }
      ];

      const activityForm = ref()

      const getAllUsers = () => {
        Service.getAllUsers()
            .then((res) => {
              const users = res.data[0] || [];
              all_users.value = users.map((user: any) => ({
                value: user.userId,
                label: user.username || user.email,
              }));
              // console.log(" leave all_users updated:", JSON.stringify(all_users));
            })
            .catch((err) => {
              console.error("Failed to fetch users:", err);
              all_users.value = [];
            });
      }
  
      onMounted(() => {
        getAllUsers();
      })
      // methods
      const submitForm = () => {
        activityForm.value.validate((valid: any): boolean => {
          if (valid) {
            let leave_submitted_at = new Date();
            let record = {
              accessToken: sessionStorage.getItem('accessToken'),
              leave_reason: sizeForm.leave_reason,
              leave_start_time: sizeForm.leave_start_time1 + "-" + sizeForm.leave_start_time2,
              leave_end_time: sizeForm.leave_end_time1 + "-" + sizeForm.leave_end_time2,
              leave_submitted_at: leave_submitted_at.getFullYear() + "-" + leave_submitted_at.getMonth() + 1 + "-"
                                + leave_submitted_at.getDate() + "-" + leave_submitted_at.getHours + "-"
                                + leave_submitted_at.getMinutes(),
              leave_status: '未审核',
              leave_review_user_id: sizeForm.notified_user
            }
            console.log("Submission form:", JSON.stringify(record))
            try {
              Service.addLeaveApproval(record).then((res) => {
                sizeForm.leave_id = res.data.leave_id
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
            let notify_record = {
              notified_user_id: sizeForm.notified_user,
              request_type: 'reimbursement',
              request_id: sizeForm.leave_id,
              submitted_at: leave_submitted_at.getFullYear() + "-" + leave_submitted_at.getMonth() + 1 + "-"
                  + leave_submitted_at.getDate() + "-" + leave_submitted_at.getHours + "-"
                  + leave_submitted_at.getMinutes(),
            }
            console.log("Submission form:", JSON.stringify(record))
            try {
              Service.addNotification(record).then((res) => {
                sizeForm.leave_id = res.data.leave_id
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
            sizeForm.leave_reason = ''
            sizeForm.leave_user_id = ''
            sizeForm.leave_status = '未审核'
            sizeForm.leave_type = ''
            sizeForm.notified_user = 0
            sizeForm.cc_user = []
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

      return {
        handleBack,
        sizeForm,
        all_users,
        activityForm,
        leave_type_options,
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
