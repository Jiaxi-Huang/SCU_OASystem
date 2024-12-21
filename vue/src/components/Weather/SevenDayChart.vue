<template>
  <div ref="chart" style="width: 400px; height: 300px;"></div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import * as echarts from 'echarts';

// 定义 ChartData 类型
interface ChartData {
  dates: string[];
  minTemperatures: number[];
  maxTemperatures: number[];
}

const city = '成都';  // 确定要查询的城市

const chart = ref<HTMLElement | null>(null);

const renderChart = (data: ChartData) => {
  if (!chart.value) {
    console.error('Chart element is not available.');
    return;
  }

  console.log('Initializing chart with data:', data);
  const myChart = echarts.init(chart.value as HTMLElement);

  const option = {
    title: {
      text: '七日温度变化',
    },
    tooltip: {
      trigger: 'axis',
    },
    legend: {
      data: ['最低温度', '最高温度'],
    },
    xAxis: {
      type: 'category',
      data: data.dates,
      boundaryGap: false,
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        name: '最低温度',
        type: 'line',
        data: data.minTemperatures,
      },
      {
        name: '最高温度',
        type: 'line',
        data: data.maxTemperatures,
      },
    ],
  };

  myChart.setOption(option);
};

const getPastDates = (days: number): string[] => {
  const dates: string[] = [];
  for (let i = 0; i < days; i++) {
    const date = new Date();
    date.setDate(date.getDate() - i);
    dates.push(date.toISOString().split('T')[0]);
  }
  return dates.reverse(); // 确保日期顺序从过去到现在
};

const getSevenDayWeather = async () => {
  try {
    const pastDates = getPastDates(7);
    const chartData: ChartData = {
      dates: pastDates,
      minTemperatures: [],
      maxTemperatures: []
    };

    for (const date of pastDates) {
      const response = await axios.get(`http://localhost:8080/api/weather?city=${city}&date=${date}`);
      const data = response.data;
      console.log(`Weather data for ${date}:`, data);

      if (data && data.temperature) {
        const [minTemp, maxTemp] = data.temperature.split('~').map((temp: string) => parseInt(temp));
        chartData.minTemperatures.push(minTemp);
        chartData.maxTemperatures.push(maxTemp);
      }
    }

    console.log("Formatted Chart Data:", chartData);
    renderChart(chartData);
  } catch (error) {
    console.error("Error fetching seven-day weather data:", error);
  }
};

onMounted(() => {
  console.log('Component mounted, fetching seven-day weather data...');
  getSevenDayWeather();
});
</script>

<style scoped>
</style>
