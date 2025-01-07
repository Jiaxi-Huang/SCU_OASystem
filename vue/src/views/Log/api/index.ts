import request from '@/utils/request'

const roleApi = {
  queryAuthedPermission: '/api/auth/permission/routes'
}

class Service {
  /**
   * @description POST 管理员查询操作日志列表
   */
  static postAdminQueryLogList(data: any) {
    return request({
      url: 'http://localhost:8080/api/log/list',
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
   * @description POST 管理员删除操作日志列表
   */
  static postAdminDeleteLog(data:any) {
    return request({
      url: 'http://localhost:8080/api/log/delete',
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
   * @description POST 管理员导出操作日志列表
   */
  static postAdminExportLog(data:any){
    return request({
      url: 'http://localhost:8080/api/log/export',
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
          link.setAttribute('download', '操作日志_' + new Date().getTime() + '.xls'); // 设置下载文件名
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
  /**
   * @description POST 管理员查询打印日志列表
   */
  static postAdminPrintLog(data:any){
    return request({
      url: 'http://localhost:8080/api/log/download',
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
          link.setAttribute('download', '操作日志_' + new Date().getTime() + '.pdf'); // 设置下载文件名
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
