<template>
    <el-form ref="settingEmailFormRef" :model="resetEmailForm" :rules="rules" label-position="right" label-width="100px" title="修改绑定邮箱">
      <el-form-item label="原绑定邮箱" prop="oldEmail">
        <el-input v-model="resetEmailForm.oldEmail" type="email" placeholder="旧的邮箱">
        </el-input>
      </el-form-item>
      <el-form-item label="新绑定邮箱" prop="newEmail">
        <el-input v-model="resetEmailForm.newEmail" type="email" placeholder="新的邮箱">
        </el-input>
        <el-button type="primary" :disabled="sendingCode" @click="sendCaptcha">{{ codeText }}</el-button>
      </el-form-item>
      <el-form-item label="验证码" prop="captcha">
        <el-input v-model.number="resetEmailForm.captcha" maxlength="6" autocomplete="off" placeholder="新邮箱验证码">
        </el-input>
      </el-form-item>
      <el-row class="btn-container">
        <el-button size="mini" type="primary" @click="submitResetEmailForm()"> <i class="fa fa-plus"> </i> 修改 </el-button>
      </el-row>
    </el-form>
</template>
<script lang="ts">
import { computed, defineComponent, reactive, ref} from 'vue'
import Service from "@/views/Personal/api";
import { useStore } from '@/store'
import {ElMessage} from "element-plus/es";

export default defineComponent({
  name: 'PersonalEmailEdit',
  emits: ['success'],
  setup(props,{ emit }) {
    const settingEmailFormRef = ref()
    const store = useStore()
    const sendingCode = ref(false)
    const codeText = ref('获取验证码')
    const rules = {
      oldEmail: [{ required: true, type:'email', message: '请输入邮箱', trigger: 'change' }],
      newEmail: [{ required: true, type:'email', message: '请输入邮箱', trigger: 'change' }],
    }
    const resetEmailForm = reactive({
      oldEmail: '',
      newEmail: '',
      captcha: null,
      accessToken: sessionStorage.getItem('accessToken')
    })
    const sendCaptcha = async() => {
      const data = {
        email: resetEmailForm.newEmail
      }
      const res = await Service.postCaptcha(data)
      if (res.status === 0) {
        ElMessage({
          type: 'success',
          message: res.message
        })
        getCodeSuccess()
        return true
      }
      ElMessage({
        type: 'warning',
        message: res.message
      })
      return false
    }
    // 短验已发送状态
    const getCodeSuccess = () => {
      let countDown = 60
      sendingCode.value = true
      const interval = setInterval(() => {
        if (countDown > 0) {
          codeText.value = `已发送(${countDown}s)`
          countDown -= 1
        } else {
          clearInterval(interval)
          sendingCode.value = false
          codeText.value = '获取验证码'
        }
      }, 1000)
    }
    const submitResetEmailForm = async () => {
        const data = {
          ...resetEmailForm
        }
        const res = await Service.postSetEmail(data)
        console.log(res)
        if (res.status === 0) {
          store.dispatch('permissionModule/getUserInfos', res.data)
          ElMessage({
            type: 'success',
            message: res.message
          })
          emit('success')
        }
        else{
          ElMessage({
            type: 'error',
            message: res.message
          })
        }
    }
    return {
      rules,
      sendingCode,
      codeText,
      resetEmailForm,
      sendCaptcha,
      getCodeSuccess,
      submitResetEmailForm
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
