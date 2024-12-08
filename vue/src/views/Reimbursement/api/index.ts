import request from '@/utils/request'

const reimbursementApi = {
  getReimbursementList: '/api/reimbursement/getReimbursementList',
  getAdminReimbursementList: '/api/reimbursement/getAdminReimbursementList',
  updateReimbursement: '/api/reimbursement/modifyReimbursementRecord',
  deleteReimbursement: '/api/reimbursement/deleteReimbursementRecord',
  addReimbursement: '/api/reimbursement/addReimbursementRecord',
  localHost:'http://localhost:8080',
}


class Service {
  /**
   * @description POST 用户登录接口
   */
  static getReimbursementList() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: reimbursementApi.localHost + reimbursementApi.getReimbursementList,
      method: 'POST',
      json: true,
      data: data,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

  static updateReimbursement(reimbursement :any) {
    reimbursement.acsTkn = sessionStorage.getItem('accessToken')
    console.log(reimbursement)
    return request({
      url: reimbursementApi.localHost + reimbursementApi.updateReimbursement,
      method: 'POST',
      json: true,
      data: reimbursement,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

  static deleteReimbursement(record:any) {
    return request({
      url: reimbursementApi.localHost + reimbursementApi.deleteReimbursement,
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

  static addReimbursement(record:any) {
    record.acsTkn = sessionStorage.getItem('accessToken')
    // console.log(record.acsTkn)
    return request({
      url: reimbursementApi.localHost + reimbursementApi.addReimbursement,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      if (res.status === 200) {
        return res
      }
      return null
    })
  }

    static getAdminReimbursementList() {
      return request({
        url: reimbursementApi.localHost + reimbursementApi.getAdminReimbursementList,
        method: 'POST',
        json: true,
      }).then((res) => {
        if (res.status === 200) {
          return res
        }
        return null
      })
    }
}
export default Service
