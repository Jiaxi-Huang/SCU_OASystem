import request from '@/utils/request'

const personalApi = {
  basicInfo: '/api/setting/basicInfo',
  personalTags: '/api/personal/tags'
}

class Service {
  /**
   * @description POST 设置基本信息
   */
  static postSetBasicInfo(data: any) {
    return request({
      url: 'http://localhost:8080/api/setting/basicInfo',//personalApi.basicInfo,
      method: 'post',
      json: true,
      data
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
  static postSetEmail(data: any){
    return request({
      url: 'http://localhost:8080/api/setting/resetEmail',//personalApi.basicInfo,
      method: 'post',
      json: true,
      data
    }).then((res)=>{
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
  /**
   * @description   POST 发送验证码 /auth/email/sendCaptcha
   */
  static postCaptcha(data: any) {
    return request({
      url: 'http://localhost:8080/api/setting/email/sendCaptcha',
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
   * @description Post 重置密码
   */
  static postSetPassword(data:any){
    return request({
      url: 'http://localhost:8080/api/setting/resetPassword',
      method: 'post',
      json: true,
      data
    }).then((res)=>{
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
  /**
   * @description Get 获取tags
   */
  static getPersonTags() {
    return request({
      url: personalApi.personalTags,
      method: 'get',
      json: true
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }
}
export default Service
