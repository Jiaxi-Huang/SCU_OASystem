<template>
  <div>
    <el-card shadow="hover" class="card">
      <p>
        <span><i class="icon-square red"></i> 总数</span> <span>{{ totalLogsCount }}</span>
      </p>
      <!-- ID统计 -->
      <div class="id">
        <h3>按编号统计</h3>
        <div class="e-chart" style="height: 201px; width: 100%">
          <div ref="refLogsIdChart" style="width: inherit; height: inherit;"></div>
        </div>
      </div>
      <!-- 内容统计 -->
      <div class="content">
        <h3>按内容统计</h3>
        <div class="e-chart" style="height: 401px; width: 100%">
          <div ref="refLogsContentChart" style="width: inherit; height: inherit;"></div>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script lang="ts">
import {computed, defineComponent, nextTick, onMounted, reactive, ref, toRef, toRefs} from 'vue'
import {useInitPieChart} from "./useInitPieCharts"
interface stateTypes {
  data: any[]
}
export default defineComponent({
  name: 'LogStatistic',
  props: {
    data: {
      type: Array,
      default: () => []
    }
  },
  setup(props) {
    // 析构获取 props 属性 basePath
    const refLogsIdChart = ref<HTMLElement | null>(null)
    const refLogsContentChart = ref<HTMLElement | null>(null)
    const state = reactive<stateTypes>({
      data: props.data || [], // 设置默认值
    })
    // 计算属性：总人数
    const totalLogsCount = computed(() => state.data.length)
    const idsCount = computed(() => {
      const countMap: { [key: number]: number } = {};
      state.data.forEach(log => {
        countMap[log.userId] = (countMap[log.userId] || 0) + 1;
      });
      return countMap;
    })

    const contentsCount = computed(() => {
      const countMap: { [key: string]: number } = {};
      state.data.forEach(log => {
        countMap[log.logContent] = (countMap[log.logContent] || 0) + 1;
      });
      return countMap;
    })

    // 显示图表
    const displayChart = () => {
      nextTick(() => {
        if (refLogsIdChart.value) {
          useInitPieChart(refLogsIdChart.value, Object.entries(idsCount.value).map(([id, count]) => ({
            name: id,
            value: count
          })));
        } else {
          console.log("refLogsIdChart not exist!");
        }
        if (refLogsContentChart.value) {
          useInitPieChart(refLogsContentChart.value, Object.entries(contentsCount.value).map(([content, count]) => ({
            name: content,
            value: count
          })));
        } else {
          console.log("refLogsContentChart not exist!");
        }
      })
    }
    // 初始化统计与图表显示
    onMounted(() => {
      displayChart();
    })
    // 暴露方法给模板或其他地方调用
    return {
      ...toRefs(state),
      displayChart,
      idsCount,
      contentsCount,
      refLogsIdChart,
      refLogsContentChart,
      totalLogsCount
    }
  }
})
</script>
<style lang="stylus" scoped>
.chart-widget-list p{
  border-bottom 1px solid #f1f3fa
  margin-bottom 0.5rem
  padding-bottom 0.5rem
  display flex
  flex-direction row
  justify-content space-between
  align-items center
}
.icon-square{
  width 12px
  height 12px
  display inline-block
}
.red{
  background-color #ec6769
}
.green{
  background-color  #93cb79
}
.yellow {
  background-color #f9c761
}
.deep-blue{
  background-color #5572c3
}
</style>
