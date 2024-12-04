<template>
  <meta name="referrer" content="no-referrer">
  <div class="PersonalSetting">
    <el-row>
      <el-col :offset="1" :span="22">
        <div class="grid-content bg-purple-dark">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <el-button class="button" type="text" @click="handleBack"><i class="el-icon-arrow-left" />返回</el-button>

                <span>个人设置</span>
                <div></div>
              </div>
            </template>
            <el-tabs :tab-position="tabPosition">
              <el-tab-pane label="基本设置">
                <div class="set-title">
                  <span>基本设置</span>
                </div>
                <div class="set-info">
                  <div class="form-info">
                    <el-form ref="settingFormRef" :model="settingForm" :rules="rules" label-width="100px" class="demo-ruleForm">
                      <el-form-item label="用户名称" prop="nickname">
                        <el-input v-model="settingForm.username" placeholder="请输入用户名" maxlength="10"></el-input>
                      </el-form-item>
                      <el-form-item label="联系电话" prop="mobile">
                        <el-input v-model="settingForm.phone" placeholder="请输入11位大陆手机号码"></el-input>
                      </el-form-item>
                      <el-form-item label="个人简介" prop="desc">
                        <el-input v-model="settingForm.intro" type="textarea" placeholder="个人简介" maxlength="120"></el-input>
                      </el-form-item>
                      <el-form-item>
                        <el-button type="primary" :loading="updateLoading" @click="submitForm()">更新基本信息</el-button>
                        <el-button @click="resetForm()">重置</el-button>
                      </el-form-item>
                    </el-form>
                  </div>
                  <div class="avatar">
                    <div class="preview">
                      <span>头像</span>
                      <img :src="getAvatarUrl(avatar)" />
                    </div>
                    <el-upload
                        action="http://localhost:8080/api/upload/uploadAvatar"
                        :data="additionalParams"
                        :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload"
                    >
                      <el-button type="primary">点击上传</el-button>
                    </el-upload>
                  </div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="安全设置">
                <div class="set-title">
                  <span>安全设置</span>
                </div>
                <div class="secure-item">
                  <div class="secure-info">
                    <span class="secure-key">账户密码</span>
                  </div>
                  <div class="opera-btn" @click="showPasswordDialog = true"><span>修改</span></div>
                </div>
                <div class="secure-item">
                  <div class="secure-info">
                    <span class="secure-key">绑定邮箱</span>
                    <span class="secure-value">已绑定邮箱：{{email}}</span>
                  </div>
                  <div class="opera-btn" @click="showEmailDialog = true"><span>修改</span></div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="新消息通知">
                <div class="set-title">
                  <span>新消息通知</span>
                </div>
                <div class="secure-item">
                  <div class="secure-info">
                    <span class="secure-key">账户密码</span>
                    <span class="secure-value">用户信息将以系统内部渠道通知</span>
                  </div>
                  <el-tooltip :content="'是否开启用户信息: '" placement="top">
                    <el-switch v-model="userSwitch" active-color="#13ce66" inactive-color="#ff4949" :active-value="true" :inactive-value="false"> </el-switch>
                  </el-tooltip>
                </div>
                <div class="secure-item">
                  <div class="secure-info">
                    <span class="secure-key">系统消息</span>
                    <span class="secure-value">系统消息将以系统内部渠道通知</span>
                  </div>
                  <el-tooltip :content="'是否开启系统消息: '" placement="top">
                    <el-switch v-model="sysSwitch" active-color="#13ce66" inactive-color="#ff4949" :active-value="true" :inactive-value="false"> </el-switch>
                  </el-tooltip>
                </div>
                <div class="secure-item">
                  <div class="secure-info">
                    <span class="secure-key">代办任务</span>
                    <span class="secure-value">代办任务将以系统内部渠道通知</span>
                  </div>
                  <el-tooltip :content="'是否开启代办任务消息: '" placement="top">
                    <el-switch v-model="taskSwitch" active-color="#13ce66" inactive-color="#ff4949" :active-value="true" :inactive-value="false"> </el-switch>
                  </el-tooltip>
                </div>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </div>
      </el-col>
    </el-row>
    <el-dialog title="修改绑定邮箱" :visible.sync="showEmailDialog" :model-value="showEmailDialog" width="50%" :before-close="handleClose">
      <personal-email-edit @success="handleSaveEmail"></personal-email-edit>
    </el-dialog>
    <el-dialog title="修改密码" :visible.sync="showPasswordDialog" :model-value="showPasswordDialog" width="50%" :before-close="handleClose">
      <personal-password-edit @success="handleSavePassword"></personal-password-edit>
    </el-dialog>
  </div>
</template>
<script lang="ts">
import { ElMessage } from 'element-plus'
import {computed, defineComponent, onMounted, reactive, ref, toRefs} from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from '@/store'
import Service from './api/index'
import PersonalEmailEdit  from "@/views/Personal/personalEmailEdit.vue";
import PersonalPasswordEdit from "@/views/Personal/personalPasswordEdit.vue";
import LoginService from '../Login/api/index'
// eslint-disable-next-line no-unused-vars
type VoidNoop = (arg0?: Error) => void
export default defineComponent({
  name: 'PersonalSetting',
  components: {PersonalEmailEdit,PersonalPasswordEdit},
  setup() {
    const email = localStorage.getItem('email')
    const router = useRouter()
    const tabPosition = ref('left')
    const settingFormRef = ref()
    const store = useStore()
    const showEmailDialog = ref(false)
    const showPasswordDialog = ref(false)
    var avatar = computed(() => store.state.permissionModule.avatar)
    const noticeSwitch = reactive({
      userSwitch: false,
      sysSwitch: true,
      taskSwitch: true
    })
    const settingForm = reactive({
      username: '',
      intro: '',
      phone: '',
      accessToken: sessionStorage.getItem('accessToken')
    })
    const updateLoading = ref(false)
    const additionalParams = reactive({
      'accessToken': `${settingForm.accessToken}` // 假设 accessToken 存储在 sessionStorage 中
    });
    const getAvatarUrl = (avatar: string) => {
      if(avatar) {
          new URL(avatar);
          return avatar;
      }
      else {
        return '../../assets/avatar-default.jpg';
      }
    }
    // eslint-disable-next-line no-unused-vars
    const validateMobile = (rule: any, value: string, callback: VoidNoop) => {
      if (value === '') {
        callback(new Error('手机号码不可为空哦'))
      } else {
        const reg = /^1[3-9]\d{9}$/
        if (reg.test(value)) {
          callback()
        } else {
          callback(new Error('请输入正确的手机号码'))
        }
      }
    }

    //
    const rules = {
      /*
      email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
      ],
      */
      username: { required: true, message: '请输入昵称', trigger: ['blur', 'change'] },
      intro: { required: true, message: '请输入个人简介', trigger: ['blur', 'change'] },
      phone: { required: true, validator: validateMobile, trigger: ['blur', 'change'] }
    }
    onMounted(() => {})
    // methods

    const handleBack = () => {
      router.go(-1)
    }
    const submitForm = () => {
      settingFormRef.value.validate(async (valid: any) => {
        if (valid) {
          try {
            updateLoading.value = true
            const data = {
              ...settingForm
            }
            const res = await Service.postSetBasicInfo(data)
            console.log(res)
            store.dispatch('permissionModule/getUserInfos', res.data)
            updateLoading.value = false
            ElMessage({
              type: 'success',
              message: res.data.message
            })
          } catch (err) {
            console.error(err)
          }
          // 执行通过校验以后的操作；
          return true
        }
        return false
      })
    }
    const resetForm = () => {
      settingFormRef.value.resetFields()
    }
    const handleAvatarSuccess = async(res: any) => {
      if (res.status === 0) {
        ElMessage('上传头像成功')
        // 更新头像URL到store或其他地方
        console.log(res.data[0])
        store.dispatch('permissionModule/getUserInfos', res.data[0])
      } else {
        ElMessage.error('上传头像失败: ' + res.message)
      }
    }
    const beforeAvatarUpload = (file: { raw: any; type: string; size: number }) => {
      // const isJPG = file.type === 'image/jpeg'
      if (!/(gif|png|jpg|jpeg)$/i.test(file.type)) {
        ElMessage({
          message: '上传头像图片只能是gif|png|jpg|jpeg 格式!',
          type: 'warning'
        })
        return false
      }
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isLt2M) {
        ElMessage.error('上传头像图片大小不能超过 2MB!')
      }
      return isLt2M
    }
    const handleClose = (done: () => void) => {
      showEmailDialog.value = false
      showPasswordDialog.value = false
      done()
    }

    const handleSaveEmail = (data: any) => {
      // 处理保存邮箱逻辑
      ElMessage({
        type: 'success',
        message: '邮箱修改成功'
      })
      showEmailDialog.value = false
    }
    const handleSavePassword = (data: any) => {
      ElMessage({
        type: 'success',
        message: '密码修改成功'
      })
      showPasswordDialog.value = false
    }
    return {
      email,
      avatar,
      additionalParams,
      getAvatarUrl,
      handleBack,
      tabPosition,
      settingFormRef,
      settingForm,
      submitForm,
      resetForm,
      handleAvatarSuccess,
      beforeAvatarUpload,
      showEmailDialog,
      showPasswordDialog,
      handleClose,
      handleSaveEmail,
      handleSavePassword,
      rules,
      ...toRefs(noticeSwitch),
      updateLoading
    }
  }
})
</script>

<style lang="stylus" scoped>
.PersonalSetting{
    margin-top:20px;
    .demo-ruleForm{
        text-align :left;
    }
    .set-title{
      text-align :left;
    }
    .secure-item{
      width:100%;
      padding:20px;
      border-bottom:1px solid #f0f0f0;
      display :flex;
      flex-direction:row;
      justify-content :space-between;
      align-items :center;
      .secure-info{
         display :flex;
      flex-direction:column;
      justify-content :flex-start;
      align-items :flex-start;
        .secure-key{
          margin-bottom: 4px;
          color: rgba(0,0,0,.85);
          font-size: 14px;
          line-height: 1.6;
        }
        .secure-value{
          color: rgba(0,0,0,.45);
          font-size: 14px;
          line-height: 1.6;
        }
      }
      .opera-btn{
        color:#1890ff;
        cursor:pointer;

      }
    }
    .set-info{
      display :flex;
      flex-direction :row;
      justify-content :space-around;
      align-items :flex-start;
      .form-info{


      }
      .avatar{
        display :flex;
        flex-direction:row;
        justify-content:flex-start;
        align-items :flex-end;
        .preview{
           display :flex;
        flex-direction:column;
        justify-content:flex-start;
        align-items :flex-start;
        margin-right:20px;
         img{
            width:174px;
          height:174px;
          border-radius:50%;
         }
        }

          .avatar-uploader .el-upload:hover {
            border-color: #409EFF;
          }
          .avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            line-height: 178px;
            text-align: center;
          }
          .avatar {
            width: 178px;
            height: 178px;
            display: block;
          }
      }
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
}
</style>
