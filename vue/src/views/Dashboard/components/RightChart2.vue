<template>
  <div class="right-chart-2">
    <div class="rc1-header">
      员工待办事项
    </div>

    <div class="rc1-chart-container">
      <div class="left">
        <div class="number">
          {{ state.total }}
        </div>
        <div>员工待办事项总数</div>
      </div>

      <charts class="right" :option="state.option" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import {Charts} from "@kjgl77/datav-vue3";
import {onMounted, reactive} from "vue";
import Service from '../api/index'
const state = reactive({
  option: {
    series: [
      {
        type: 'pie',
        data: [],
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
    color: ['#00baff', '#3de7c9', '#469f4b', '#ffc530', ],
  },
})

onMounted(async () => {
  const statistics = await Service.getTodoStatistics();
  if (statistics) {
    state.total = statistics[1];
    state.option.series[0].data = statistics[0];
  }
});
</script>

<style lang="less">
.right-chart-2 {
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
    width: 20%;
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
