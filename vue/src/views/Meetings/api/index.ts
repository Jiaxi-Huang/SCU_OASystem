import request from '@/utils/request'

const meetingApi = {
  getMeetingList: '/api/meetings/getMyMeetings',
  updateMeeting: '/api/meetings/updateMeeting',
  createMeeting: '/api/meetings/createMeeting',
  deleteMeetingPersonally: '/api/meetings/deleteMeetingPersonally',
  addMeetingPersonally: '/api/meetings/addMeetingPersonally',
  search_by_mtin_id: '/api/meetings/search_by_mtin_id',
  localHost:'http://localhost:8080',
  getPDF: '/api/meetings/getPdf',
  getExcel: '/api/meetings/getExcel',
  searchBy: '/api/meetings/searchByFieldKey',
}


class Service {
  /**
   * @description POST 用户登录接口
   */
  static getPersonalMeetingList() {
    const data = {'accessToken':sessionStorage.getItem('accessToken')}
    return request({
      url: meetingApi.localHost + meetingApi.getMeetingList,
      method: 'POST',
      json: true,
      data: data,
    }).then((res) => {
      if (res.status === 0) {
        // console.log("postGetTodoList success")
        return res
      }
      return null
    })
  }

  static searchBy(record: any) {
    record.accessToken = sessionStorage.getItem('accessToken')
    return request({
      url: meetingApi.localHost + meetingApi.searchBy,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      // console.log("searchBy return")
      // console.log(res)
      if (res.status === 0 || res.status === 1) {
        // console.log("searchBy success")
        return res
      }
      return null
    })
  }

  static getPDF() {
    const data = {'accessToken': sessionStorage.getItem('accessToken')}

    return request({
      url: meetingApi.localHost + meetingApi.getPDF,
      method: 'POST',
      responseType: 'blob', // 设置响应类型为二进制文件流
      contentType: 'application/json', // 确保是发送 JSON 数据
      data: data,
    }).then((res) => {
      if (res) {
        if (res instanceof Blob) {
          const url = window.URL.createObjectURL(res);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', '我的会议_' + new Date().getTime() + '.pdf'); // 设置下载文件名
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link); // 清理
        } else {
          console.error("响应数据不是 Blob 类型");
        }
      }
    }).catch((err) => {
      console.error('Error downloading PDF:', err);
      throw err; // 抛出错误，前端捕获并显示
    });
  }

  static getExcel() {
    const data = {'accessToken': sessionStorage.getItem('accessToken')}

    return request({
      url: meetingApi.localHost + meetingApi.getExcel,
      method: 'POST',
      responseType: 'blob', // 设置响应类型为二进制文件流
      contentType: 'application/json', // 确保是发送 JSON 数据
      data: data,
    }).then((res) => {
      if (res) {
        if (res instanceof Blob) {
          const url = window.URL.createObjectURL(res);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', '我的会议_' + new Date().getTime() + '.xls'); // 设置下载文件名
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link); // 清理
        } else {
          console.error("响应数据不是 Blob 类型");
        }
      }
    }).catch((err) => {
      console.error('Error downloading excel:', err);
      throw err; // 抛出错误，前端捕获并显示
    });
  }

  static addMeetingPersonally(item:any) {
    const record = {
      mtin_id: item.mtin_id,
      accessToken: sessionStorage.getItem('accessToken')
    }
    return request({
      url: meetingApi.localHost + meetingApi.addMeetingPersonally,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      if (res.status === 0) {
        // console.log("addMeetingPersonally success")
        return res
      } else {
        // console.log(res.message)
      }
      return null
    })
  }

  static deleteMeetingPersonally(item:any) {
    const record = {
      mtin_id: item.mtin_id,
      accessToken: sessionStorage.getItem('accessToken')
    }
    return request({
      url: meetingApi.localHost + meetingApi.deleteMeetingPersonally,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      if (res.status === 0) {
        // console.log("deleteMeetingPersonaly success")
        return res
      } else {
        // console.log(res.message)
      }
      return null
    })
  }

  static updateMeeting(meeting :any) {
    meeting.acsTkn = sessionStorage.getItem('accessToken')
    // console.log(meeting)
    return request({
      url: meetingApi.localHost + meetingApi.updateMeeting,
      method: 'POST',
      json: true,
      data: meeting,
    }).then((res) => {
      if (res.status === 0) {
        return res
      }
      return null
    })
  }

  static deleteMeeting(record:any) {
    record.todo_fin = '未完成'
    return request({
      url: meetingApi.localHost + meetingApi.deleteTodo,
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

  static postCreateMeeting(record:any) {
    return request({
      url: meetingApi.localHost + meetingApi.createMeeting,
      method: 'POST',
      json: true,
      data: record,
    }).then((res) => {
      // console.log(res)
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }

  static search_by_mtin_id(mtin_id: any) {
    const data = {data:[Number(mtin_id)]}
    // console.log(data)
    return request({
      url: meetingApi.localHost + meetingApi.search_by_mtin_id,
      method: 'POST',
      json: true,
      data: data,
    }).then((res) => {
      // console.log(res)
      if (res.status ===  0 || res.status === 1) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }

}
export default Service
