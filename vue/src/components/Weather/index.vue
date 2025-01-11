<template>
  <div class="weather">
    <el-tooltip effect="dark" placement="bottom">
      <template #content>
        <SevenDayChart />
      </template>
      <el-button type="text">
        <el-image style="width: 50px; height: 50px; margin-right: 5px" :src="weatherIconUrl" alt="Weather Icon" />
      </el-button>
    </el-tooltip>
    <span>{{ city }}</span>
    <span>{{ weatherDescription }}</span>
    <span>{{ temperature }}°C</span>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import SevenDayChart from "@/components/Weather/SevenDayChart.vue";

// 导入所有天气图标
import qingIcon from '@/assets/images/qing.png';
import xueIcon from '@/assets/images/xue.png';
import yuIcon from '@/assets/images/yu.png';
import duoyunIcon from '@/assets/images/duoyun.png';
import yinIcon from '@/assets/images/yin.png';
import wuIcon from '@/assets/images/wu.png'; // 新增的雾图标

const city = ref('');
const weatherDescription = ref('');
const temperature = ref('');
const weatherIconUrl = ref('');

// 根据天气情况返回对应的图标
const getWeatherIcon = (weather: string) => {
  switch (weather) {
    case '晴':
      return qingIcon;
    case '雪':
      return xueIcon;
    case '雨':
      return yuIcon;
    case '多云':
      return duoyunIcon;
    case '阴':
      return yinIcon;
    case '雾': // 新增对“雾”天气的判断
      return wuIcon;
    default:
      return qingIcon; // 默认返回晴天的图标
  }
};
const getLocalDate = () => {
  const date = new Date();
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从 0 开始，需要加 1
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

const getWeather = async (cityName: string) => {
  const today = getLocalDate(); // 使用本地时区的日期
  try {
    const response = await axios.get(`http://localhost:8080/api/weather?city=${cityName}&date=${today}`);
    console.log("Request URL:", `http://localhost:8080/api/weather?city=${cityName}&date=${today}`);
    const data = response.data;
    console.log("Weather API response:", data);
    if (data) {
      weatherDescription.value = data.humidity;
      temperature.value = data.temperature;
      // 根据天气情况设置图标
      weatherIconUrl.value = getWeatherIcon(data.humidity);
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

    // 检查 data.city 是否为字符串
    if (typeof data.city === 'string') {
      city.value = data.city.replace('市', '');
      getWeather(city.value); // 获取城市后调用 getWeather 函数
    } else {
      console.error('City data is not a string:', data.city);
    }
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
