import request from '@/utils/request'

const todolistApi = {
  getTodolist: '/api/todolist/getRec',
  localHost:'http://localhost:8080',
  modifyTodolist: '/api/todolist/modifyRec',
}


class Service {
  /**
   * @description POST 用户登录接口
   */
  static postGetTodoList() {
    return request({
      url: todolistApi.localHost + todolistApi.getTodolist,
      method: 'POST',
      json: true,
    }).then((res) => {
      if (res.status === 0) {
        console.log("postGetTodoList success")
        return res
      }
      return null
    })
  }

  static postModifyTodo(record:any) {
    return request({
      url: todolistApi.localHost + todolistApi.modifyTodolist,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      if (res.status === 0) {
        console.log("postModifyTodo success")
        return res
      }
      return null
    })
  }
}
export default Service
