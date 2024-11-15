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
}
export default Service
