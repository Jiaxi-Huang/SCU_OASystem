<template>
  <div class="left-chart-1">
    <div class="lc1-header">
      文件统计
    </div>
    <div class="lc1-details">
      服务器文件总数<span>{{ totalCount }}</span>
    </div>
    <capsule-chart class="lc1-chart" :config="state.config" />
    <decoration-2 style="height:10px;" />
  </div>
</template>

<script lang="ts" setup>
import {Decoration2,CapsuleChart} from "@kjgl77/datav-vue3";
import {onMounted, reactive, ref} from "vue";
import Service from "@/views/Dashboard/api";

const state = reactive({
  config: {
    data: [] as { name: string; value: number }[],
    colors: ['#00baff', '#3de7c9', '#fff', '#ffc530', '#469f4b'],
    unit: '个',
  },
})
const totalCount = ref(0);

onMounted(async () => {
  try {
    const res = await Service.getAdminFileList();
    if (res) {
      totalCount.value = res[1];
      state.config.data = res[0];
    }
  } catch (error) {
    console.error("获取文件数据失败:", error);
  }
});
</script>

<style lang="less">
.left-chart-1 {
  width: 100%;
  height: 37%;
  display: flex;
  flex-grow: 0;
  flex-direction: column;

  .lc1-header {
    text-align: center;
    height: 40px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 30px;
    margin-bottom: 20px;
  }

  .lc1-details {
    height: 50px;
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

  .lc1-chart {
    flex: 1;
  }
}
</style>
