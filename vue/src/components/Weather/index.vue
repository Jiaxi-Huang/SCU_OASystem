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
    <span>{{ city }}</span>
    <span>        </span>
    <span>{{ weatherDescription }}</span>
    <span>{{ temperature }}°C</span>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import SevenDayChart from "@/components/Weather/SevenDayChart.vue";
import weathericon from '@/assets/images/weathericon.png';

const city = ref('');
const weatherDescription = ref('');
const temperature = ref('');
const weatherIconUrl = ref(weathericon);

const getWeather = async (cityName: string) => {
  const today = new Date().toISOString().split('T')[0];
  try {
    const response = await axios.get(`http://localhost:8080/api/weather?city=${cityName}&date=${today}`);
    console.log("Request URL:", `http://localhost:8080/api/weather?city=${cityName}&date=${today}`);
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

const getCityAndWeather = async () => {
  try {
    const response = await axios.get("https://restapi.amap.com/v3/ip", {
      params: {
        key: "112f7278845a2b4a727d04cffeb63b0b",
      },
    });
    const data = response.data;
    console.log(data);
    // 去掉城市名称中的“市”字
    city.value = data.city.replace('市', '');
    getWeather(city.value); // 获取城市后调用 getWeather 函数
  } catch (error) {
    console.error("Error fetching city data:", error);
  }
};

onMounted(() => {
  getCityAndWeather(); // 获取城市和天气数据
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
