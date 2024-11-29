<style lang="stylus" scoped>
.dragable-container{
  color #6c757d
  padding 0px 20px
  background-color #fafbfe

  .page-title-box{
  box-sizing border-box
  display flex
  flex-direction row
  justify-content space-between
  align-items center
  .page-title{
    font-size  18px
    margin  0
    line-height  85px
    overflow  hidden
    white-space: nowrap
    text-overflow  ellipsis
    color  inherit
    font-weight 700
  }
  .page-title-right{
    display flex
    flex-direction row
    justify-content space-around
    align-items center
  }
}
  .flex{
    display flex
  }
  .flex-row{
    flex-direction row
  }
  .flex-column{
    flex-direction column
  }
  .flex-end{
    justify-content flex-end
  }
  .flex-start{
    justify-content flex-start
  }
  .flex-between{
    justify-content space-between
  }
  .text-muted{
    color #98a6ad
  }
  .align-center{
    align-items center
  }
  .text-norap{
    white-space nowrap
  }
  .margin-top-1{
    margin-top 10px
  }
  .margin-bottom-1{
    margin-bottom 10px
  }
  .margin-top-2{
    margin-top 20px
  }
  .margin-bottom-2{
    margin-bottom 20px
  }
  .margin-right-1{
    margin-right 10px
  }
  .padding-right-1{
    padding-right 10px
  }

  .badge{
    display: inline-block;
    padding: 0.25em 0.4em;
    font-size: 130%;
    font-weight: 700;
    line-height: 1;
    text-align: center;
    white-space: nowrap;
    vertical-align: baseline;
    border-radius: 0.25rem;
    -webkit-transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,-webkit-box-shadow .15s ease-in-out;
    transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,-webkit-box-shadow .15s ease-in-out;
    transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;
    transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out,-webkit-box-shadow .15s ease-in-out;
  }
  .badge-secondary{
    color #fff
    background-color #6c757d
  }
  .badge-danger{
    color #fff
    background-color #fa5c7c
  }
  .badge-success{
    color: #fff;
    background-color: #0acf97;
  }

  .board{
    display: block;
    white-space: nowrap;
    overflow-x: auto;
    .task{
      display: inline-block;
      width: 22rem;
      padding: 0 1rem 1rem 1rem;
      border: 1px solid #e3eaef;
      vertical-align: top;
      margin-bottom: 30px;
      border-radius: 0.25rem;
      .task-header{
        background-color #f1f3fa
        padding 1rem
        margin 0 -1rem
        font-size 130%
      }
      .task-list-item{
        min-height 150px
        position relative
        .card {
        white-space normal
        margin-top 1rem
      }
      }
      &:not(:last-child) {
        margin-right: 1.25rem;
      }
    }

    .text-upercase{
      text-transform: uppercase!important;
    }
    .margin-top-0{
      margin-top 0 !important
    }
    h5{
      font-weight 700
      color  #6c757d
    }
    .name{
      margin-left 5px
    }
    .pointer{
      cursor pointer
    }

  }
  .info{
    text-align: left;
    padding-left: 20px;
    margin-bottom: 20px;
    font-size: 12px;
  }
  .section{
    display:flex;
    flex-direction:row;
    justify-content: flex-start;
    align-items :center;
  }

  .el-row {
    margin-bottom: 20px;
  }
  .box{
    background-color: var(--color-bg-primary);
    border: 1px solid var(--color-border-primary);
    border-radius: 6px;
    padding: 16px!important;
    display: flex!important;
    .item-list-content{
      display: flex;
      width: 100%;
      flex-direction: column;
    }
  }
  .box-card{
    margin-bottom :10px;
  }

  .external-events{
    cursor move
    margin 10px 0
    padding 8px 10px
    color #fff
    text-align left
    width 258.25px
    overflow hidden
    text-overflow ellipsis
    white-space nowrap

    .list-circle{
    width 14px
    height 14px
    border-radius 50%
    background-color white
    display inline-block
    margin-right: 10px
  }
  }
  :deep(.bg-success){
    background-color: #0acf97!important
  }
  :deep(.bg-info) {
    background-color: #39afd1!important;
  }
  :deep(.bg-danger) {
    background-color: #fa5c7c!important;
  }
  :deep(.bg-dark) {
    background-color: #313a46!important;
  }
  :deep(.bg-warning) {
    background-color: #ffbc00!important;
  }

}
</style>

<template>
  <div class="dragable-container">
    <el-row style="margin: 15px">
      <el-col :span="24" class="page-title-box">
        <h4 class="page-title">会议列表</h4>
      </el-col>
    </el-row>
    <el-row>
      <el-row>
        <el-col><el-button type="success">创建会议</el-button></el-col>
      </el-row>
      <el-col :span="24">
        <div class="board">
          <div class="task">
            <h5 class="task-header text-upercase margin-top-0">已预订会议</h5>
            <div class="task-list-item">
              <template v-if="task.scheduled && task.scheduled.length">
              <el-card v-for="item in task.scheduled" :key="item.mtin_id" shadow="hover" class="card">
                <el-row>
                  <el-col :span="24" style="text-align: left">
                    <div class="flex flex-row flex-between">
                      <span class="badge badge-success">{{ item.mtin_title }}</span>
                      <small class="text-muted">会议ID：  {{ item.mtin_id }} </small>
                    </div>
                    <p class="name margin-top-1 margin-bottom-1">会议时间：{{ item.mtin_st }}</p>
                    <p class="text-muted margin-bottom-1">
                      <span class="text-norap margin-right-1"
                        ><el-icon size="19px"><ChatLineSquare /></el-icon>&nbsp;&nbsp;会议简介： {{ item.mtin_ctnt }}</span
                      >
                    </p>
                    <div class="flex flex-row flex-between">
                      <div class="flex flex-row flex-start align-center">
                        <el-avatar size="small" :src="item.avatar? item.avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'"></el-avatar>
                        <p class="name">主持人ID: {{ item.mtin_host }}</p>
                      </div>
                    </div>
                    <el-row></el-row>
                    <div class="flex flex-row flex-between">
                      <div class="handle"><el-icon class="pointer" @click="buttonClickTest()"><View /></el-icon></div>
                      <div class="handle"><el-icon class="pointer" @click="buttonClickTest()"><Edit /></el-icon></div>
                      <div class="handle"><el-icon class="pointer" @click="buttonClickTest()"><Delete /></el-icon></div>
                    </div>
                  </el-col>
                </el-row>
              </el-card>
              </template>
              <div v-else>
                <el-row></el-row>
                <p>暂无会议安排</p>
              </div>
            </div>
          </div>

          <div class="task">
            <h5 class="task-header text-upercase margin-top-0">今天的会议</h5>
            <div class="task-list-item">
              <template v-if="task.progressing && task.progressing.length">
                <el-card v-for="item in task.progressing" :key="item.id" shadow="hover" class="card">
                  <el-row>
                    <el-col :span="24" style="text-align: left">
                      <div class="flex flex-row flex-between">
                        <span class="badge badge-danger">{{ item.mtin_title }}</span>
                        <small class="text-muted">会议ID：  {{ item.mtin_id }} </small>
                      </div>
                      <p class="name margin-top-1 margin-bottom-1">会议时间：{{ item.mtin_st }}</p>
                      <p class="text-muted margin-bottom-1">
                      <span class="text-norap margin-right-1"
                      ><el-icon size="19px"><ChatLineSquare /></el-icon>&nbsp;&nbsp;会议简介： {{ item.mtin_ctnt }}</span
                      >
                      </p>
                      <div class="flex flex-row flex-between">
                        <div class="flex flex-row flex-start align-center">
                          <el-avatar size="small" :src="item.avatar? item.avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'"></el-avatar>
                          <p class="name">主持人ID: {{ item.mtin_host }}</p>
                        </div>
                      </div>
                      <el-row></el-row>
                      <div class="flex flex-row flex-between">
                        <div class="handle"><el-icon class="pointer" @click="meetingDetails()"><View /></el-icon></div>
                        <div class="handle"><el-icon class="pointer" @click="buttonClickTest()"><Edit /></el-icon></div>
                        <div class="handle"><el-icon class="pointer" @click="buttonClickTest()"><Delete /></el-icon></div>
                      </div>
                    </el-col>
                  </el-row>
                </el-card>
              </template>
              <div v-else>
                <el-row></el-row>
                <p>暂无会议安排</p>
              </div>
            </div>
          </div>

          <div class="task">
            <h5 class="task-header text-upercase margin-top-0">已完成会议</h5>
            <div class="task-list-item">
              <template v-if="task.passed && task.passed.length">
                <el-card v-for="item in task.passed" :key="item.id" shadow="hover" class="card">
                  <el-row>
                    <el-col :span="24" style="text-align: left">
                      <div class="flex flex-row flex-between">
                        <span class="badge badge-secondary">{{ item.mtin_title }}</span>
                        <small class="text-muted">会议ID：  {{ item.mtin_id }} </small>
                      </div>
                      <p class="name margin-top-1 margin-bottom-1">会议时间：{{ item.mtin_st }}</p>
                      <p class="text-muted margin-bottom-1">
                      <span class="text-norap margin-right-1"
                      ><el-icon size="19px"><ChatLineSquare /></el-icon>&nbsp;&nbsp;会议简介： {{ item.mtin_ctnt }}</span
                      >
                      </p>
                      <div class="flex flex-row flex-between">
                        <div class="flex flex-row flex-start align-center">
                          <el-avatar size="small" :src="item.avatar? item.avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'"></el-avatar>
                          <p class="name">主持人ID: {{ item.mtin_host }}</p>
                        </div>
                      </div>
                      <el-row></el-row>
                      <div class="flex flex-row flex-between">
                        <div class="handle"><el-icon class="pointer" @click="showMeetingDetails(item)"><View /></el-icon></div>
                        <div class="handle"><el-icon class="pointer" @click="buttonClickTest()"><Edit /></el-icon></div>
                        <div class="handle"><el-icon class="pointer" @click="buttonClickTest()"><Delete /></el-icon></div>
                      </div>
                    </el-col>
                  </el-row>
                </el-card>
              </template>
              <div v-else>
                <el-row></el-row>
                <p>暂无会议安排</p>
              </div>
            </div>
          </div>

          <el-dialog v-model="task.detailFormVisible" title="会议详情">
            <el-form :model="task.detailForm">
              <el-form-item label="会议标题&nbsp;&nbsp;" :label-width="formLabelWidth">
                {{ task.detailForm.mtin_title }}
              </el-form-item>
              <el-form-item label="会议内容&nbsp;&nbsp;" :label-width="formLabelWidth">
                {{ task.detailForm.mtin_ctnt }}
              </el-form-item>
              <el-form-item label="会议开始时间&nbsp;&nbsp;" :label-width="formLabelWidth">
                {{ task.detailForm.mtin_st }}
              </el-form-item>
              <el-form-item label="会议长度&nbsp;&nbsp;" :label-width="formLabelWidth">
                {{ task.detailForm.mtin_len }}
              </el-form-item>
              <el-form-item label="会议地点&nbsp;&nbsp;" :label-width="formLabelWidth">
                {{ task.detailForm.mtin_loc }}
              </el-form-item>
              <el-form-item label="会议主持人ID&nbsp;&nbsp;" :label-width="formLabelWidth">
                {{ task.detailForm.mtin_host }}
              </el-form-item>
              <el-form-item label="会议ID&nbsp;&nbsp;" :label-width="formLabelWidth">
                {{ task.detailForm.mtin_id }}
              </el-form-item>
              <el-form-item label="会议状态&nbsp;&nbsp;" :label-width="formLabelWidth">
                {{ task.detailForm.mtin_fin }}
              </el-form-item>
              <el-form-item label="会议创建时间&nbsp;&nbsp;" :label-width="formLabelWidth">
                {{ task.detailForm.mtin_crt }}
              </el-form-item>
              <el-form-item label="会议添加者ID&nbsp;&nbsp;" :label-width="formLabelWidth">
                {{ task.detailForm.adder_id }}
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="task.detailFormVisible = false">确 定</el-button>
            </div>
          </el-dialog>

        </div>
      </el-col>
    </el-row>
  </div>
</template>


<script setup lang="ts">
import { onMounted, ref, onUnmounted, reactive } from 'vue'
import {Operation, Suitcase, ChatLineSquare, View, Delete, Edit} from '@element-plus/icons-vue'
import Service from "@/views/Meetings/api/index";
import {ElMessage} from "element-plus";
import { useRouter } from 'vue-router'
import permission from '@/directive/permission'

let eventGuid = 0
/**
 * @description 事件Uid
 */
const createEventId = () => String(eventGuid++)

interface taskType {
  [key: string]: { id: number; priority: number; date: string; title: string; tag: string;
    comments: number; avatar: string; name: string }[]
}

const task = reactive<taskType>({
  scheduled: [
    {
      id: 1,
      priority: 2,
      date: '19jul 2018',
      title: '后台没打开',
      tag: 'web',
      comments: 28,
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      name: 'GeekQiaQia'
    },
  ],
  progressing: [
    {
      id: 1,
      priority: 2,
      date: '19jul 2018',
      title: '后台没打开',
      tag: 'web',
      comments: 22,
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      name: 'GeekQiaQia'
    },
  ],
  passed: [
  ],
  modifyFormVisible: false,
  detailFormVisible: false,
})

onMounted(() => {
  getPersonalMeetingList()

})

// 组件卸载时销毁实例
onUnmounted(() => {
})

const buttonClickTest = () => {
  console.log("CLicked!")
}


//scope.row
const showMeetingDetails = (row:any) => {
  console.log("Detail Shows!")
  task.detailForm = row
  console.log(task.detailForm)
  task.detailFormVisible = true
}

const getPersonalMeetingList = () => {
  console.log("getPersonalMeetingList exc")
  try {
    Service.getPersonalMeetingList().then((res) => {
      if (res) {
        task.scheduled = res.data[0]
        task.progressing = res.data[1]
        task.passed = res.data[2]
      } else {
        console.log('getPersonalMeetingList error!')
      }
    });
  } catch (err) {
    ElMessage({
      type: 'warning',
      message: err.message
    })
  }
}

</script>
