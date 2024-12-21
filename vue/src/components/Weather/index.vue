<template>
  <div class="weather">
    <el-tooltip effect="dark" placement="bottom">
      <template #content>
        <SevenDayChart />
      </template>
      <el-button type="text">
        <el-image style="width: 80px; height: 50px; margin-right: 5px" :src="weatherIconUrl" alt="Weather Icon"></el-image>
      </el-button>
    </el-tooltip>
    <span>{{ weatherDescription }}</span>
    <span>{{ temperature }}°C</span>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import axios from 'axios'; // 确保正确导入 axios
import SevenDayChart from "@/components/Weather/SevenDayChart.vue"; // 引入七日温度折线图组件
import weathericon from '@/assets/images/weathericon.png'; // 确保路径和文件名正确

const city = '成都'; // 使用正确的城市名称

const weatherDescription = ref('');
const temperature = ref(''); // 定义一个新的 ref 变量来存储温度
const weatherIconUrl = ref(weathericon); // 默认图标

const getWeather = async () => {
  const today = new Date().toISOString().split('T')[0];
  try {
    const response = await axios.get(`http://localhost:8080/api/weather?city=${city}&date=${today}`);
    console.log("Request URL:", `http://localhost:8080/api/weather?city=${city}&date=${today}`); // 打印请求URL
    const data = response.data;
    console.log("Weather API response:", data);
    if (data) {
      weatherDescription.value = data.humidity;
      temperature.value = data.temperature;
    }
  } catch (error) {
    console.error("Error fetching weather data:", error);
    temperature.value = 'Error';
  }
};

onMounted(() => {
  getWeather(); // 获取天气数据
});
</script>

<style lang="stylus" scoped>
.weather
  display: flex
  align-items: center
  span
    margin-left: 5px
  img
    margin-right: 5px
</style>
