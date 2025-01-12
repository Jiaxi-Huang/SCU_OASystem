import request from '@/utils/request'
import * as assert from "node:assert";

const Api = {
  localHost:'http://localhost:8080',
  getAdminReimbursementList: '/api/reimbursement/getAdminReimbursementList',
  getAdminUserList: '/api/admin/user/statistic',
  getTodoStatistics: '/api/todolist/getTodoStatistics',
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

  static getTodoStatistics() {
    // STUB
    // return [
    //   { name: '收费系统', value: 93 },
    //   { name: '通信系统', value: 66 },
    //   { name: '监控系统', value: 52 },
    //   { name: '供配电系统', value: 34 },
    //   { name: '其他', value: 22 },
    // ];
    return request({
      url: Api.localHost + Api.getTodoStatistics,
      method: 'POST',
      json: true,
    }).then((res) => {
      if (res.status === 0) {
        const statics = [];
        Object.entries(res.data[1]).forEach(([key, value]) => {
          statics.push({
            name: key,
            value: value,
          })
        });
        return [statics, res.data[0]]
      }
      return null
    })
  }
}
export default Service
