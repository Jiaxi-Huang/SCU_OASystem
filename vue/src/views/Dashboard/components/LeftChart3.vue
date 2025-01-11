<template>
  <div class="left-chart-3">
    <div class="lc3-header">
      报销统计
    </div>
    <div class="lc3-details">
      本月报销总额<span>{{ currentMonthTotal }}</span>
    </div>
    <capsule-chart class="lc3-chart" :config="state.config" />
  </div>
</template>

<script lang="ts" setup>
import { CapsuleChart } from "@kjgl77/datav-vue3";
import { reactive, onMounted, ref } from "vue";
import Service from "../api/index";

// 定义响应式数据
const state = reactive({
  config: {
    data: [] as { name: string; value: number }[], // 近5个月的报销金额
    colors: ['#00baff', '#3de7c9', '#fff', '#ffc530', '#469f4b'],
    unit: '元',
  },
});

const currentMonthTotal = ref(0); // 本月报销总额

// 获取当前月份和近5个月的月份
const getMonths = () => {
  const months = [];
  const date = new Date();
  for (let i = 0; i < 5; i++) {
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    months.unshift(`${year}-${month.toString().padStart(2, '0')}`);
    date.setMonth(date.getMonth() - 1);
  }
  return months;
};

// 处理报销数据
const processReimbursementData = (records: any[]) => {
  const months = getMonths();
  const monthlyTotals = new Array(5).fill(0); // 近5个月的报销总额
  let currentMonth = new Date().toISOString().slice(0, 7); // 当前月份，格式为 YYYY-MM

  records.forEach(record => {
    const recordMonth = record.submitted_at.slice(0, 7); // 获取报销记录的月份
    if (recordMonth === currentMonth) {
      currentMonthTotal.value += parseFloat(record.amount); // 累加本月报销总额
    }
    const index = months.indexOf(recordMonth);
    if (index !== -1) {
      monthlyTotals[index] += parseFloat(record.amount); // 累加近5个月的报销总额
    }
  });

  // 更新图表数据
  state.config.data = months.map((month, index) => ({
    name: month,
    value: monthlyTotals[index],
  }));
};

// 在组件挂载时获取数据
onMounted(async () => {
  try {
    const res = await Service.getAdminReimbursementList();
    if (res && res.data) {
      processReimbursementData(res.data);
    }
  } catch (error) {
    console.error("获取报销数据失败:", error);
  }
});
</script>

<style lang="less">
.left-chart-3 {
  width: 100%;
  height: 33%;
  display: flex;
  flex-direction: column;

  .lc3-header {
    height: 20px;
    line-height: 20px;
    font-size: 16px;
    text-indent: 20px;
    margin-top: 10px;
  }

  .lc3-details {
    height: 40px;
    font-size: 16px;
    display: flex;
    align-items: center;
    text-indent: 20px;

    span {
      color: #096dd9;
      font-weight: bold;
      font-size: 35px;
      margin-left: 20px;
    }
  }

  .lc3-chart {
    flex: 1;
  }
}
</style>
