<template>
  <div class="right-chart-1">
    <div class="rc1-header">
      各部门人数
    </div>

    <div class="rc1-chart-container">
      <div class="left">
        <div class="number">
          {{ totalDepartments }}
        </div>
        <div>各部门数</div>
      </div>

      <capsule-chart class="right" :config="state.config" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { CapsuleChart } from '@kjgl77/datav-vue3';
import Service from "../api/index";

// 定义状态
const state = reactive({
  config: {
    data: [] as { name: string; value: number }[], // 动态数据
    unit: '人',
  },
});

// 计算总部门数
const totalDepartments = computed(() => {
  return state.config.data.length;
});

// 获取用户列表并计算各部门人数
const fetchUserList = async () => {
  const accessToken = sessionStorage.getItem('accessToken'); // 从 sessionStorage 获取 accessToken
  if (!accessToken) {
    console.error('未找到 accessToken，请先登录');
    return;
  }

  try {
    const data = { accessToken }; // 构造请求体
    const response = await Service.getUserList(data); // 调用接口获取用户列表

    if (response) {
      const userList = response; // 获取用户列表数据

      // 计算每个部门的人数
      const departmentCounts: Record<string, number> = {};

      userList.forEach((user: any) => {
        const department = user.userDepartment; // 假设 userDepartment 是部门字段
        if (departmentCounts[department]) {
          departmentCounts[department]++;
        } else {
          departmentCounts[department] = 1;
        }
      });

      // 更新图表数据
      state.config.data = Object.keys(departmentCounts).map((key) => ({
        name: key,
        value: departmentCounts[key],
      }));
    } else {
      console.error('获取用户列表失败');
    }
  } catch (error) {
    console.error('请求失败:', error);
  }
};

// 组件加载时调用
onMounted(() => {
  fetchUserList();
});
</script>

<style lang="less">
.right-chart-1 {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;

  .rc1-header {
    font-size: 24px;
    font-weight: bold;
    height: 30px;
    line-height: 30px;
  }

  .rc1-chart-container {
    flex: 1;
    display: flex;
  }

  .left {
    width: 30%;
    font-size: 16px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .number {
      font-size: 34px;
      color: #096dd9;
      font-weight: bold;
      margin-bottom: 30px;
    }
  }

  .right {
    flex: 1;
    padding-bottom: 20px;
    padding-right: 20px;
    box-sizing: border-box;
  }
}
</style>
