<template>
  <div>
    <el-card shadow="hover" class="card">
      <p>
        <span><i class="icon-square red"></i> 总人数</span> <span>{{ totalPeople }}</span>
      </p>
      <!-- 考勤统计 -->
      <div class="status">
        <h3>按考勤统计</h3>
        <div class="e-chart" style="height: 201px; width: 100%">
          <div ref="refRolesChart" style="width: inherit; height: inherit;"></div>
        </div>
        <div class="chart-widget-list">
          <p v-for="(count, role) in rolesCount" :key="role">
            <span><i :class="getRoleIconClass(role)"></i> {{ role }} </span>
            <span>{{ count }}</span>
          </p>
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
  rolesCount: { [key: string]: number }

}
export default defineComponent({
  name: 'RoleStatistic',
  props: {
    data: {
      type: Array,
      default: () => []
    }
  },
  setup(props) {
    // 析构获取 props 属性 basePath
    const refRolesChart = ref<HTMLElement | null>(null)

    const state = reactive<stateTypes>({
      data: props.data || [], // 设置默认值
      rolesCount: {},

    })
    const roleMapping = {

    }
    const roleIconMapping = {
      "管理员": "icon-square red",
      "部门经理": "icon-square green",
      "员工": "icon-square deep-blue"
    }

    // 计算属性：总人数
    const totalPeople = state.data.length
    // 动态获取图标类名的方法
    const getRoleIconClass = (role: string): string => {
      return roleIconMapping[role] || "icon-square";
    }

    // 统计函数
    const doStatistics = () => {
      // 遍历数据进行统计
      state.data.forEach(record => {
        const role = roleMapping[record.status]|| record.status;
        if (role) {
          if (state.rolesCount[role]) {
            state.rolesCount[role]++;
          } else {
            state.rolesCount[role] = 1;
          }
        }
      });

    };

    // 显示图表
    const displayChart = () => {
      // 角色统计图表数据
      const rolesChartData = Object.entries(state.rolesCount).map(([role, count]) => ({
        name: role,
        value: count
      }));

      nextTick(() => {
        if (refRolesChart.value) {
          useInitPieChart(refRolesChart.value, rolesChartData);
        } else {
          console.log("refRolesChart not exist!");
        }
      });
    };
    // 初始化统计与图表显示
    onMounted(() => {
      doStatistics();
      displayChart();
    })
    // 暴露方法给模板或其他地方调用
    return {
      ...toRefs(state),
      getRoleIconClass,
      displayChart,
      doStatistics,
      roleMapping,
      roleIconMapping,
      refRolesChart,
      totalPeople
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
