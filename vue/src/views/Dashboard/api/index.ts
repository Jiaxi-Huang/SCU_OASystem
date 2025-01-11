import request from '@/utils/request'

const reimbursementApi = {
  localHost:'http://localhost:8080',
  getAdminReimbursementList: '/api/reimbursement/getAdminReimbursementList',}


class Service {

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
