<style lang="stylus" scoped>

.calendar-info{
  color black
  padding 0px 20px
  background-color #fafbfe

  .page-title-box{
  box-sizing border-box
  display flex
  flex-direction row
  justify-content space-between
  align-items center
  color #6c757d
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
  .text-muted{
    color #98a6ad
  }
  .text-left{
    text-align left
  }

}
:deep(.fc .fc-daygrid-day.fc-day-today){
  background #f3f6f8
}
:deep(.fc .fc-button-primary){
  background-color #409effab
  border-color transparent
}
:deep(.fc .fc-button-primary:not(:disabled).fc-button-active){
  background-color #409eff
  border-color transparent
}
:deep(.fc .fc-daygrid-day-number){
  float right
  height 20px
  width 20px
  text-align center
  line-height 20px
  background-color #f1f3fa
  border-radius 50%
  margin 5px
  font-size 11px
  display: inline-table;
}
:deep(.bg-success){
  background-color #0acf97!important
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
:deep(.external-events){
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
  margin-right 10px
}
}
</style>

<template>
  <div class="calendar-info">
    <el-row style="margin: 15px">
      <el-col :span="24" class="page-title-box">
        <h4 class="page-title">Calendar</h4>
        <div class="page-title-right">
          <div style="margin-right: 50px">
            <div style="text-align: left"><el-checkbox v-model="togglechecked" label="不显示周末" size="large" @change="handleWeekendsToggle"></el-checkbox></div>
          </div>
          <div style="margin-right: 10px"><el-date-picker v-model="pickDate" type="date" placeholder="选择日期"> </el-date-picker></div>
          <el-button :icon="Refresh" circle type="primary" style="maring-left: 10px"></el-button>
          <el-button :icon="Search" circle type="primary"></el-button>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-card>
          <el-row>
            <el-col :span="18">
              <FullCalendar class="demo-app-calendar" :options="calendarOptions">
                <template #eventContent="arg">
                  <b>{{ arg.timeText }}</b>
                  <i>{{ arg.event.title }}</i>
                </template>
              </FullCalendar>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { Refresh, Search, CirclePlus } from '@element-plus/icons-vue'
import '@fullcalendar/core/vdom' // solve problem with Vite
import FullCalendar, {CalendarOptions, EventApi, DateSelectArg, EventClickArg, EventInput} from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin, { Draggable, DropArg } from '@fullcalendar/interaction'
import { ElMessageBox, ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import type { ElForm } from 'element-plus'
import { createEventId } from './event-utils'
import Service from "./event-utils"

type FormInstance = InstanceType<typeof ElForm>
type task = {
  title: string
  classNames: string
  id: string
}
interface ITaskList {
  [index: number]: task
}

let currentEvents: EventApi[]

const pickDate = ref('')
const checked = ref(false)
const togglechecked = ref(false)
const dialogFormVisible = ref(false)
const formLabelWidth = ref(120)
const ruleFormRef = ref<FormInstance>()

const form = reactive({
  title: '',
  className: 'bg-success'
})
const rules = reactive({
  title: [
    {
      required: true,
      message: 'Please input event title',
      trigger: 'blur'
    }
  ],
  className: [
    {
      required: true,
      message: 'Please select category color',
      trigger: 'blur'
    }
  ]
})

const INITIAL_EVENTS = reactive<EventInput[]>([
  // {
  //   id: createEventId(),
  //   title: '全天安排',
  //   start: ,
  // },
  // {
  //   id: createEventId(),
  //   title: '算法专训',
  //   start: `${todayStr}T12:00:00`,
  // },
]);

/**
 * @description 选中某天处理事件
 */
const handleDateSelect = (selectInfo: DateSelectArg) => {
  // eslint-disable-next-line no-alert
  const title = prompt('请输入任务标题')
  const calendarApi = selectInfo.view.calendar

  calendarApi.unselect() // clear date selection

  if (title) {
    calendarApi.addEvent({
      id: createEventId(),
      title,
      start: selectInfo.startStr,
      end: selectInfo.endStr,
      allDay: selectInfo.allDay
    })
  }
}

/**
 * @description 选中当前任务事件
 */
const handleEventClick = (clickInfo: EventClickArg) => {
  ElMessageBox.confirm('确定删除当前任务吗？', '温馨提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      clickInfo.event.remove()

      ElMessage({
        type: 'success',
        message: '删除成功'
      })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '已取消删除'
      })
    })
}
/**
 * @description 选中当前事件
 */
const handleEvents = (events: EventApi[]) => {
  currentEvents = events
}

const calendarOptions = reactive({
  plugins: [
    dayGridPlugin,
    timeGridPlugin,
    interactionPlugin // needed for dateClick
  ],
  headerToolbar: {
    left: 'prev,next today',
    center: 'title',
    right: 'dayGridMonth,timeGridWeek,timeGridDay'
  },
  droppable: true,
  drop(info: DropArg) {
    if (checked.value) {
      // if so, remove the element from the "Draggable Events" list
      // eslint-disable-next-line no-unused-expressions
      info.draggedEl.parentNode.removeChild(info.draggedEl)
    }
  },
  initialView: 'dayGridMonth',
  editable: true,
  selectable: true,
  selectMirror: true,
  dayMaxEvents: true,
  weekends: true,
  select: handleDateSelect,
  eventClick: handleEventClick,
  eventsSet: handleEvents,
  events: INITIAL_EVENTS,
  /* 可以通过触发以下事件来更新远程数据库
        eventAdd:
        eventChange:
        eventRemove:
        */
} as CalendarOptions)
// eslint-disable-next-line no-new

const handleWeekendsToggle = () => {
  calendarOptions.weekends = !calendarOptions.weekends // update a property
}

const getMyMeetingSchedule = () => {
  try {
    Service.getMySchedule().then((res) => {
      if (res) {
        console.log('getMyMeetingSchedule success!')
      } else {
        console.log('getMyMeetingSchedule error!')
      }
    });
  } catch (err) {
    ElMessage({
      type: 'warning',
      message: err.message
    })
  }
}

const getMyTodoSchedule = () => {
  try {
    Service.getMyTodoSchedule().then((res) => {
      if (res) {
        const records = res.data;
        for (let record of records) {
          record.id = "todo_" + record.todo_id
          record.title = record.todo_title
          record.start = record.todo_crt.replace(' ', 'T')
          record.end = record.todo_ddl.replace(' ', 'T') + ":00"
          // record.rendering = true
          INITIAL_EVENTS.push(record)
        }
        console.log(INITIAL_EVENTS)
        console.log('getMyTodoSchedule success!')
      } else {
        console.log('getMyTodoSchedule error!')
      }
    });
  } catch (err) {
    ElMessage({
      type: 'warning',
      message: err.message
    })
  }
}

onMounted(() => {
  // getMyMeetingSchedule()
  getMyTodoSchedule()
})
</script>
