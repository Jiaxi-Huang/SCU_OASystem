import request from '@/utils/request'

const reimbursementApi = {
  getMyReimbursementList: '/api/reimbursement/getReimbursementList',
  getAdminReimbursementList: '/api/reimbursement/getAdminReimbursementList',
  getReviewReimbursementList: '/api/reimbursement/getReviewReimbursementList',
  getNotifyReimbursementList: '/api/reimbursement/getNotifyReimbursementList',
  updateReimbursement: '/api/reimbursement/modifyReimbursementRecord',
  deleteReimbursement: '/api/reimbursement/deleteReimbursementRecord',
  addReimbursement: '/api/reimbursement/addReimbursementRecord',
  addNotification: '/api/notification/addNotification',
  getAllUsers: '/api/notification/getAllUsers',
  localHost:'http://localhost:8080',
}


class Service {
  /**
   * @description POST 用户登录接口
   */
  static getMyReimbursementList() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: reimbursementApi.localHost + reimbursementApi.getMyReimbursementList,
      method: 'POST',
      json: true,
      data: data,
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

  static getReviewReimbursementList() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: reimbursementApi.localHost + reimbursementApi.getReviewReimbursementList,
      method: 'POST',
      json: true,
      data: data,
    }).then((res) => {
      if (res.status === 200) {
        return res
      }
      return null
    })
  }

  static getNotifyReimbursementList() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: reimbursementApi.localHost + reimbursementApi.getNotifyReimbursementList,
      method: 'POST',
      json: true,
      data: data,
    }).then((res) => {
      if (res.status === 200) {
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
    return request({
      url: reimbursementApi.localHost + reimbursementApi.addReimbursement,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      console.log("res reimbursement_id: " + JSON.stringify(res.reimbursement_id))
      if (res.status === 200) {
        return res
      }
      return res.reimbursement_id
    })
  }

    static addNotification(record:any) {
      return request({
        url: reimbursementApi.localHost + reimbursementApi.addNotification,
        method: 'POST',
        json: true,
        data: record,
      }).then((res) => {
        if (res.status === 200) {
          return res
        }
        return res.status
      })
    }

    static getAllUsers() {
      return request({
        url: reimbursementApi.localHost + reimbursementApi.getAllUsers,
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
