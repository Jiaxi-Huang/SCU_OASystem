import request from '@/utils/request'

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
    uploadFile:'/api/file/uploadFile'
}


class Service {
    /**
     * @description POST 用户登录接口
     */
    static loadFile() {
        const data = {'accessToken':sessionStorage.getItem('accessToken')}
        return request({
            url: fileApi.localHost + fileApi.loadFile,
            method: 'POST',
            json: true,
            data: data,
        }).then((res) => {
            if (res.status === 0) {
                console.log("loadFile success")
                console.log(res)
                return res
            }else{
                console.log("loadFile error111")
            }
            return null
        })
    }
    static loadFolder() {
        const data = {'accessToken':sessionStorage.getItem('accessToken')}
        return request({
            url: fileApi.localHost + fileApi.loadFolder,
            method: 'POST',
            json: true,
            data: data,
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
                return res
            }
            return null
        })
    }
    static modifyFolder(folderData:any) {
        let record = {
            id:folderData.folder_value,
            title: folderData.folder_name ,
        }
        return request({
            url: fileApi.localHost + fileApi.modifyFolder,
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
    static moveFolder(folderData:any) {
        let record = {
            id:folderData.current_id,
            pid: folderData.target_pid ,
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
            return null
        })
    }


    static delFolder(folderData:any) {
        let record = {
            id:folderData.id,
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
            return null
        })
    }


    static moveFile(folderData:any) {
        let record = {
            ids:folderData.select_file_id,
            dirId:folderData.target_pid
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
            return null
        })
    }


    static delFile(folderData:any) {
        let record = {
            ids:folderData.select_file_id,
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
            return null
        })
    }

    static remarkFile(folderData:any) {
        let record = {
            ids:folderData.select_file_id,
            remark:folderData.remark_content
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
