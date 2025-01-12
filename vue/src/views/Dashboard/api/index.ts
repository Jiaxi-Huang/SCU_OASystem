import request from '@/utils/request'

const Api = {
  localHost:'http://localhost:8080',
  getAdminReimbursementList: '/api/reimbursement/getAdminReimbursementList',
  getAdminUserList: '/api/admin/user/statistic',
}


class Service {
  static getAdminReimbursementList() {
    return request({
      url: Api.localHost + Api.getAdminReimbursementList,
      method: 'POST',
      json: true,
    }).then((res) => {
      if (res.status === 200) {
        return res
      }
      return null
    })
  }
  static getAdminUserStatistic(data: any) {
    return request({
      url: Api.localHost + Api.getAdminUserList,
      method: 'POST',
      json: true,
      data
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }
}
export default Service
