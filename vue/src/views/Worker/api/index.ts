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
      console.log(res)
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
      console.log(res)
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
  /**
   * @description POST 经理分发指定用户列表待办事项
   */
  static postManagerDistributeTodo(data: any){
    return request({
      url: 'http://localhost:8080/api/admin/distribute/todo',
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      console.log(res)
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
      url: 'http://localhost:8080/api/admin/distribute/meeting',
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      console.log(res)
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
}
export default Service
