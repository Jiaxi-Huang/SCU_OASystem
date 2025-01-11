<template>
  <div class="center-cmp">
    <div class="cc-header">
      <decoration-1 style="width:200px;height:50px;" />
      <div>用户总数</div>
      <decoration-1 style="width:200px;height:50px;" />
    </div>

    <div class="cc-details">
      <div>在线用户总数</div>
      <div class="card">
        {{state.config.onlineUsers[0]}}
      </div>
      <div class="card">
        {{state.config.onlineUsers[1]}}
      </div>
      <div class="card">
        {{state.config.onlineUsers[2]}}
      </div>
      <div class="card">
        {{state.config.onlineUsers[3]}}
      </div>
    </div>

    <div class="cc-main-container">
      <div class="ccmc-left">
        <div class="station-info">
          用户总数<span>{{state.config.allUsers}}</span>
        </div>
        <div class="station-info">
          管理员<span>{{state.config.data[0].value}}</span>
        </div>
      </div>

      <active-ring-chart class="ccmc-middle" :config="state.config" />

      <div class="ccmc-right">
        <div class="station-info">
          <span>{{state.config.data[1].value}}</span>部门经理
        </div>
        <div class="station-info">
          <span>{{state.config.data[2].value}}</span>员工
        </div>
      </div>

      <Label-Tag :config="state.labelConfig" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ActiveRingChart,Decoration1} from "@kjgl77/datav-vue3";
import {onMounted, reactive} from "vue";
import LabelTag from "./LabelTag.vue";
import Service from "../api/index";

const state = reactive({
  config: {
    onlineUsers: [null,null,null,null],
    allUsers: 0,
    data: [
      {
        name: '管理员',
        value: 415,
      },
      {
        name: '部门经理',
        value: 90,
      },
      {
        name: '员工',
        value: 317,
      },
    ],
    roleMap : {
      manager: '部门经理',
      admin: '管理员',
      worker: '员工'
    },
    color: ['#00baff', '#3de7c9', '#ffc530', '#469f4b'],
    lineWidth: 30,
    radius: '55%',
    activeRadius: '60%',
  },
  labelConfig: {
    data: ['管理员', '部门经理', '员工'],
  },

})

const processUserData = (users: any[]) => {
  state.config.data=users.map(item => ({
    name: state.config.roleMap[item.name] || item.name, // 如果没有找到匹配，则保留原名
    value: item.value
  }));
  // 计算所有 value 的总和
  state.config.allUsers = users.reduce((sum, item) => sum + item.value, 0);
}
onMounted(async () => {
  try {
    const data ={accessToken: sessionStorage.getItem('accessToken')}
    const res = await Service.getAdminUserStatistic(data);
    if (res) {
      let onlineUsersArray = res.onlineUsers.toString().split('').map(Number);
      // 如果数组长度小于指定长度，则在前面填充0
      while (onlineUsersArray.length < 4) {
        onlineUsersArray.unshift(0);
      }
      state.config.onlineUsers = onlineUsersArray;
      processUserData(res.data);
    }
  } catch(error) {
      console.error("获取用户数据失败:", error);
    }
});
</script>

<style lang="less">
.center-cmp {
  width: 100%;
  height: 100%;
  margin: 0px;
  padding: 0px;
  display: flex;
  flex-direction: column;

  .cc-header {
    height: 70px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 30px;
  }

  .cc-details {
    height: 120px;
    display: flex;
    justify-content: center;
    font-size: 20px;
    align-items: center;

    .card {
      background-color: rgba(4,49,128,.6);
      color: #08e5ff;
      height: 70px;
      width: 70px;
      font-size: 45px;
      font-weight: bold;
      line-height: 70px;
      text-align: center;
      margin: 10px;
    }
  }

  .cc-main-container {
    position: relative;
    flex: 1;
    display: flex;

    .ccmc-middle {
      width: 50%;
      height: 90%;

      .active-ring-name {
        font-size: 20px !important;
      }
    }

    .ccmc-left, .ccmc-right {
      width: 25%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      font-size: 15px;

      span {
        font-size: 40px;
        font-weight: bold;
      }

      .station-info {
        height: 80px;
        display: flex;
        align-items: center;
      }
    }

    .ccmc-left {
      align-items: flex-end;

      span {
        margin-left: 20px;
      }
    }

    .ccmc-right {
      align-items: flex-start;

      span {
        margin-right: 20px;
      }
    }
  }

  .label-tag {
    position: absolute;
    width: 500px;
    height: 30px;
    bottom: 10px;
    left: 50%;
    transform: translateX(-50%);
  }
}
</style>
