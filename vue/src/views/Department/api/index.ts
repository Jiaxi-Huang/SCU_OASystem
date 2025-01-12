import request from '@/utils/request'

class Service {
  /**
   * @description POST 管理员查询用户信息列表
   */
  static postAdminQueryDepartmentList(data: any) {
    return request({
      url: 'http://localhost:8080/api/admin/department/list',
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
  static postAdminDepartmentAdd(data: any) {
    return request({
      url: 'http://localhost:8080/api/admin/department/add',
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
  static postAdminDepartmentDelete(data: any) {
    return request({
      url: 'http://localhost:8080/api/admin/department/delete',
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
