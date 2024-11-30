import request from '@/utils/request'

const todolistApi = {
  getMeetingList: '/api/meetings/getMyMeetings',
  updateMeeting: '/api/meetings/updateMeeting',
  localHost:'http://localhost:8080',
}


class Service {
  /**
   * @description POST 用户登录接口
   */
  static getPersonalMeetingList() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: todolistApi.localHost + todolistApi.getMeetingList,
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

  static updateMeeting(meeting :any) {
    meeting.acsTkn = sessionStorage.getItem('accessToken')
    console.log(meeting)
    return request({
      url: todolistApi.localHost + todolistApi.updateMeeting,
      method: 'POST',
      json: true,
      data: meeting,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

  static deleteTodo(record:any) {
    record.todo_fin = '未完成'
    return request({
      url: todolistApi.localHost + todolistApi.deleteTodo,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

  static addTodo(record:any) {
    record.todo_fin = '未完成'
    record.acsTkn = sessionStorage.getItem('accessToken')
    // console.log(record.acsTkn)
    return request({
      url: todolistApi.localHost + todolistApi.addTodolist,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }
}
export default Service
