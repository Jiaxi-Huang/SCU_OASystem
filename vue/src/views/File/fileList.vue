<template>
  <h3>个人文件管理</h3>

  <vuecmf-fileexplorer
      root_path="个人文件管理"
      :page_size="30"
      list_show="list"
      :tool_config="['new_folder','update_folder','move_folder','del_folder','upload','move_file','del_file','remark_file']"
      upload_api="http://localhost:8080/api/file/upload"

      :data="{user:'2'}"

      @loadFolder="loadFolder"
      @moveFolder="moveFolder"
      @saveFolder="saveFolder"
      @delFolder="delFolder"
      @loadFile="loadFile"
      @selectFile="selectFile"
      @moveFile="moveFile"
      @delFile="delFile"
      @saveFile="saveFile"
      @remarkFile="remarkFile"
      @upload="upload"

      @beforeUpload="beforeUpload"
      @onUploadSuccess="onUploadSuccess"
      @onUploadError="onUploadError"

  >
  </vuecmf-fileexplorer>
  <!--      这个data里的user没用，这里应该传token但还不知道怎么弄-->
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, ref} from 'vue';
import {AnyObject} from "./packages/vue-vuecmf-fileexplorer/src/typings/vuecmf";
import {ElMessage} from "element-plus";
import Service from "@/views/File/api/index";


export default defineComponent({
  name: 'App',
  setup(){
    //加载文件夹列表
    const loadFolder = (folderObj: AnyObject): void => {
      Service.loadFolder().then((res) => {
        if (res) {
          const data = res.data;
          if (data && Array.isArray(data)) {
            let user=data[data.length-1];
            data.pop();
            console.log(data);
            console.log(user);
            //0是个人，1是部门，2是公司，文件夹id是0，-1，-2
            for(let i = 0;i <= 2;i++){
              //folderObj.total = data.length; // 更新文件夹的总数
              folderObj.data[i].id=-i;
              //folderObj.data[0].title="uploads";
              folderObj.data[i].children=[];

              let map = {};
              // 遍历所有数据，创建一个 map，将每个对象按 id 存储
              data.forEach(item => {
                map[item.id] = {...item, children: []};
              });
              data.forEach(item => {
                if(i===0&&((item.department===null)||(item.department===""))&&item.userId===user.userId){
                  console.log("个人")
                  if (item.pid === -i) {
                    // 如果是根节点（pid为0），加入结果数组
                    folderObj.data[i].children.push(map[item.id]);
                  } else {
                    if(map[item.pid]){
                      map[item.pid].children.push(map[item.id]);
                    }
                    // 否则将其添加到对应父节点的 children 数组中
                  }
                }
                if(i===1&&item.department===user.department){
                  console.log("部门")
                  if (item.pid === -i) {
                    // 如果是根节点（pid为0），加入结果数组
                    folderObj.data[i].children.push(map[item.id]);
                  } else {
                    if(map[item.pid]){
                      map[item.pid].children.push(map[item.id]);
                    }
                    // 否则将其添加到对应父节点的 children 数组中
                  }
                }
                if(i===2&&((item.department===null)||(item.department===""))){
                  console.log("公司")
                  if (item.pid === -i) {
                    // 如果是根节点（pid为0），加入结果数组
                    folderObj.data[i].children.push(map[item.id]);
                  } else {
                    if(map[item.pid]){
                      map[item.pid].children.push(map[item.id]);
                    }
                    // 否则将其添加到对应父节点的 children 数组中
                  }
                }
              });
              let is_exist=0;
              if(folderObj.keywords){
                //这部分是搜索还没做
                console.log("进入查询")
                folderObj.data[i].children=[];
                data.forEach(item => {
                  if(item.title === folderObj.keywords){
                    //folderObj.data[0].id=-3;
                    //folderObj.data[0].title="找到了";
                    folderObj.data[i].children.push(map[item.id]);
                    is_exist=1;
                  }
                  if(!is_exist){
                    //folderObj.data[0].id=-3;
                    //folderObj.data[0].title="没找到";
                  }
                });
                console.log(JSON.stringify(folderObj, null, 2));
                console.log(folderObj.is_new);
              }
            }

          }
        } else {
          console.log("getPersonalFolders RES MISS");

        }


      }).catch(err => {
        ElMessage({
          type: 'warning',
          message: err.message || '加载文件失败',
        });
      });
    }





    //加载文件列表
    const loadFile = (folderObj: AnyObject): void => {
      console.log("getPersonalFiles exc")
      let dir_id=0
      if(folderObj.filter.dir_id!=null){
        dir_id = folderObj.filter.dir_id
      }
      console.log("dir_id "+dir_id)
      Service.loadFile(dir_id).then((res) => {
        if (res) {
          // 处理返回的结果
          const data = res.data;
          console.log(data);
          let folderType=data[data.length-2];
          let user=data[data.length-1];
          console.log(folderType)
          console.log(user)
          data.pop()
          data.pop()
          if (data && Array.isArray(data)) {
            folderObj.data=[];


            if(folderType==0){
              //个人
              //folderObj.total = data.length;
              console.log(folderObj.keywords)
              if(folderObj.keywords){
                //keywords是搜索的关键词，这部分是搜索，else是文件加载，后面同理
                for (let i = 0; i < data.length; i++) {
                  if(folderObj.keywords === data[i].fileName)
                    folderObj.data.push({
                      "id": data[i].id,
                      "file_name": data[i].fileName,
                      "ext": data[i].ext,
                      "size": data[i].size,
                      "dir_id": data[i].dirId,
                      "url": data[i].url,
                      "remark": data[i].remark,
                      "create_time": data[i].createTime,
                      "update_time": data[i].updateTime,
                    })
                }
              }else{
                console.log(folderObj.filter.dir_id)
                for (let i = 0; i < data.length; i++) {
                  if(data[i].dirId === folderObj.filter.dir_id
                      &&data[i].userId===user.userId
                      &&((data[i].department==="")||(data[i].department===null))
                  ){
                    folderObj.data.push({
                      "id": data[i].id,
                      "file_name": data[i].fileName,
                      "ext": data[i].ext,
                      "size": data[i].size,
                      "dir_id": data[i].dirId,
                      "url": data[i].url,
                      "remark": data[i].remark,
                      "create_time": data[i].createTime,
                      "update_time": data[i].updateTime,
                    })
                  }
                }
              }
            }

            if(folderType===-1){
              //部门
              //folderObj.total = data.length;
              console.log(folderObj.keywords)
              if(folderObj.keywords){
                for (let i = 0; i < data.length; i++) {
                  if(folderObj.keywords === data[i].fileName)
                    folderObj.data.push({
                      "id": data[i].id,
                      "file_name": data[i].fileName,
                      "ext": data[i].ext,
                      "size": data[i].size,
                      "dir_id": data[i].dirId,
                      "url": data[i].url,
                      "remark": data[i].remark,
                      "create_time": data[i].createTime,
                      "update_time": data[i].updateTime,
                    })
                }
              }else{
                console.log(folderObj.filter.dir_id)
                for (let i = 0; i < data.length; i++) {
                  if(data[i].dirId === folderObj.filter.dir_id
                      &&data[i].department===user.department
                  ){
                    folderObj.data.push({
                      "id": data[i].id,
                      "file_name": data[i].fileName,
                      "ext": data[i].ext,
                      "size": data[i].size,
                      "dir_id": data[i].dirId,
                      "url": data[i].url,
                      "remark": data[i].remark,
                      "create_time": data[i].createTime,
                      "update_time": data[i].updateTime,
                    })
                  }
                }
              }
            }

            if(folderType===-2){
              //公司
              //folderObj.total = data.length;
              console.log(folderObj.keywords)
              if(folderObj.keywords){
                for (let i = 0; i < data.length; i++) {
                  if(folderObj.keywords === data[i].fileName)
                    folderObj.data.push({
                      "id": data[i].id,
                      "file_name": data[i].fileName,
                      "ext": data[i].ext,
                      "size": data[i].size,
                      "dir_id": data[i].dirId,
                      "url": data[i].url,
                      "remark": data[i].remark,
                      "create_time": data[i].createTime,
                      "update_time": data[i].updateTime,
                    })
                }
              }else{
                console.log(folderObj.filter.dir_id)
                for (let i = 0; i < data.length; i++) {
                  if(data[i].dirId === folderObj.filter.dir_id
                      &&((data[i].department==="")||(data[i].department===null))
                  ){
                    folderObj.data.push({
                      "id": data[i].id,
                      "file_name": data[i].fileName,
                      "ext": data[i].ext,
                      "size": data[i].size,
                      "dir_id": data[i].dirId,
                      "url": data[i].url,
                      "remark": data[i].remark,
                      "create_time": data[i].createTime,
                      "update_time": data[i].updateTime,
                    })
                  }
                }
              }
            }
          }
        } else {
          console.log("FileLoader RES MISS");
        }
      }).catch(err => {
        ElMessage({
          type: 'warning',
          message: err.message || '加载文件失败',
        });
      });
    }

    //保存文件夹
    const saveFolder = (folderData: AnyObject):void => {
      //创建
      console.log(folderData.is_new)
      if(folderData.is_new === true){
        // true代表创建
        try {
          Service.createFolder(folderData).then((res) => {
            if (res) {
              // console.log(res)
            } else {
            }
          });
        } catch (err) {
          ElMessage({
            type: 'warning',
            message: err.message
          })
        }
      }else{
        // false代表修改
        try {
          Service.modifyFolder(folderData).then((res) => {
            if (res) {
              // console.log(res)
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
      console.log(folderData)
    }

    //移动文件夹
    const moveFolder = (data:AnyObject):void => {
      console.log(data)
      //重新加载文件夹列表及文件列表
      try {
        Service.moveFolder(data).then((res) => {
          if (res) {
            // console.log(res)
          } else {
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
      data.loadFolder()

    }

    //删除文件夹
    const delFolder = (folderData: AnyObject):void => {
      console.log(folderData)
      try {
        Service.delFolder(folderData).then((res) => {
          if (res) {
            // console.log(res)
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



    //选择文件事件
    const selectFile = (files:AnyObject):void => {
      console.log('当前选择的文件信息：', files)
    }

    //移动文件
    const moveFile = (data:AnyObject):void => {
      console.log(data)
      //重新加载文件列表
      try {
        Service.moveFile(data).then((res) => {
          if (res) {
            // console.log(res)
          } else {
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }

      data.loadFile()
    }

    //删除文件
    const delFile = (data:AnyObject):void => {
      console.log(data)
      //重新加载文件列表

      try {
        Service.delFile(data).then((res) => {
          if (res) {
            // console.log(res)
          } else {
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
      data.loadFile()
    }

    //保存文件
    const saveFile = (data:AnyObject):void => {
      console.log(data)
    }

    //备注文件
    const remarkFile = (data:AnyObject):void => {
      console.log(data)
      //重新加载文件列表
      try {
        Service.remarkFile(data).then((res) => {
          if (res) {
            // console.log(res)
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




    //上传文件前
    const beforeUpload = (data:AnyObject):void => {
      console.log("before upload: ",data)
      console.log(data.folder_id)
    }

    //上传文件成功返回数据时
    const onUploadSuccess = (data:AnyObject):void => {
      console.log('success = ',data)
    }

    //上传文件失败
    const onUploadError = (data:AnyObject):void => {
      console.log('error = ', data)
    }

    const upload =(data:AnyObject):void=>{
      try {
        Service.uploadFile(data).then((res) => {
          if (res) {
            // console.log(res)
          } else {
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
      console.log(data)
    }
    return {
      loadFolder,
      saveFolder,
      moveFolder,
      delFolder,
      loadFile,
      selectFile,
      moveFile,
      delFile,
      saveFile,
      remarkFile,
      upload,

      beforeUpload,
      onUploadSuccess,
      onUploadError

    }
  }
});
</script>
