import { EventInput } from '@fullcalendar/vue3'
import request from '@/utils/request'

// 事件Uid初始化
let eventGuid = 0
/**
 * @description 日期字符串格式化
 */
const todayStr = new Date().toISOString().replace(/T.*$/, '') // YYYY-MM-DD of today
/**
 * @description 事件Uid
 */
export function createEventId() {
  return String(eventGuid++)
}
/**
 * @description 初始化事件对象
 */

const scheduleApi = {
  getTodolist: '/api/todolist/getRec',
  getMeetingList: '/api/meetings/getMyMeetings',
  localHost:'http://localhost:8080',
}

class Service {
  static getMyMeetingSchedule() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: scheduleApi.localHost + scheduleApi.getMeetingList,
      method: 'POST',
      json: true,
      data: data,
    }).then((res) => {
      if (res.status === 0) {
        console.log("postGetTodoList success")
        console.log(res)
        return res
      }
      return null
    })
  }

  static getMyTodoSchedule() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: scheduleApi.localHost + scheduleApi.getTodolist,
      method: 'POST',
      json: true,
      data: data,
    }).then((res) => {
      if (res.status === 0) {
        console.log("postGetTodoList success")
        return res
      }
      return null
    })
  }

}
export default Service
