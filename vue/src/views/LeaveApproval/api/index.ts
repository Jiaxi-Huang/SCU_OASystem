import request from '@/utils/request'

const leaveApprovalApi = {
  localHost:'http://localhost:8080',
  getMyLeaveRecord: '/api/leaveApproval/getMyLeaveRecord',
  getNotifyLeaveRecord: '/api/leaveApproval/getNotifyLeaveRecord',
  getReviewLeaveRecord: '/api/leaveApproval/getReviewLeaveRecord',
  getAdminLeaveRecord: '/api/leaveApproval/getAdminLeaveRecord',
  modifyLeaveApproval: '/api/leaveApproval/modifyLeaveRecord',
  addLeaveApproval: '/api/leaveApproval/addLeaveRecord',
  deleteLeaveApproval: '/api/leaveApproval/deleteLeaveRecord',
  addNotification: '/api/notification/addNotification',
  getAllUsers: '/api/notification/getAllUsers',
}

class Service {
  static postGetMyLeaveRecord() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: leaveApprovalApi.localHost + leaveApprovalApi.getMyLeaveRecord,
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
  static postGetNotifyLeaveRecord() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: leaveApprovalApi.localHost + leaveApprovalApi.getNotifyLeaveRecord,
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
  static postGetReviewLeaveRecord() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: leaveApprovalApi.localHost + leaveApprovalApi.getReviewLeaveRecord,
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
  static postGetAdminLeaveRecord() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: leaveApprovalApi.localHost + leaveApprovalApi.getAdminLeaveRecord,
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
  static getAllUsers() {
    return request({
      url: leaveApprovalApi.localHost + leaveApprovalApi.getAllUsers,
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
