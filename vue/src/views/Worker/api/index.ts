import request from '@/utils/request'

const roleApi = {
  queryAuthedPermission: '/api/auth/permission/routes'
}

class Service {
  /**
   * @description POST 保存授权菜单权限
   */
  static postAuthPermission(data: any) {
    return request({
      url: 'http://localhost:8080/api/auth/permission/routes',//roleApi.queryAuthedPermission,
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
  /**
   * @description POST 经理查询本部门用户信息列表
   */
  static postAdminQueryUserList(data: any) {
    return request({
      url: 'http://localhost:8080/api/admin/user/list',
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
  static postAdminExportUser(data:any){
    return request({
      url: 'http://localhost:8080/api/admin/user/export',
      method: 'POST',
      responseType: 'blob', // 设置响应类型为二进制文件流
      json:true,
      data
    }).then((res) => {
      if (res) {
        if (res instanceof Blob) {
          const url = window.URL.createObjectURL(res);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', '用户列表_' + new Date().getTime() + '.xls'); // 设置下载文件名
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link); // 清理
        } else {
          console.error("响应数据不是 Blob 类型");
        }
      }
    }).catch((err) => {
      console.error('Error downloading excel:', err);
      throw err; // 抛出错误，前端捕获并显示
    });
  }
  static postAdminPrintUser(data:any){
    return request({
      url: 'http://localhost:8080/api/admin/user/download',
      method: 'POST',
      responseType: 'blob', // 设置响应类型为二进制文件流
      json:true,
      data
    }).then((res) => {
      if (res) {
        if (res instanceof Blob) {
          const url = window.URL.createObjectURL(res);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', '用户列表_' + new Date().getTime() + '.pdf'); // 设置下载文件名
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link); // 清理
        } else {
          console.error("响应数据不是 Blob 类型");
        }
      }
    }).catch((err) => {
      console.error('Error downloading pdf:', err);
      throw err; // 抛出错误，前端捕获并显示
    });
  }
  /**
   * @description POST 经理分发指定用户列表待办事项
   */
  static postManagerDistributeTodo(data: any){
    return request({
      url: 'http://localhost:8080/api/todolist/distributed_create',
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
  /**
   * @description POST 经理分发指定用户会议
   */
  static postManagerDistributeMeeting(data: any){
    return request({
      url: 'http://localhost:8080/api/meetings/distributed_create',
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
  static postManagerUpdateMenu(data: any){
    return request({
      url: 'http://localhost:8080/api/admin/menu/update',
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
}
export default Service
