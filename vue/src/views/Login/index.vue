<template>
  <div class="container">
    <div class="login-container">
      <div class="login-left">
        <div class="top">
          <div class="title">
            <span>欢迎使用</span>
          </div>
          <div class="desc">
            <span>SCU_OA办公管理系统！</span>
          </div>
        </div>
        <div class="bottom">
          <img :src="working" />
        </div>
      </div>
      <div class="login-right">
        <div class="login-box">
        <div class="login-options" >
          <el-button type='primary' @click="selectLoginType('account')">账号登录</el-button>
          <el-button type='primary' @click="selectLoginType('qr')">二维码登录</el-button>
        </div>
        <div class="login-content">
          <div v-if="loginType === 'qr'">
            <span>请使用微信扫描二维码登录</span><br>
            <qrcode-vue :value="value" :size="size" level="H" />
            <div id="wechat-login"></div>
          </div>
          <div v-else-if="loginType === 'account'">
            <Login v-if="!showReset" @toResetPwd="handleResetPwd"></Login>
            <ForgetPassword v-else :show-reset="showReset" @toLogin="handleToLogin"></ForgetPassword>
          </div>
        </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import QrcodeVue from 'qrcode.vue'
import { defineComponent, onMounted, ref } from 'vue'
import viteLogo from '@/assets/logo-vite.svg'
import vueLogo from '@/assets/logo.png'
import working from '@/assets/woking.gif'
import Login from './components/loginForm.vue'
import ForgetPassword from './components/forgetPassword.vue'

export default defineComponent({
  name: 'Index',
  components: {
    QrcodeVue,
    Login,
    ForgetPassword
  },
  data() {
    return {
      value: 'http://localhost:8080/api/auth/wechat/login',
      size: 100,
    }
  },
  setup() {

    const loginType = ref('account')
    const showReset = ref(false)
    const selectLoginType= (type: 'account'|'qr') => {
      loginType.value = type
    }
    const handleResetPwd = () => {
      // 展示重置密码框
      showReset.value = true
    }
    const handleToLogin = () => {
      // 展示登录框框
      showReset.value = false
    }
    return {
      loginType,
      selectLoginType,
      showReset,
      handleResetPwd,
      handleToLogin,
      vueLogo,
      working,
      viteLogo
    }
  }
})
</script>

<style lang="stylus" scoped>
.container{

    position: relative;
    background-image:linear-gradient(90deg, #ebebeb, #f5f7f6);
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;

    .login-container{
        width:874px;
        min-width: 874px;
        height: 78%;
        min-height: 600px;
        flex-direction: row;
        display: flex;
        justify-content: space-evenly;
        border-radius: 10px;
        overflow: hidden;
        background-color :white;
        box-shadow:0 0 20px 5px rgba(34,84,142,.26);

        .login-left{
            width:50%;
        padding:47px 54px;
        img{
           width: 100%;
           height: auto;
           margin: 0px 20px;
        }
        .top{
            display:flex;
            flex-direction:column;
            justify-content :flex-start;
            align-items :flex-start;
            margin:40px 0px;
            .title{
                font-size: 32px;
                margin-bottom: 16px;

            }
            .desc{
                font-size: 28px;
                text-align: left;
                color: rgb(166,175,188);
            }
        }

        }
        .login-right{
            width:50%;
            display:flex;
            flex-direction:column ;
            justify-content: center;
            align-items :center;
            padding:30px;

        }
      .login-box { /* 带阴影的盒子样式 */
        width: 100%;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        background-color: white;

        .login-options {
          margin-bottom: 20px; /* 选项与内容之间的间距 */
        }

        .login-content {
          .account-form {
            margin-top: 20px; /* 账号表单与其他内容之间的间距 */
          }
        }
      }
    }

}
</style>
