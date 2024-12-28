import request from '@/utils/request'

const attendanceApi = {
  localHost:'http://localhost:8080',
  getAttendanceRecord: '/api/attendance/loadAttendance',
  addAttendance: '/api/attendance/addAttendance',
  delAttendance:'/api/attendance/delAttendance',
  editAttendance:'/api/attendance/editAttendance',
}

class Service {
  static postAttendanceList(data:any) {
    console.log("进入attend record")
    return request({
      url: attendanceApi.localHost + attendanceApi.getAttendanceRecord,
      method: 'POST',
      json: true,
    }).then((res) => {
      if (res.status === 0) {
        console.log(res)
        return res;
      }
      else{
        console.log(res)
      }
    })
  }
  static postAddAttendance(data: any) {
    console.log(data)
    return request({
      url: attendanceApi.localHost + attendanceApi.addAttendance,
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

  static postDeleteAttendance(id: int) {
    return request({
      url: `${attendanceApi.localHost + attendanceApi.delAttendance}?id=${id}`,
      method: 'POST',
      json: true,
      id
    }).then((res) => {
      if (res.status === 0) {
        return Promise.resolve(res)
      }
      return Promise.reject(res)
    })
  }



  static postEditAttendance(data: any) {
    return request({
      url: attendanceApi.localHost + attendanceApi.editAttendance,
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
  static postAdminDeleteUser(data:any) {
    return request({
      url: 'http://localhost:8080/api/admin/user/delete',
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
  static postAdminExportUser(data:any){
    return request({
      url: 'http://localhost:8080/api/admin/user/export',
      method: 'POST',
      responseType: 'blob', // 设置响应类型为二进制文件流
      json:true,
      data
    }).then((res) => {
      if (res) {
        if (res instanceof Blob) {
          const url = window.URL.createObjectURL(res);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', '用户列表_' + new Date().getTime() + '.xls'); // 设置下载文件名
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
  static postAdminPrintUser(data:any){
    return request({
      url: 'http://localhost:8080/api/admin/user/download',
      method: 'POST',
      responseType: 'blob', // 设置响应类型为二进制文件流
      json:true,
      data
    }).then((res) => {
      if (res) {
        if (res instanceof Blob) {
          const url = window.URL.createObjectURL(res);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', '用户列表_' + new Date().getTime() + '.pdf'); // 设置下载文件名
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link); // 清理
        } else {
          console.error("响应数据不是 Blob 类型");
        }
      }
    }).catch((err) => {
      console.error('Error downloading pdf:', err);
      throw err; // 抛出错误，前端捕获并显示
    });
  }
}


export default Service
