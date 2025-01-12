<template>
  <div class="right-chart-1">
    <div class="rc1-header">
      各部门人数
    </div>

    <div class="rc1-chart-container">
      <div class="left">
        <div class="number">
          262
        </div>
        <div>各部门数</div>
      </div>

      <capsule-chart class="right" :config="state.config" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import {CapsuleChart} from "@kjgl77/datav-vue3";
import {reactive} from "vue";

const state = reactive({
  config: {
    data: [
      {
        name: 'HR',
        value: 25,
      },
      {
        name: 'IT',
        value: 66,
      },
      {
        name: 'Finance',
        value: 123,
      },
      {
        name: 'Markect',
        value: 72,
      },
      {
        name: '其他',
        value: 99,
      },
    ],
    unit: '件',
  },
})
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
