<template>
  <div id="data-view">
      <div class="main-header">
          <border-box-2>
            OA办公平台驾驶舱
          </border-box-2>
      </div>

      <border-box-1 class="main-container">
        <border-box-3 class="left-chart-container">
          <Left-Chart-1 />
          <Left-Chart-2 />
          <Left-Chart-3 />
        </border-box-3>

        <div class="right-main-container">
          <div class="rmc-top-container">
            <border-box-3 class="rmctc-left-container">
              <Center-Cmp />
            </border-box-3>

            <div class="rmctc-right-container">
              <border-box-3 class="rmctc-chart-1">
                <Right-Chart-1 />
              </border-box-3>

              <border-box-4 class="rmctc-chart-2" :reverse="true">
                <Right-Chart-2 />
              </border-box-4>
            </div>
          </div>

          <border-box-4 class="rmc-bottom-container">
            <Bottom-Charts />
          </border-box-4>
        </div>
      </border-box-1>
  </div>
</template>

<script lang="ts">
import {defineComponent, ref, reactive, onMounted} from 'vue'
import {BorderBox1,BorderBox2,BorderBox3,BorderBox4} from "@kjgl77/datav-vue3";
import LeftChart1 from './components/LeftChart1.vue'
import LeftChart2 from './components/LeftChart2.vue'
import LeftChart3 from './components/LeftChart3.vue'
import CenterCmp from './components/CenterCmp.vue'
import RightChart1 from './components/RightChart1.vue'
import RightChart2 from './components/RightChart2.vue'
import BottomCharts from './components/BottomCharts.vue'
export default defineComponent({
  components: {
    BorderBox1,BorderBox2,BorderBox3,BorderBox4,
    LeftChart1,
    LeftChart2,
    LeftChart3,
    CenterCmp,
    RightChart1,
    RightChart2,
    BottomCharts
  },
  setup() {
    // return
    return {}
  }
})
</script>

<style lang="less">

#data-view {
  background-color: #030409;
  background-image: url('../../assets/pageBg.png');
  color: #fff;
  height: 98vh; /* 设置高度为视口高度 */
  width: 96.5vw; /* 设置宽度为视口宽度 */
  margin: 0; /* 去除默认的边距 */
  padding: 0; /* 去除默认的填充 */
  overflow: hidden; /* 隐藏溢出内容 */
  .main-header {
    height: 80px;
    display: flex;
    justify-content: center;
    align-items: flex-end;
    font-size: 30px;
  }

  .main-container {
    height: calc(~"100% - 80px");

    .border-box-content {
      padding: 20px;
      box-sizing: border-box;
      display: flex;
    }
  }

  .left-chart-container {
    width: 22%;
    padding: 10px;
    box-sizing: border-box;

    .border-box-content {
      flex-direction: column;
    }
  }

  .right-main-container {
    width: 78%;
    padding-left: 5px;
    box-sizing: border-box;
  }

  .rmc-top-container {
    height: 65%;
    display: flex;
  }

  .rmctc-left-container {
    width: 65%;
  }

  .rmctc-right-container {
    width: 35%;
  }

  .rmc-bottom-container {
    height: 35%;
  }

  .rmctc-chart-1, .rmctc-chart-2 {
    height: 50%;
  }
}
</style>
