import request from '@/utils/request'

const todolistApi = {
  getTodolist: '/api/todolist/getRec',
  localHost:'http://localhost:8080',
  modifyTodolist: '/api/todolist/modifyRec',
  addTodolist: '/api/todolist/add',
  deleteTodo: '/api/todolist/deleteTodo'
}


class Service {
  /**
   * @description POST 用户登录接口
   */
  static postGetTodoList() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: todolistApi.localHost + todolistApi.getTodolist,
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

  static postModifyTodo(record:any) {
    let todoList = record
    console.log(todoList)
    return request({
      url: todolistApi.localHost + todolistApi.modifyTodolist,
      method: 'POST',
      json: true,
      data: todoList,
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
