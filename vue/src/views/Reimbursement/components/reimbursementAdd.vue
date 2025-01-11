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
            <el-form ref="activityForm" style="text-align: left" :model="sizeForm" label-width="80px" size="small">
              <el-form-item label="金额">
                <el-input v-model="sizeForm.amount"></el-input>
              </el-form-item>

              <el-form-item label="内容">
                <el-input v-model="sizeForm.description" autosize type="textarea"/>
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
                <el-button type="primary" @click="submitForm">立即创建</el-button>
                <el-button @click="resetForm">取消</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {ArrowLeft, Check, DeleteFilled, Edit} from '@element-plus/icons-vue'
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
      reimbursement_id: 0,
      user_id: 0,
      amount: '',
      description: '',
      status: [],
      submitted_at: '',
      cc_user: [], // 抄送人数组
      notified_user: 0,
    })

    const all_users = ref([]);

    const activityForm = ref()

    const getAllUsers = () => {
      Service.getAllUsers()
          .then((res) => {
            const users = res.data[0] || [];
            all_users.value = users.map((user: any) => ({
              value: user.userId,
              label: user.username || user.email,
            }));
          })
          .catch((err) => {
            console.error("Failed to fetch users:", err);
            all_users.value = [];
          });
    }

    onMounted(() => {
      getAllUsers();
    })

    const submitForm = () => {
      activityForm.value.validate((valid: any): boolean => {
        if (valid) {
          const currentDateTime = new Date();
          function formatDate(date: any) {
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            const seconds = String(date.getSeconds()).padStart(2, '0');

            return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
          }
          const formattedDate = formatDate(currentDateTime);

          let record = {
            accessToken: sessionStorage.getItem('accessToken'),
            amount: sizeForm.amount,
            description: sizeForm.description,
            status: '未审核',
            submitted_at: formattedDate,
            review_user_id: sizeForm.notified_user,
            cc_user: sizeForm.cc_user, // 传递抄送人数组
          }

          console.log("Submission form:", JSON.stringify(record))
          try {
            Service.addReimbursement(record).then((res) => {
              sizeForm.reimbursement_id = res.data.reimbursement_id
              console.log("Reimbursement ID:", sizeForm.reimbursement_id)

              // 创建抄送记录
              if (sizeForm.cc_user && sizeForm.cc_user.length > 0) {
                sizeForm.cc_user.forEach((ccUserId: number) => {
                  let ccRecord = {
                    notified_user_id: ccUserId,
                    request_type: 'reimbursement',
                    request_id: sizeForm.reimbursement_id,
                    submitted_at: currentDateTime,
                  }
                  Service.addNotification(ccRecord).then((res) => {
                    console.log("CC Notification added:", res.data);
                  });
                });
              }

              ElMessage({
                type: 'success',
                message: '提交成功'
              })
            });
          } catch (err) {
            ElMessage({
              type: 'warning',
              message: err.message
            })
          }

          sizeForm.reimbursement_id = 0
          sizeForm.user_id = 0
          sizeForm.amount = ''
          sizeForm.description = ''
          sizeForm.status = []
          sizeForm.submitted_at = ''
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

    return {
      sizeForm,
      all_users,
      activityForm,
      submitForm,
      resetForm,
    }
  }
})
</script>
