import request from '@/utils/request'
import {ElMessage} from "element-plus";

const fileApi = {
    localHost:'http://localhost:8080',
    loadFile: '/api/file/loadFile',
    loadFolder: '/api/folder/loadFolder',
    createFolder:'/api/folder/createFolder',
    modifyFolder: '/api/folder/modifyFolder',
    moveFolder:'/api/folder/moveFolder',
    delFolder:'/api/folder/delFolder',
    moveFile:'/api/file/moveFile',
    delFile:'/api/file/delFile',
    remarkFile:'/api/file/remarkFile',
    uploadFile:'/api/file/uploadFile',
    modifyFile:'/api/file/modifyFile'
}


class Service {
    /**
     * @description POST 用户登录接口
     */
    static loadFile(dir_id:any) {
        const data = {'accessToken':sessionStorage.getItem('accessToken')}
        return request({
            url: `${fileApi.localHost + fileApi.loadFile}?dir_id=${dir_id}`,
            //传递文件夹指向，确定是个人还是部门还是公司
            method: 'POST',
            json: true,
            data: data
        }).then((res) => {
            if (res.status === 0) {
                console.log("loadFile success")
                console.log(res)
                return res
            }else{
                console.log("loadFile error111")
                console.log(res.status)
            }
            return null
        })
    }
    static loadFolder(i:any) {
        const data = {'accessToken':sessionStorage.getItem('accessToken')}
        return request({
            url:fileApi.localHost + fileApi.loadFolder,
            method: 'POST',
            json: true,
            data: data
        }).then((res) => {
            if (res.status === 0) {
                console.log("loadFolder success")
                console.log(res)
                return res
            }else{
                console.log("loadFolder error111")
            }
            return null
        })
    }

    static createFolder(folderData:any) {
        let record = {
            title: folderData.folder_name ,
            pid: folderData.folder_pid ,
            acsTkn: sessionStorage.getItem('accessToken')
        }
        return request({
            url: fileApi.localHost + fileApi.createFolder,
            method: 'POST',
            json: true,
            data: record,
        }).then((res) => {
            if (res.status === 0) {
                return res;
            }
            if (res.status === -1){
                // 权限不足时弹出提示
                ElMessage.error('您的权限不够，无法创建文件夹')
            }
            return null
        })
    }
    static modifyFolder(folderData:any) {
        let record = {
            id:folderData.folder_value,
            title: folderData.folder_name ,
            acsTkn: sessionStorage.getItem('accessToken')
        }
        return request({
            url: fileApi.localHost + fileApi.modifyFolder,
            method: 'POST',
            json: true,
            data: record,
        }).then((res) => {
            if (res.status === 0) {
                return res;
            }
            if (res.status === -1){
                ElMessage.error('您的权限不够，无法修改文件夹名称')
            }
            return null
        })
    }
    static moveFolder(folderData:any) {
        let record = {
            id:folderData.current_id,
            pid: folderData.target_pid ,
            acsTkn: sessionStorage.getItem('accessToken')
        }
        return request({
            url: fileApi.localHost + fileApi.moveFolder,
            method: 'POST',
            json: true,
            data: record,
        }).then((res) => {
            if (res.status === 0) {
                return res
            }
            if (res.status === -1){
                ElMessage.error('您的权限不够，无法移动文件夹')
            }
            return null
        })
    }


    static delFolder(folderData:any) {
        let record = {
            id:folderData.id,
            acsTkn: sessionStorage.getItem('accessToken')
        }
        return request({
            url: fileApi.localHost + fileApi.delFolder,
            method: 'POST',
            json: true,
            data: record,
        }).then((res) => {
            if (res.status === 0) {
                return res
            }
            if(res.status === -1){
                ElMessage.error('您的权限不够，无法删除文件夹')
            }
            return null
        })
    }


    static moveFile(folderData:any) {
        let record = {
            ids:folderData.select_file_id,
            dirId:folderData.target_pid,
            beforeDirId:folderData.select_file_before_dir_id,
            acsTkn: sessionStorage.getItem('accessToken')
        }
        return request({
            url: fileApi.localHost + fileApi.moveFile,
            method: 'POST',
            json: true,
            data: record,
        }).then((res) => {
            if (res.status === 0) {
                return res
            }
            if(res.status === -1){
                ElMessage.error('您的权限不够，无法移动文件')
            }
            return null
        })
    }


    static delFile(folderData:any) {
        let record = {
            ids:folderData.select_file_id,
            beforeDirId: folderData.select_file_before_dir_id,
            acsTkn: sessionStorage.getItem('accessToken')
        }
        return request({
            url: fileApi.localHost + fileApi.delFile,
            method: 'POST',
            json: true,
            data: record,
        }).then((res) => {
            if (res.status === 0) {
                return res
            }
            if(res.status === -1){
                ElMessage.error('您的权限不够，无法删除文件')
            }
            return null
        })
    }

    static remarkFile(folderData:any) {
        let record = {
            ids:folderData.select_file_id,
            remark:folderData.remark_content,
            beforeDirId:folderData.select_file_before_dir_id,
            acsTkn: sessionStorage.getItem('accessToken')
        }
        return request({
            url: fileApi.localHost + fileApi.remarkFile,
            method: 'POST',
            json: true,
            data: record,
        }).then((res) => {
            if (res.status === 0) {
                return res
            }
            if(res.status === -1){
                ElMessage.error('您的权限不够，无法备注文件')
            }
            return null
        })
    }

    static modifyFile(Data:any) {
        let record = {
            id:Data.id,
            fileName:Data.file_name,
            dirId:Data.dir_id,
            acsTkn: sessionStorage.getItem('accessToken')
        }
        console.log(record)
        return request({
            url: fileApi.localHost + fileApi.modifyFile,
            method: 'POST',
            json: true,
            data: record,
        }).then((res) => {
            if (res.status === 0) {
                return res
            }
            if(res.status === -1){
                ElMessage.error('您的权限不够，无法修改文件名称')
            }
            return null
        })
    }



    static uploadFile(folderData:any) {

        let data=folderData.record
        // 找到最后一个点的位置
        let dotIndex = data.file.name.lastIndexOf('.');

        // 分离文件名和扩展名
        let fileName = data.file.name.substring(0, dotIndex);
        let ext = data.file.name.substring(dotIndex + 1);

        console.log(fileName)
        console.log(ext)
        let record = {
            fileName:fileName,
            ext:ext,
            size:data.file.size,
            userId:data.data.user,
            dirId: data.data.folder_id
        }
        console.log(record)
        return request({
            url: fileApi.localHost + fileApi.uploadFile,
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
