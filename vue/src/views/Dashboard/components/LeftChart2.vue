<template>
  <div class="left-chart-2">
    <div class="lc2-header">
      今日考勤情况
    </div>
    <div class="lc2-details">
      考勤人数<span>{{totalCount}}</span>
    </div>
    <charts class="lc2-chart" :option="state.option" />
    <decoration-2 style="height:10px;" />
  </div>
</template>

<script lang="ts" setup>
import {Charts,Decoration2} from "@kjgl77/datav-vue3";
import {onMounted, reactive, ref} from "vue";
import Service from "@/views/Dashboard/api";

const state = reactive({
  option: {
    series: [
      {
        type: 'pie',
        data: [] as { name: string; value: number }[],
        radius: ['45%', '65%'],
        insideLabel: {
          show: false,
        },
        outsideLabel: {
          labelLineEndLength: 10,
          formatter: '{percent}%\n{name}',
          style: {
            fontSize: 14,
            fill: '#fff',
          },
        },
      },
    ],
    color: ['#00baff', '#3de7c9', '#fff', '#ffc530', '#469f4b'],
  },
})
const totalCount = ref(0);

onMounted(async () => {
  try {
    const res = await Service.getAdminAttendanceList();
    if (res[1]) {
      totalCount.value = res[1];
      state.option.series[0].data = res[0];
    }else{
      totalCount.value = 0;
      state.option.series[0].data = [{ name: "今日无人打卡", value: 0 }];
    }

  } catch (error) {
    console.error("获取考勤数据失败:", error);
  }
});
</script>

<style lang="less">
.left-chart-2 {
  width: 100%;
  height: 30%;
  display: flex;
  flex-direction: column;

  .lc2-header {
    height: 20px;
    line-height: 20px;
    font-size: 16px;
    text-indent: 20px;
    margin-top: 10px;
  }

  .lc2-details {
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

  .lc2-chart {
    height: calc(~"100% - 80px");
  }
}
</style>
