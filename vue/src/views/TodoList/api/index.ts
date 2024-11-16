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
    let todoList = {
      todo_ddl: record.ddl ,
      todo_title: record.title ,
      todo_fin: record.status ,
      todo_ctnt: record.content ,
      todo_crt: record.crt ,
      adder_id: record.adder ,
      todo_id: record.todo_id ,
      user_id: record.user_id ,
    }
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
