<template>
  <div class="weather">
    <el-tooltip effect="dark" placement="bottom">
      <template #content>
        <SevenDayChart />
      </template>

    </el-tooltip>
    <div class="info">
      <span>当前位置：{{ city }}</span>
      <div ref="chart" style="width: 400px; height: 300px;"></div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import SevenDayChart from "@/components/Weather/SevenDayChart.vue";
import weathericon from '@/assets/images/weathericon.png';
import * as echarts from 'echarts';

// 定义 ChartData 类型
interface ChartData {
  dates: string[];
  minTemperatures: number[];
  maxTemperatures: number[];
}

const city = ref('');
const weatherDescription = ref('');
const temperature = ref('');
const weatherIconUrl = ref(weathericon);
const chart = ref<HTMLElement | null>(null);

const getWeather = async (cityName: string) => {
  const today = new Date().toISOString().split('T')[0];
  try {
    const response = await axios.get(`http://localhost:8080/api/weather?city=${cityName}&date=${today}`);
    // console.log("Request URL:", `http://localhost:8080/api/weather?city=${cityName}&date=${today}`);
    const data = response.data;
    // console.log("Weather API response:", data);
    if (data) {
      weatherDescription.value = data.humidity;
      temperature.value = data.temperature;
    }
  } catch (error) {
    // console.error("Error fetching weather data:", error);
    temperature.value = 'Error';
  }
};

const renderChart = (data: ChartData) => {
  if (!chart.value) {
    // console.error('Chart element is not available.');
    return;
  }

  // console.log('Initializing chart with data:', data);
  const myChart = echarts.init(chart.value as HTMLElement);

  const option = {
    backgroundColor: '#FFFFFF',
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
        showSymbol: false,
      },
      {
        name: '最高温度',
        type: 'line',
        data: data.maxTemperatures,
        showSymbol: false,
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
  return dates.reverse();
};

const getSevenDayWeather = async (cityName: string) => {
  try {
    const pastDates = getPastDates(7);
    const chartData: ChartData = {
      dates: pastDates,
      minTemperatures: [],
      maxTemperatures: []
    };

    for (const date of pastDates) {
      const response = await axios.get(`http://localhost:8080/api/weather?city=${cityName}&date=${date}`);
      const data = response.data;
      // console.log(`Weather data for ${date}:`, data);

      if (data && data.temperature) {
        const [minTemp, maxTemp] = data.temperature.split('~').map((temp: string) => parseInt(temp));
        chartData.minTemperatures.push(minTemp);
        chartData.maxTemperatures.push(maxTemp);
      }
    }

    // console.log("Formatted Chart Data:", chartData);
    renderChart(chartData);
  } catch (error) {
    // console.error("Error fetching seven-day weather data:", error);
  }
};

const getCityAndWeather = async () => {
  try {
    const response = await axios.get("https://restapi.amap.com/v3/ip", {
      params: {
        key: "112f7278845a2b4a727d04cffeb63b0b",
      },
    });
    const data = response.data;
    // console.log(data);
    city.value = data.city.replace('市', '');
    getWeather(city.value);
    getSevenDayWeather(city.value); // 获取城市后调用 getSevenDayWeather 函数
  } catch (error) {
    // console.error("Error fetching city data:", error);
  }
};

onMounted(() => {
  getCityAndWeather();
});
</script>

<style lang="stylus" scoped>
.weather
  display: flex
  flex-direction: column
  align-items: flex-start
  span
    margin-top: 10px
  img
    margin-right: 5px
.info
  margin-top: 20px
  span
    display: block
    margin-bottom: 10px
</style>
