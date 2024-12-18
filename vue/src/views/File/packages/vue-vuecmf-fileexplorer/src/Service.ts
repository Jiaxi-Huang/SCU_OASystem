
import {onMounted, reactive, ref, toRefs, ToRefs} from "vue";
import {AnyObject} from "./typings/vuecmf";
import {
    ElMessage,
    ElMessageBox,
    ElTable,
    UploadFile,
    UploadFiles,
    UploadInstance,
    UploadProgressEvent,
    UploadRawFile, UploadUserFile
} from "element-plus";
import ClipboardJS from "clipboard";


import request from '@/utils/request'

const fileApi = {
    localHost:'http://localhost:8080',
    judgeFileType:'/api/file/judgeFileType'
}


export default class Service {
    emit: EmitFn<EE[]>

    //基本信息配置
    config = reactive({
        vuecmf_fileexplorer_ref: ref(),
        folder_list_width: ref('220px'), //文件夹区域宽度
        folder_display: ref('display: inline-block'), //文件夹区域显示与隐藏
        filterFolderKeyWord: ref(''), //筛选文件夹关键字
        collapsePadding: ref('padding-left: 10px;'), //折叠按钮边距
        is_collapse: ref(false),  //文件夹是否已折叠
        scrollbar_height: ref('calc(80VH - 45px)'),  //文件夹滚动区域高度
        tool: ref<AnyObject>([]),  //工具条
        is_help_dlg: false,  //是否显示帮助弹窗

        folder_tree_ref: ref(),
        folder: {
            root_path: ref('uploads'), //文件夹根目录
            data: ref<AnyObject>([{id: 0, title: '个人文件管理', children: ref<AnyObject>([])},
                {id: -1, title: '部门文件管理', children: ref<AnyObject>([])},
                {id: -2, title: '公司文件管理', children: ref<AnyObject>([])},
            ]),  //文件夹列表数据
            searchData: ref<AnyObject>([{id: 0, title: '个人文件管理', children: ref<AnyObject>([])},
                {id: -1, title: '部门文件管理', children: ref<AnyObject>([])},
                {id: -2, title: '公司文件管理', children: ref<AnyObject>([])},
            ]),  //文件夹列表数据
            defaultProps: {
                children: 'children',
                label: 'title',
                value: 'id'
            },
            current_select: ref(), //当前选择的文件夹
            current_select_key: ref(0), //当前选择的文件夹KEY
            folder_dlg: false,  //是否显示文件夹弹窗
            folder_dlg_title: '', //文件夹弹窗标题
            current_pid: ref(0), //当前选择的父级ID
            folder_name: ref(''), //当前文件夹名称
            folder_value: ref(0), //当前文件夹值
            keywords: ref(''), //文件夹搜索关键词
            is_new: true, //是否是新建文件夹

            move_folder_dlg: false, //是否显示移动文件夹弹窗
            move_pid: ref(0), //当前移动到的父级
        },

        file_table_ref: ref(),
        file: {
            table_height: 'calc(80VH - 80px)',        //列表表格高度
            page_layout: "total, sizes,prev, pager, next", //分页条展示形式
            current_page: 1, //当前页码数
            page_size: 10,   //每页显示条数
            total: 0, //总条数
            path: '/', //当前文件夹路径
            keywords: ref(''), //文件搜索关键词
            data: ref<AnyObject>(),  //文件列表数据
            fileExtensions: ref<AnyObject>(),
            isExt:false,
            extFilter: ref([]), //文件列表过滤器
            filter: ref({}), //文件列表过滤器
            list_show: 'card', //文件列表展示方式 card = 缩略图，list = 列表
            select_files: ref(), //已选择的文件信息

            move_file_dlg: false, //是否显示移动文件弹窗
            move_pid: ref(0), //当前移动到的父级

            upload_file_dlg: false, //是否显示上传文件弹窗
            tableRef: ref<InstanceType<typeof ElTable>>(), //文件列表table实例
            current_input_file: ref(), //当前修改的文件
            uploadInstance: ref<UploadInstance>(), //上传组件实例
            current_folder_id: ref(0), //当前上传文件选择的文件夹ID
            remark_file_dlg: false, //是否显示备注弹窗
            remark_content: ref(''),  //备注内容
            order_field: ref('update_time'),  //文件列表排序字段
            order_sort: ref('desc'),  //排序类型
        },
    })

    constructor(init_config: AnyObject, emit:EmitFn<EE[]>) {
        this.emit = emit
        this.config.folder.root_path = init_config.root_path
        this.config.folder.data[0].title = init_config.root_path.value
        this.config.file.page_size = init_config.page_size
        this.config.file.path = '/' + init_config.root_path.value
        this.config.file.list_show = init_config.list_show.value
        this.config.file.tableRef = init_config.tableRef

        //加载文件夹列表
        this.emit('loadFolder', this.config.folder)
        //加载文件列表
        this.emit('loadFile', this.config.file)



        this.config.tool = [
            { name: 'new_folder', label: '创建文件夹', icon:'bi bi-folder-plus', event: this.openNewFolder, visible: true },
            { name: 'update_folder', label: '修改文件夹', icon:'bi bi-pencil-square', event: this.openUpdateFolder, visible: true },
            { name: 'move_folder', label: '移动文件夹', icon:'bi bi-folder-symlink', event: this.openMoveFolder, visible: true },
            { name: 'del_folder', label: '删除文件夹', icon:'bi bi-folder-x', event: this.delFolder, visible: true },
            { name: 'upload', label: '上传文件', icon:'bi bi-cloud-upload', event: this.openUploadDlg, visible: true },
            { name: 'move_file', label: '移动文件', icon:'bi bi-arrows-move', event: this.openMoveFile, visible: true },
            { name: 'del_file', label: '删除文件', icon:'bi bi-trash', event: this.delFile, visible: true },
            { name: 'remark_file', label: '备注', icon:'bi bi-journal-bookmark-fill', event: this.openRemarkDlg, visible: true },
        ]

        this.config.tool.forEach((item:AnyObject) => {
            item.visible = init_config.tool_config.value.indexOf(item.name) != -1;
        })

        //复制文件链接
        onMounted(() => {
            const conf = this.config
            const clipboard = new ClipboardJS('#copy-file-link',{
                text() {
                    const arr:Array<string> = []
                    if(conf.file.select_files != null){
                        conf.file.select_files.forEach((item:AnyObject)=>{
                            arr.push(item.url)
                        })
                    }
                    return arr.join(',')
                }
            })
            clipboard.on('success', () => {
                ElMessage.success('已成功复制到剪贴版')
            })
            clipboard.on('error', () => {
                if(conf.file.select_files == null){
                    ElMessage.error('请选择要复制链接的文件')
                }else{
                    ElMessage.error('文件链接复制失败')
                }
            })
        })

    }

    /**
     * 获取配置参数并导出
     */
    getConfig = (): ToRefs => {
        return toRefs(this.config)
    }

    /**
     * 左侧菜单展开与折叠
     */
    collapse = ():void => {
        if(!this.config.is_collapse){
            this.config.folder_list_width = '46px'
            this.config.is_collapse = true
            this.config.folder_display = 'display: none'
            this.config.collapsePadding = 'padding-left: 4px;'
        }else{
            this.config.folder_list_width = '220px'
            this.config.is_collapse = false
            this.config.folder_display = 'display: inline-block;'
            this.config.collapsePadding = 'padding-left: 10px;'
        }
    }

    /**
     * 文件列表展示方式切换
     * @param type
     */
    changeListShow = (type:string):void => {
        this.config.file.list_show = type
        this.resetTableRowSelect()
    }

    /**
     * 重置列表文件选择状态
     */
    resetTableRowSelect = ():void => {
        if(this.config.file.select_files == null){
            return
        }
        setTimeout(() => {
            this.config.file.select_files.forEach((item:AnyObject)=>{
                if(this.config.file.tableRef != null) this.config.file.tableRef.toggleRowSelection(item, true)
            })
        }, 200)
    }

    /**
     * 创建文件夹
     */
    openNewFolder = ():void => {
        this.config.folder.is_new = true
        this.config.folder.folder_dlg = true
        this.config.folder.folder_dlg_title = '创建文件夹'
        this.config.folder.folder_name = ''
        this.config.folder.folder_value = 0
        this.config.folder.current_pid = 0
    }

    /**
     * 打开修改文件夹弹窗
     */
    openUpdateFolder = ():void => {
        if(this.config.folder.current_select == null){
            ElMessage.error('请先选择文件夹！')
            return
        }
        this.config.folder.is_new = false
        this.config.folder.folder_name = this.config.folder.current_select.title
        this.config.folder.folder_dlg = true
        this.config.folder.folder_dlg_title = '修改文件夹'
        this.config.folder.folder_value = this.config.folder.current_select.id
    }

    /**
     * 修改父级
     * @param selectRow
     */
    changeFolderParent = (selectRow: AnyObject):void => {
        setTimeout(() => {
            this.config.folder.current_pid = selectRow.id
        },100)

    }

    /**
     * 修改移动到父级
     * @param selectRow
     */
    changeMoveFolderParent = (selectRow: AnyObject):void => {
        setTimeout(() => {
            this.config.folder.move_pid = selectRow.id
        },100)
    }

    /**
     * 选择上传文件夹
     * @param selectRow
     */
    changeUploadFolder = (selectRow: AnyObject):void => {
        this.config.file.current_folder_id = selectRow.id
    }


    /**
     * 保存文件夹
     */


    saveFolder = ():void => {
        this.config.folder.folder_dlg=false;
        this.emit('saveFolder', {
            is_new:this.config.folder.is_new,
            folder_name: this.config.folder.folder_name,
            folder_value: this.config.folder.folder_value,
            folder_pid: this.config.folder.current_pid,
            loadFolder: () => {
                this.emit('loadFolder', this.config.folder)
                this.emit('loadFile', this.config.file)
            }
        }
        );
    }

    /**
     * 打开移动文件夹弹窗
     */
    openMoveFolder = ():void => {
        if(this.config.folder.current_select == null){
            ElMessage.error('请先选择文件夹！')
            return
        }
        if(this.config.folder.data == null){
            ElMessage.error('文件夹列表为空！')
            return
        }
        this.config.folder.move_folder_dlg = true
        this.config.folder.move_pid = 0
    }

    /**
     * 执行移动文件夹操作
     */
    moveFolder = ():void => {
        this.emit('moveFolder', {
            current_id: this.config.folder.current_select.id,
            target_pid: this.config.folder.move_pid,
            loadFolder: () => {
                this.emit('loadFolder', this.config.folder)
                this.emit('loadFile', this.config.file)
            }
        })


        this.config.folder.move_folder_dlg = false
    }

    /**
     * 删除文件夹
     */
    delFolder = ():void => {
        if(this.config.folder.current_select == null){
            ElMessage.error('请先选择文件夹！')
            return
        }

        ElMessageBox.confirm(
            '文件夹及其下面的所有文件将被删除且不可恢复，确定要执行此操作?',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        ).then(() => {
            this.emit('delFolder', {
                id:this.config.folder.current_select.id,
                loadFolder: () => {
                    this.emit('loadFolder', this.config.folder)
                    this.config.file.filter = { dir_id: this.config.folder.current_select.pid }
                    this.emit('loadFile', this.config.file)
                    this.config.folder.current_select = null
                }
            })
        }).catch(() => {
            console.log('delete cancel')
        })
    }

    /**
     * 筛选文件夹列表
     */
    searchFolder = (val: string):void => {
        console.log(val)
        this.config.folder.keywords = val
        this.emit('loadFolder', this.config.folder)
    }


    static judgeFileType(id: any){
        let record = {
            id:id
        }
        return request({
            url: fileApi.localHost + fileApi.judgeFileType,
            method: 'POST',  // Sending POST request
            json: true,  // Assuming you're sending JSON (otherwise remove this)
            data: record,   // Send data in the body
        }).then((res) => {
            if (res.status === 0) {
                console.log("judgeFileType success", res);
                return res;  // Assuming the backend returns the fileType in res.data.fileType
            } else {
                console.error("judgeFileType error", res.status);
                return null;  // If there's an error, return null
            }
        }).catch((error) => {
            console.error("Error in judgeFileType:", error);
            return null;  // Return null in case of an error
        });
    }

    /**
     * 选择文件夹时，重新加载右边文件列表
     * @param nodeData
     * @param nodeObj
     */
    changeFolder = (nodeData: AnyObject, nodeObj: AnyObject):void => {
        this.config.folder.current_select = nodeData
        this.config.folder.current_select_key = nodeData.id
        this.config.folder.current_pid = nodeData.pid
        //重载右边的文件列表
        this.config.file.filter = { dir_id: nodeData.id }
        this.searchFile()
        if(!this.config.folder.keywords){
            try {
                console.log(nodeData.id)
                Service.judgeFileType(nodeData.id).then((res) => {
                    if (res) {
                        let fileType=res.data[0]
                        this.config.file.path = '/' + this.config.folder.searchData[-fileType].title
                        if(nodeObj.level > 1){
                            this.config.file.path = '/' + this.config.folder.searchData[-fileType].title +  '/' + this.getFolderPath(nodeData.title, nodeObj)
                        }
                    } else {
                    }
                });
            } catch (err) {
                ElMessage({
                    type: 'warning',
                    message: err.message
                })
            }
        }
        else{
            try {
                Service.judgeFileType(nodeData.id).then((res) => {
                    if (res) {
                        let fileType=res.data[0]
                        this.config.file.path = '/' + this.config.folder.searchData[-fileType].title
                        if(nodeData.pid != -0&&nodeData.pid != -1&&nodeData.pid != -2){
                            this.config.file.path = '/' + this.config.folder.searchData[-fileType].title +  '/' + this.getSearchFolderPath(nodeData.title, nodeData.pid)
                        }
                    } else {
                    }
                });
            } catch (err) {
                ElMessage({
                    type: 'warning',
                    message: err.message
                })
            }
        }
    }

    /**
     * 获取文件夹层级路径
     * @param path
     * @param nodeObj
     */
    getFolderPath = (path:string, nodeObj: AnyObject):string => {
        if(nodeObj.data.pid != 0&&nodeObj.data.pid != -1&&nodeObj.data.pid != -2){
            path = this.getFolderPath(nodeObj.parent.data.title + '/' +  path, nodeObj.parent)
        }
        return path
    }

     searchById= (nodes:AnyObject, id: number):AnyObject => {
        for (const node of nodes) {
            if (node.id === id) {
                return node;  // 找到匹配的节点，返回该节点
            }
            // 如果当前节点有子节点，则递归搜索子节点
            if (node.children) {
                const result = this.searchById(node.children, id);
                if (result) {
                    return result;  // 如果在子节点中找到匹配节点，返回
                }
            }
        }
        return null;  // 如果没有找到，返回 null
    }

    getSearchFolderPath = (path:string, pid: number):string => {
        let item=this.searchById(this.config.folder.searchData,pid)
        if(item.id != -0&&item.id != -1&&item.id != -2){
                       path = this.getSearchFolderPath(item.title + '/' +  path, item.pid)
                   }
        return path
    }

    /**
     * 搜索文件
     */
    searchFile = ():void => {
        this.emit('loadFolder',this.config.folder)
        this.emit('loadFile', this.config.file)
    }






    /**
     * 每页显示条数修改
     * @param size 每页显示的条数
     */
        // 直接修改嵌套对象的属性可能会出现问题
    handleSizeChange = (size: number): void => {
        console.log("handleSizeChange " + size);
        this.config.file = {
            ...this.config.file,  // 创建一个新的对象，保持其他属性不变
            page_size: size,      // 修改 page_size 属性
        };
        this.searchFile();
    };


    /**
     * 当前页修改
     * @param page_num 页码
     */
    handleCurrentChange = (page_num: number):void => {
        this.config.file.current_page = page_num
        this.searchFile()
    }

    /**
     * 列表选择文件
     * @param files
     */
    tableSelectionChange = (files: AnyObject):void => {
        this.config.file.select_files = files
        this.emit('selectFile', files)
    }

    /**
     * 点击缩略图
     * @param select_file
     */
    clickCard = (select_file:AnyObject):void => {
        const tmp = []
        let flag = true
        if(this.config.file.select_files == null){
            tmp.push(select_file)
            this.config.file.select_files = tmp
            this.emit('selectFile', tmp)
            return
        }
        this.config.file.select_files.forEach((item:AnyObject)=>{
            if(item.id === select_file.id){
                flag = false
            }else{
                tmp.push(item)
            }
        })

        if(flag) tmp.push(select_file)
        this.config.file.select_files = tmp
        this.emit('selectFile', tmp)
    }

    /**
     * 判断当前文件是否选中
     * @param current_file
     */
    checkFileSelect = (current_file:AnyObject):string => {
        let res = 'card-container'
        if(this.config.file.select_files == null){
            return res
        }
        this.config.file.select_files.forEach((item:AnyObject)=>{
            if(item.id === current_file.id){
                res = 'card-container-select'
                return res
            }
        })
        return res
    }

    /**
     * 修改文件移动到父级
     * @param selectRow
     */
    changeMoveFileParent = (selectRow: AnyObject):void => {
        setTimeout(() => {
            this.config.file.move_pid = selectRow.id
        },100)
    }

    /**
     * 打开上传文件弹窗
     */
    openUploadDlg = ():void => {
        if(this.config.file.uploadInstance != null){
            this.config.file.uploadInstance.clearFiles()
        }
        this.config.file.upload_file_dlg = true
    }

    /**
     * 执行上传操作
     */
    submitUpload = (uploadInstance:UploadInstance):void => {
        this.config.file.uploadInstance = uploadInstance
        console.log("开始上传")
        uploadInstance.submit()
    }

    /**
     * 关闭上传文件窗口
     */
    closeUploadDlg = ():void => {
        this.emit('loadFile', this.config.file)
    }

    /**
     * 打开移动文件弹窗
     */
    openMoveFile = ():void => {
        if(this.config.file.select_files == null || this.config.file.select_files.length == 0){
            ElMessage.error('请先选择文件！')
            return
        }
        if(this.config.folder.data == null){
            ElMessage.error('文件夹列表为空！')
            return
        }
        this.config.file.move_file_dlg = true
        this.config.file.move_pid = 0
    }

    /**
     * 执行移动文件
     */
    moveFile = ():void => {
        const file_id:Array<number> = []
        const file_before_dir_id=this.config.file.select_files[0].dir_id
        this.config.file.select_files.forEach((item:AnyObject)=>{
            file_id.push(item.id)

        })


        this.emit('moveFile', {
            select_file_id: file_id,
            select_file_before_dir_id:file_before_dir_id,
            target_pid: this.config.file.move_pid,
            loadFile: () => {
                this.emit('loadFile', this.config.file)
            }
        })
        this.config.file.move_file_dlg = false
    }

    /**
     * 打开备注弹窗
     */
    openRemarkDlg = ():void => {
        if(this.config.file.select_files == null || this.config.file.select_files.length == 0){
            ElMessage.error('请先选择文件！')
            return
        }
        if(this.config.folder.data == null){
            ElMessage.error('文件夹列表为空！')
            return
        }
        this.config.file.remark_file_dlg = true
        this.config.file.remark_content = ''
    }

    /**
     * 备注文件
     */
    remarkFile = ():void => {
        const file_id:Array<number> = []
        const beforeDirId=this.config.file.select_files[0].dir_id
        this.config.file.select_files.forEach((item:AnyObject)=>{
            file_id.push(item.id)
        })

        this.emit('remarkFile', {
            select_file_id: file_id,
            select_file_before_dir_id:beforeDirId,
            remark_content: this.config.file.remark_content,
            loadFile: () => {
                this.emit('loadFile', this.config.file)
            }
        })
        this.config.file.remark_file_dlg = false
    }

    /**
     * 删除文件
     */
    delFile = ():void => {
        if(this.config.file.select_files == null || this.config.file.select_files.length == 0){
            ElMessage.error('请先选择文件！')
            return
        }

        ElMessageBox.confirm(
            '文件删除后不可恢复，确定要执行此操作?',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        ).then(() => {
            const file_id:Array<number> = []
            const beforeDirId=this.config.file.select_files[0].dir_id
            this.config.file.select_files.forEach((item:AnyObject)=>{
                file_id.push(item.id)
            })

            this.emit('delFile', {
                select_file_id: file_id,
                select_file_before_dir_id:beforeDirId,
                loadFile: () => {
                    this.emit('loadFile', this.config.file)
                }
            })
        }).catch(() => {
            console.log('delete cancel')
        })
    }

    /**
     * 编辑文件
     * @param fileObj
     */
    editFile = (fileObj:AnyObject):void => {
        this.config.file.current_input_file = fileObj
    }

    /**
     * 保存文件
     */
    saveFile = ():void => {
        this.emit('saveFile',this.config.file.current_input_file)
        this.config.file.current_input_file = null
    }

    /**
     * 上传文件成功回调
     * @param response
     * @param uploadFile
     * @param uploadFiles
     */
    onUploadSuccess = (response: AnyObject, uploadFile: UploadFile, uploadFiles: UploadFiles):void => {
        if(response.status===-1){
            ElMessage.error('您的权限不够，无法在公司共享文件夹上传文件')
        }else{
            this.emit('onUploadSuccess',{response:response, uploadFile: uploadFile, uploadFiles: uploadFiles, uploadInstance: this.config.file.uploadInstance})
        }
    }

    /**
     * 上传文件失败回调
     * @param error
     * @param uploadFile
     * @param uploadFiles
     */
    onUploadError = (error: Error, uploadFile: UploadFile, uploadFiles: UploadFiles):void => {
        this.emit('onUploadError',{error: error, uploadFile: uploadFile, uploadFiles: uploadFiles, uploadInstance: this.config.file.uploadInstance})
    }

    /**
     * 上传文件之前的钩子，参数为上传的文件， 若返回false或者返回 Promise 且被 reject，则停止上传。
     * @param rawFile
     */
    beforeUpload = (rawFile: UploadRawFile):void => {
        this.emit('beforeUpload', {rawFile:rawFile, uploadInstance: this.config.file.uploadInstance})
    }

    /**
     * 点击文件列表中已上传的文件时的钩子
     * @param uploadFile
     */
    onPreview = (uploadFile: UploadFile):void => {
        this.emit('onPreview', {uploadFile:uploadFile, uploadInstance: this.config.file.uploadInstance})
    }

    /**
     * 文件列表移除文件时的钩子
     * @param uploadFile
     * @param uploadFiles
     */
    onRemove = (uploadFile: UploadFile, uploadFiles: UploadFiles):void => {
        this.emit('onRemove', {uploadFile:uploadFile, uploadFiles:uploadFiles, uploadInstance: this.config.file.uploadInstance})
    }

    /**
     * 文件上传时的钩子
     * @param evt
     * @param uploadFile
     * @param uploadFiles
     */
    onProgress = (evt: UploadProgressEvent, uploadFile: UploadFile, uploadFiles: UploadFiles):void => {
        this.emit('onProgress', {evt: evt, uploadFile:uploadFile, uploadFiles:uploadFiles, uploadInstance: this.config.file.uploadInstance})
    }

    /**
     * 文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
     * @param uploadFile
     * @param uploadFiles
     */
    onChange = (uploadFile: UploadFile, uploadFiles: UploadFiles):void => {
        this.emit('onChange', {uploadFile:uploadFile, uploadFiles:uploadFiles, uploadInstance: this.config.file.uploadInstance})
    }

    /**
     * 当超出限制时，执行的钩子函数
     * @param files
     * @param uploadFiles
     */
    onExceed = (files: File[], uploadFiles: UploadUserFile[]):void => {
        this.emit('onExceed',{files:files, uploadFiles: uploadFiles, uploadInstance: this.config.file.uploadInstance})
    }




    /**
     * 格式化显示文件大小
     * @param size
     */
    formatFileSize = (size: number): string => {
        if(size >= 1024 * 1024 * 1024){
            return Math.round(size / (1024 * 1024 * 1024) * 10) / 10 + "GB"
        }else if(size >= 1024 * 1024){
            return Math.round(size / (1024 * 1024) * 10) / 10 + "MB"
        }else if(size >= 1024){
            return Math.round(size / 1024  * 10) / 10 + "KB"
        }else{
            return size + "字节"
        }
    }

    /**
     * 文件列表排序回调事件
     * @param data
     */
    fileSortChange = (data:AnyObject):void => {
        this.config.file.order_field = data.prop
        this.config.file.order_sort = data.order == 'ascending' ? 'asc' : 'desc'
        this.searchFile()
    }


    handleFilterChange = (filters: AnyObject): void => {
        console.log("filters:", JSON.stringify(filters));
        console.log("filters:", filters);

        // 检查 filters 是否是一个空对象或所有数组都为空
        let extValue = Object.keys(filters).reduce((acc, key) => {
            const value = filters[key];
            if (Array.isArray(value) && value.length > 0) {
                acc = value; // 如果某个字段是非空数组，保存该值
            }
            return acc;
        }, []); // 默认为空数组

        // 如果 extValue 为空数组，则表示没有筛选条件
        if (extValue.length === 0) {
            this.config.file.isExt = false;
            console.log("未进行筛选");
        } else {
            this.config.file.isExt = true;
            this.config.file.extFilter = extValue; // 将筛选条件赋给 extFilter
            console.log("筛选条件:", this.config.file.extFilter);
        }

        // 触发事件，发送筛选后的数据
        this.emit('loadFile', this.config.file);
    };





    // filterStatus方法过滤文件扩展名
    filterStatus = (value, row):AnyObject =>{
        return row.ext === value;
    }




    /**
     * 文件列表排序回调事件
     * @param data
     */


    // handleHttpRequest = (data: any) => {
    //     console.log(data)
    //     const file = data.file;
    //     const formData = new FormData();
    //     formData.append("file", file);
    //     let folder_id=data.data.folder_id
    //     let user=data.data.user
    //     formData.append("folder_id", data.data.folder_id);
    //     formData.append("user", data.data.user);
    //
    //     console.log(formData)
    //     // 发起文件上传请求
    //     fetch('http://localhost:8080/api/file/upload', {
    //         method: 'POST',
    //         body: formData
    //     })
    //         .then(response => response.json())
    //         .then(result => {
    //             console.log("上传成功:", result);
    //             if (result && result.data) {
    //                 // 获取上传成功后的文件 URL
    //                 this.onUploadSuccess(result.data); // 调用成功回调
    //             } else {
    //                 console.error("上传失败");
    //             }
    //         })
    //         .catch(error => {
    //             console.error("上传失败:", error);
    //             this.onUploadError(error);
    //         });
    // }
    //
    // handleHttpRequest1=(data:AnyObject)=>{
    //     console.log(data.file);
    //     let file=data.file;
    //     let record={
    //         uid:file.uid,
    //         lastModified:file.lastModified,
    //         lastModifiedDate:file.lastModifiedDate,
    //         name:file.name,
    //         size:file.size,
    //         type:file.type
    //     }
    //     console.log(record);
    //     //let file=data.file;
    //     //let fileData = new FormData();
    //     //fileData.append("file",file)
    //     this.emit('upload',{record:data})
    // }

}
