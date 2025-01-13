import request from '@/utils/request'
import * as assert from "node:assert";

const Api = {
    localHost:'http://localhost:8080',
    getAdminReimbursementList: '/api/reimbursement/getAdminReimbursementList',
    getAdminUserList: '/api/admin/user/statistic',
    getTodoStatistics: '/api/todolist/getTodoStatistics',
    getFileStatistics: '/api/file/getFileStatistics',
    getAdminAttendanceList: '/api/attendance/getAttendanceStatistics',
    getUserList: '/api/admin/user/list',
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

    static getAdminFileList() {
        return request({
            url: Api.localHost + Api.getFileStatistics,
            method: 'POST',
            json: true,
        }).then((res) => {
            if (res.status === 0) {
                const statics = [];
                Object.entries(res.data[0]).forEach(([key, value]) => {
                    statics.push({
                        name: key,
                        value: value,
                    })
                });
                return [statics, res.data[1]]
            }
            return null
        })
    }
    static getAdminAttendanceList() {
        let pickData=new Date().toISOString().split('T')[0]
        return request({
            url: `${Api.localHost + Api.getAdminAttendanceList}?pickDate=${pickData}`,
            method: 'POST',
            json: true,
        }).then((res) => {
            if (res.status === 0) {
                const statics = [];
                Object.entries(res.data[0]).forEach(([key, value]) => {
                    let tmp;
                    if(key=="Absent"){
                        tmp="缺席"
                    }
                    if(key=="Late"){
                        tmp="迟到"
                    }
                    if(key=="Leave Early"){
                        tmp="早退"
                    }
                    if(key=="On Time"){
                        tmp="准时"
                    }
                    if(key=="Late And Leave Early"){
                        tmp="迟到早退"
                    }
                    statics.push({
                        name: tmp,
                        value: value,
                    })
                });
                console.log(statics)
                console.log(res.data[1])
                return [statics, res.data[1]]
            }
            return null
        })
    }

    static getUserList(data: any) {
        return request({
            url: Api.localHost + Api.getUserList, // 使用 Api 对象中的 URL
            method: 'POST',
            json: true,
            data, // 传递请求体
        })
            .then((res) => {
                // 检查响应状态码
                if (res.status === 0) {
                    return res.data; // 返回用户列表数据
                } else {
                    // 如果状态码不是 0，打印错误信息
                    console.error('获取用户列表失败:', res.message || '未知错误');
                    return null;
                }
            })
            .catch((error) => {
                // 捕获请求失败的错误
                console.error('请求失败:', error.response?.data || error.message);
                return null;
            });
    }
}
export default Service
