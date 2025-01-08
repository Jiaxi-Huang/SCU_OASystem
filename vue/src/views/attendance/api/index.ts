import request from '@/utils/request'
import {dayjs} from "element-plus";

const attendanceApi = {
  localHost:'http://localhost:8080',
  getAttendanceRecord: '/api/attendance/loadAttendance',
  addAttendance: '/api/attendance/addAttendance',
  delAttendance:'/api/attendance/delAttendance',
  editAttendance:'/api/attendance/editAttendance',
  personalAttendance:'/api/attendance/personalAttendance',
  checkInAttendance:'/api/attendance/checkInAttendance',
  checkOutAttendance:'/api/attendance/checkOutAttendance',
  exportAttendance:'/api/attendance/exportAttendance',
  printAttendance:'/api/attendance/printAttendance',
}

class Service {
  static postAttendanceList(pickDate:String) {
    console.log("进入attend record")
    let formattedDate = dayjs(pickDate).format('YYYY-MM-DD');
    console.log(formattedDate);  // 输出：2024-12-28
    return request({
      url: `${attendanceApi.localHost + attendanceApi.getAttendanceRecord}?pickDate=${formattedDate}`,
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


    static postPersonalAttendance(data:any) {
        return request({
            url: `${attendanceApi.localHost + attendanceApi.personalAttendance}?accessToken=${data.accessToken}`,
            method: 'POST',
            json: true,

        }).then((res) => {
            if (res.status === 0) {
                return Promise.resolve(res)
            }
            return Promise.reject(res)
        })
    }


    static postCheckInAttendance(accessToken:String) {
        return request({
            url: `${attendanceApi.localHost + attendanceApi.checkInAttendance}?accessToken=${accessToken}`,
            method: 'POST',
            json: true,
        }).then((res) => {
            if (res.status === 0||res.status === -1) {
                return Promise.resolve(res)
            }
            return Promise.reject(res)
        })
    }


    static postCheckOutAttendance(accessToken:String) {
        return request({
            url: `${attendanceApi.localHost + attendanceApi.checkOutAttendance}?accessToken=${accessToken}`,
            method: 'POST',
            json: true,
        }).then((res) => {
            if (res.status === 0||res.status === -1||res.status === -2) {
                return Promise.resolve(res)
            }
            return Promise.reject(res)
        })
    }

  static postAdminExportAttendance(data:any){
    return request({
      url: attendanceApi.localHost + attendanceApi.exportAttendance,
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
          link.setAttribute('download', '考勤列表_' + new Date().getTime() + '.xls'); // 设置下载文件名
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
  static postAdminPrintAttendance(data:any){
    return request({
      url: attendanceApi.localHost + attendanceApi.printAttendance,
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
          link.setAttribute('download', '考勤列表_' + new Date().getTime() + '.pdf'); // 设置下载文件名
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
