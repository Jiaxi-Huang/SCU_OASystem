import request from '@/utils/request'

const leaveApprovalApi = {
  getLeaveApproval: '/api/leaveApproval/getLeaveRecord',
  localHost:'http://localhost:8080',
  modifyLeaveApproval: '/api/leaveApproval/modifyLeaveRecord',
  addLeaveApproval: '/api/leaveApproval/addLeaveRecord',
  deleteLeaveApproval: '/api/leaveApproval/deleteLeaveRecord'
}

class Service {
  /**
   * @description POST 用户登录接口
   */
  static postGetLeaveApproval() {
    return request({
      url: leaveApprovalApi.localHost + leaveApprovalApi.getLeaveApproval,
      method: 'POST',
      json: true,
    }).then((res) => {
      if (res.status === 0) {
        console.log("postGetLeaveApproval success")
        return res
      }
      return null
    })
  }

  static postModifyLeaveApproval(record:any) {
    let leaveApproval = {
      leave_start_date: record.start_date ,
      leave_end_date: record.end_date ,
      leave_reason: record.reason ,
      leave_status: record.status ,
      leave_submitted_at: record.submitted_at ,
      leave_id: record.leave_id ,
      user_id: record.user_id ,
    }
    console.log(leaveApproval)
    return request({
      url: leaveApprovalApi.localHost + leaveApprovalApi.modifyLeaveApproval,
      method: 'POST',
      json: true,
      data: leaveApproval,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

  static deleteLeaveApproval(record:any) {
    return request({
      url: leaveApprovalApi.localHost + leaveApprovalApi.deleteLeaveApproval,
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

  static addLeaveApproval(record:any) {
    return request({
      url: leaveApprovalApi.localHost + leaveApprovalApi.addLeaveApproval,
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
