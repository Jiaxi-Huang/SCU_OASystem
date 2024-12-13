import request from '@/utils/request'

const todolistApi = {
  getTodolist: '/api/todolist/getRec',
  localHost:'http://localhost:8080',
  modifyTodolist: '/api/todolist/modifyRec',
  addTodolist: '/api/todolist/add',
  deleteTodo: '/api/todolist/deleteTodo',
  getPDF: '/api/todolist/getPdf',
  getExcel: '/api/todolist/getExcel',
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

  static getPDF() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}

    return request({
      url: todolistApi.localHost + todolistApi.getPDF,
      method: 'POST',
      responseType: 'blob', // 设置响应类型为二进制文件流
      contentType: 'application/json', // 确保是发送 JSON 数据
      data: data,
    }).then((res) => {
      if (res) {
        if (res instanceof Blob) {
          const url = window.URL.createObjectURL(res);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', '待办事项_' + new Date().getTime() + '.pdf'); // 设置下载文件名
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link); // 清理
        } else {
          console.error("响应数据不是 Blob 类型");
        }
      }
    }).catch((err) => {
      console.error('Error downloading PDF:', err);
      throw err; // 抛出错误，前端捕获并显示
    });
  }

  static getExcel() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}

    return request({
      url: todolistApi.localHost + todolistApi.getExcel,
      method: 'POST',
      responseType: 'blob', // 设置响应类型为二进制文件流
      contentType: 'application/json', // 确保是发送 JSON 数据
      data: data,
    }).then((res) => {
      if (res) {
        if (res instanceof Blob) {
          const url = window.URL.createObjectURL(res);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', '待办事项_' + new Date().getTime() + '.xls'); // 设置下载文件名
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link); // 清理
        } else {
          console.error("响应数据不是 Blob 类型");
        }
      }
    }).catch((err) => {
      console.error('Error downloading PDF:', err);
      throw err; // 抛出错误，前端捕获并显示
    });
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

  static deleteMulti(selectionRows: []) {
    let res = null;
    for (let todo_id of selectionRows) {
      res = this.deleteTodo({todo_id: todo_id})
    }
    return res
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

export function blobValidate(data) {
  return data.type !== 'application/json'
}
