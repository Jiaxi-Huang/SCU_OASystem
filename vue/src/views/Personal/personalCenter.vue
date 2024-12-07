<template>
  <meta name="referrer" content="no-referrer">
  <div class="page-container">
    <div class="info">
      <el-divider content-position="left">个人中心</el-divider>
      <span style="text-align: left"
        >个人中心通常用来作为个人基本信息展示和个人事务通知中心
        <el-link type="primary" href="https://fullcalendar.io/docs/initialize-globals-demo"
          >fullCalendar Demo <el-tooltip class="item" effect="dark" placement="top-start"> <i class="el-icon-question"></i></el-tooltip> </el-link
      ></span>
    </div>
    <el-row :gutter="20">
      <el-col :span="7" :offset="1">
        <el-card class="box-card">
          <div class="account-avatar">
            <img :src="getAvatarUrl(avatar)" alt="个人头像" />
          </div>
          <div class="account-detail">
            <el-descriptions class="detail" :column="1" :size="size">
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-user"></i>
                  用户名:
                </template>
                {{username}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-user"></i>
                  角色:
                </template>
                {{ role }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-chat-dot-round"></i>
                  部门:
                </template>
                {{department}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-chat-dot-round"></i>
                  手机号:
                </template>
                {{phone}}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <i class="el-icon-chat-dot-round"></i>
                  自我介绍:
                </template>
                {{intro}}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </el-col>

      <el-col :span="15" :push="0">
        <el-card class="box-card">
          <fullcalendar></fullcalendar>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, ref, onMounted, computed } from 'vue'
import { useStore } from '@/store'
import Service from './api/index'
import fullcalendar from './components/fullCalendar/index.vue'
import defaultAvatar from '../../assets/avatar-default.jpg';
interface stateType {
  inputVisible: boolean
  inputValue: string
  contentTip: string
}
export default defineComponent({
  name: 'PersonalCenter',
  components: {
    fullcalendar
  },
  setup() {
    const formLabelWidth = ref(100)
    const size = ref('medium')
    const showDesc = ref(true)
    const store = useStore()
    let username = computed(() => store.state.permissionModule.username)
    let role = computed(() => store.state.permissionModule.role)
    let department = computed(() => store.state.permissionModule.department)
    let phone = computed(() => store.state.permissionModule.phone)
    let intro = computed(() => store.state.permissionModule.intro)
    let avatar = computed(() => store.state.permissionModule.avatar)
    // mothods
    /**
     * @description 获取头像
     */
    const getAvatarUrl = (avatar: string) => {
      if (avatar) {
        // 简单的 URL 验证
        new URL(avatar)
        return avatar
        }
      else{
        return defaultAvatar;
      }
    }
    onMounted(() => {})

    return {
      formLabelWidth,
      getAvatarUrl,
      size,
      username,
      role,
      department,
      phone,
      intro,
      avatar,
      showDesc
    }
  }
})
</script>
<style lang="stylus" scoped>
.page-container{
      .info{
        text-align: left;
    padding-left: 20px;
    margin-bottom: 20px;
    font-size: 12px;
    }
}
.box-card{
  p{
    text-align :right;
  }
    .is-selected {
    color: #1989FA;
  }
  ul{
    margin-top: 4%;
    margin-left: 15%;
    width: 80%;
    text-align :left;
    position: relative;
    display: inline-block;
    background-color: white;
    li{
      text-align :left;
    }
  }
  margin-top:14px;
  .account-avatar{
    text-align: center;
    margin-bottom :24px;
    img{
      width:105px;
      height:105px;
      margin-bottom :20px;
      border-radius:50%;
    }
    .account-name{
      margin-bottom: 4px;
      color: rgba(0,0,0,.85);
      font-weight: 500;
      font-size: 20px;
      line-height: 28px;
    }

  }
  .account-detail{
    .detail{
      display :flex;
      flex-direction:column;
      justify-content :flex-start;
      align-items :flex-start;
    }
  }
  .divider{
    display: flex;
    clear: both;
    width: 100%;
    min-width: 100%;
    margin: 24px 0;
  }
  .divider-dashed{
    background: 0 0;
    border: dashed rgba(0,0,0,.06);
    border-width: 1px 0 0;
  }
}
</style>
