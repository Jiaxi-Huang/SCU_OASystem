<template>
  <div class="navbar">
    <el-header height="50px">
      <hamburger id="Hamburger" :is-active="opened" class="hamburger-container" @toggleClick="toggleSideBar" />
      <breadcrumb class="breadcrumb-container" />
      <div class="right-menu">
        <search></search>
        <lang-switch></lang-switch>
        <div id="Message" class="right-menu-box">
          <el-tooltip placement="bottom" effect="light">
            <template #content>
              <div>
                <div class="el-dropdown-menu__item" @click="navigateTo('/todoList/todoTableList')">
                  您有 <strong>{{ pendingTodos.length }}</strong> 个待办事项待处理
                </div>
                <div class="el-dropdown-menu__item" @click="navigateTo('/leaveApproval/leaveList')">
                  您有 <strong>{{ pendingLeaveApprovals.length }}</strong> 条请假审批待处理
                </div>
                <div class="el-dropdown-menu__item" @click="navigateTo('/Reimbursement/reimbursementList')">
                  您有 <strong>{{ pendingReimbursement.length }}</strong> 笔报销待处理
                </div>
                <div class="el-dropdown-menu__item" @click="navigateTo('/meetings/meetingsList')">
                  您有 <strong>{{ pendingMeetings.length }}</strong> 个会议待处理
                </div>
              </div>
            </template>
            <el-badge
                :value="pendingTodos.length + pendingLeaveApprovals.length + pendingReimbursement.length + pendingMeetings.length"
                :max="99"
                class="message-badge"
                type="danger">
              <el-button class="message">
                <el-icon><Message /></el-icon>
              </el-button>
            </el-badge>
          </el-tooltip>
        </div>
        <div id="fullScreen" class="right-menu-box">
          <el-button class="full-screen">
            <el-tooltip :content="langConfig.header.fullScreen[lang]" effect="dark" placement="left">
              <el-icon v-show="fullScreen == false" @click="toShowFullScreen()"><full-screen /></el-icon>
            </el-tooltip>
            <el-tooltip :content="langConfig.header.exitFullScreen[lang]" effect="dark" placement="left">
              <el-icon v-show="fullScreen == true" @click="toExitFullScreen()"><bottom-left /></el-icon>
            </el-tooltip>
          </el-button>
        </div>
        <el-dropdown class="avatar-container" trigger="hover">
          <div class="avatar-wrapper">
            <el-avatar :src="getAvatarUrl(avatar)" referrerPolicy="no-referrer"></el-avatar>
            <div class="nickname">{{ nickname }}</div>
          </div>
          <template #dropdown>
            <el-dropdown-menu class="user-dropdown">
              <router-link to="/">
                <el-dropdown-item>{{ langConfig.header.user.homePage[lang] }}</el-dropdown-item>
              </router-link>
              <router-link to="/personal/personalCenter">
                <el-dropdown-item>{{ langConfig.header.user.personalCenter[lang] }}</el-dropdown-item>
              </router-link>
              <router-link to="/personal/personalSetting">
                <el-dropdown-item>{{ langConfig.header.user.personalSetting[lang] }}</el-dropdown-item>
              </router-link>
              <el-dropdown-item divided>
                <span style="display: block" @click="logout">{{ langConfig.header.user.logout[lang] }}</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
  </div>
</template>


<script lang="ts">
import { defineComponent, computed, ref, onMounted, onBeforeUnmount } from 'vue'
import { ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { useStore } from '@/store/index'
import TodoService from '@/views/TodoList/api/index'
import LeaveService from '@/views/LeaveApproval/api/index'
import ReimbursementService from '@/views/Reimbursement/api/index'
import MeetingService from '@/views/Meetings/api/index'  // 导入会议管理的 Service
import { Todo } from '@/types/todo'
import { LeaveApproval } from '@/types/leaveApproval'
import { Reimbursement } from '@/types/reimbursement'
import { Meeting } from '@/types/meeting'  // 导入会议类型
import Hamburger from '@/components/Hamburger/Hamburger.vue'
import Breadcrumb from '@/components/Breadcrumb/index.vue'
import Search from '@/components/Search/index.vue'
import LangSwitch from '@/components/LangSwitch/index.vue'
import { toFullScreen, exitFullScreen } from '@/utils/screen'
import { Message, FullScreen, BottomLeft } from '@element-plus/icons-vue'
import { langConfig } from '@/utils/constant/config'

export default defineComponent({
  name: 'Navbar',
  components: {
    Hamburger,
    Breadcrumb,
    Search,
    LangSwitch,
    Message,
    FullScreen,
    BottomLeft
  },
  setup() {
    const router = useRouter()
    const store = useStore()
    const opened = computed(() => store.getters['appModule/getSidebarState'])
    const fullScreen = ref(false)
    const messageNum = computed(() => store.getters['messageModule/getMessageNum'])
    const lang = computed((): string => store.getters['settingsModule/getLangState'])
    const nickname = computed(() => store.state.permissionModule.username)
    const avatar = computed(() => store.state.permissionModule.avatar)

    const todos = ref<Todo[]>([])
    const leaveApprovals = ref<LeaveApproval[]>([])
    const reimbursements = ref<Reimbursement[]>([])
    const meetings = ref<Meeting[]>([])  // 定义 meetings 变量

    const getAvatarUrl = (avatar: string) => {
      if (typeof avatar === 'string' && avatar.trim().length > 0) {
        try {
          new URL(avatar)
          return avatar
        } catch (e) {
          console.error('Invalid avatar URL:', e)
        }
      }
      return '../../assets/avatar-default.jpg'
    }

    const getTodos = async () => {
      const response = await TodoService.postGetTodoList()
      if (response && response.data) {
        todos.value = response.data
      }
    }

    const getLeaveApprovals = async () => {
      const response = await LeaveService.postGetLeaveApproval()
      if (response && response.data) {
        leaveApprovals.value = response.data
      }
    }

    const getReimbursements = async () => {
      const role = localStorage.getItem("role")
      if (role === "admin") {
        const response = await ReimbursementService.getAdminReimbursementList()
        if (response && response.data) {
          reimbursements.value = response.data
        }
      }else{
        const response = await ReimbursementService.getReimbursementList()
        if (response && response.data) {
          reimbursements.value = response.data
        }
      }
    }

    const getMeetings = async () => {
      const response = await MeetingService.getPersonalMeetingList()
      if (response && response.data) {
        meetings.value = response.data
      }
    }


    const checkDueTodos = (todos: Todo[]) => {
      const now = new Date().getTime()
      todos.forEach((todo: Todo) => {
        const dueTime = new Date(todo.todo_ddl).getTime()
        if (dueTime - now <= 3600000 && todo.todo_fin !== '已完成') {
          ElMessageBox.alert(`待办事项 "${todo.todo_title}" 即将临期`, '提醒', {
            confirmButtonText: '确定',
            type: 'warning',
          })
        }
      })
    }

    onMounted(() => {
      getTodos()
      getLeaveApprovals()
      getReimbursements()
      getMeetings()  // 获取会议数据

      const intervalId = setInterval(() => {
        console.log("Checking todos at interval...")  // 打印定时器调用信息
        getTodos().then(() => {
          checkDueTodos(todos.value)
        })
      }, 600000)

      onBeforeUnmount(() => {
        clearInterval(intervalId)
      })
    })

    const toggleSideBar = () => {
      store.dispatch('appModule/toggleSideBar')
    }

    const toShowFullScreen = () => {
      toFullScreen()
      fullScreen.value = true
    }

    const toExitFullScreen = () => {
      exitFullScreen()
      fullScreen.value = false
    }

    const navigateTo = (path: string, query?: Record<string, string>) => {
      if (path.includes('/todoList')) {
        sessionStorage.setItem('showUncompleted', '1')
      } else if (path.includes('/leaveApproval')) {
        sessionStorage.setItem('showPendingLeave', '1')
      } else if (path.includes('/Reimbursement')) {
        sessionStorage.setItem('showPendingReimbursement', '1')
      } else if (path.includes('/meetings')) {
        sessionStorage.setItem('showPendingMeetings', '1')
      }
      router.push({ path, query })
    }


    const logout = () => {
      sessionStorage.removeItem('auth')
      sessionStorage.removeItem('accessToken')
      router.replace('/login')
    }

    return {
      getAvatarUrl,
      messageNum,
      toShowFullScreen,
      toExitFullScreen,
      toFullScreen,
      exitFullScreen,
      fullScreen,
      nickname,
      lang,
      avatar,
      toggleSideBar,
      opened,
      langConfig,
      logout,
      pendingTodos: computed(() => todos.value.filter(todo => todo.todo_fin === '未完成')),
      pendingLeaveApprovals: computed(() => leaveApprovals.value.filter(leave => leave.status === '待审批')),
      pendingReimbursement: computed(() => reimbursements.value.filter(reimbursement => reimbursement.status == '未审核' || reimbursement.status == '未通过')),
      pendingMeetings: computed(() => meetings.value.filter(meeting => meeting.mtin_fin === '未完成')),
      navigateTo
    }
  }
})
</script>

<style lang="scss" scoped>
:root {
  --el-box-shadow-light: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  --el-color-primary-light-9: #ecf5ff;
  --el-color-primary: #409eff;
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.28);
  z-index: 1;

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .nickname {
    float: right;
    padding: 0px 25px 0px 25px;
    line-height: 40px;
    outline: none;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;
    display: flex;
    &:focus {
      outline: none;
    }
    .right-menu-box {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }

    .message-badge {
      .is-fixed {
        top: 12px !important;
        right: 28px !important;
      }
      .message {
        border: none;
        padding: 5px 20px;

        i {
          background-color: transparent;
          border: none;
          color: #2c3e50;
          font-size: 25px;
        }
      }
    }

    .full-screen {
      background-color: transparent;
      border: none;
      padding: 5px 20px;

      i {
        background-color: transparent;
        border: none;
        color: #2c3e50;
        font-size: 28px;
      }
    }

    .el-dropdown-menu__item {
      display: flex;
      align-items: center;
      white-space: nowrap;
      list-style: none;
      line-height: 22px;
      padding: 5px 16px;
      margin: 0;
      font-size: var(--el-font-size-base, 14px);
      color: var(--el-text-color-regular, #606266);
      cursor: pointer;
      outline: 0;
      transition: background-color 0.3s, color 0.3s;

      &:hover {
        background-color: var(--el-dropdown-menuItem-hover-fill);
        color: var(--el-dropdown-menuItem-hover-color);
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;
        cursor: pointer;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
