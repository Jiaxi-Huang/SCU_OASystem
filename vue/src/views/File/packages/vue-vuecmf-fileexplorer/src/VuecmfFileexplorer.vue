<template>
  <div ref="vuecmf_fileexplorer_ref" class="vuecmf-fileexplorer">
    <el-container>
      <el-header>
        <template v-for="(item,key) in tool">
          <el-button @click="item.event" :key="key" v-if="item.visible"><i :class="item.icon"></i>{{ item.label }}</el-button>
        </template>
        <el-button @click="service.changeListShow('card')"><i class="bi bi-card-image"></i> 缩略图</el-button>
        <el-button @click="service.changeListShow('list')"><i class="bi bi-card-checklist"></i> 列表</el-button>
        <el-button id="copy-file-link"><i class="bi bi-link-45deg"></i> 复制文件链接</el-button>
        <el-button @click="is_help_dlg = true"><i class="bi bi-question-square"></i> 帮助</el-button>
      </el-header>
      <el-container>
        <el-aside :width="folder_list_width">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <el-input :style="folder_display" v-model="filterFolderKeyWord" @input="service.searchFolder" placeholder="请输入关键词" clearable />
                <div class="collapse-folder" :style="collapsePadding" @click.prevent="service.collapse" >
                  <i class="bi bi-arrow-bar-right" title="展开" v-show="is_collapse"></i>
                  <i class="bi bi-arrow-bar-left" title="折叠" v-show="!is_collapse"></i>
                </div>
              </div>
            </template>

            <el-scrollbar :height="scrollbar_height">
              <el-tree
                  ref="folder_tree_ref"
                  node-key="id"
                  :style="folder_display"
                  :data="folder.data"
                  :props="folder.defaultProps"
                  :current-node-key="folder.current_select_key"
                  :default-expand-all="true"
                  :highlight-current="true"
                  @current-change="service.changeFolder"
              />
            </el-scrollbar>

          </el-card>

        </el-aside>
        <el-main>
          <div class="main-top">
            <el-input :model-value="'位置：' + file.path" :readonly="true" >
              <template #append>
                <el-button @click="service.searchFile"><i class="bi bi-arrow-repeat"></i></el-button>
              </template>
            </el-input>
            <el-input
                class="file-search"
                v-model="file.keywords"
                placeholder="请输入文件名关键词"
                clearable
            >
              <template #append>
                <el-button @click="service.searchFile"><i class="bi bi-search"></i></el-button>
              </template>
            </el-input>
          </div>

          <template v-if="file.list_show === 'card'">
            <el-row :gutter="10">
              <template :key="'card_' + index" v-for="(item,index) in file.data">
                <el-col :xs="12" :sm="8" :md="6" :lg="4" :xl="3" class="card-warpper">
                  <el-card  :class="service.checkFileSelect(item)" shadow="hover" @click="service.clickCard(item)">
                    <template v-if="['png','jpg','jpeg','gif','bmp','ico'].indexOf(item.ext.toLowerCase()) != -1">
                      <img :src="item.url" class="card-img-top" :alt="item.file_name">
                    </template>
                    <template v-else>
                      <div class="file-icon">
                        <i class="bi bi-file-earmark-pdf" v-if="item.ext.toLowerCase() == 'pdf'"></i>
                        <i class="bi bi-file-earmark-text" v-else-if="item.ext.toLowerCase() == 'txt'"></i>
                        <i class="bi bi-file-earmark-word" v-else-if="['doc','docx'].indexOf(item.ext.toLowerCase()) != -1"></i>
                        <i class="bi bi-file-earmark-zip" v-else-if="['zip','rar'].indexOf(item.ext.toLowerCase()) != -1"></i>
                        <i class="bi bi-file-earmark-spreadsheet" v-else-if="['xls','xlsx'].indexOf(item.ext.toLowerCase()) != -1"></i>
                        <i class="bi bi-file-earmark" v-else></i>
                      </div>
                    </template>

                    <div class="card-body">
                      <div class="card-title">
                        <div @dblclick="service.editFile(item)" v-if="file.current_input_file == null || file.current_input_file.id != item.id">{{ item.file_name }}</div>
                        <el-input
                            v-model="item.file_name"
                            clearable
                            size="small"
                            v-if="file.current_input_file != null && file.current_input_file.id == item.id"
                        >
                          <template #append>
                            <el-button @click="service.saveFile" size="small">保存</el-button>
                          </template>
                        </el-input>
                      </div>
                      <div class="card-text">
                        {{ item.update_time }}<br>
                        {{ service.formatFileSize(item.size) }}
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </template>
            </el-row>
          </template>
          <template v-else>
            <el-table
                ref="tableRef"
                :data="file.data"
                :default-sort="{ prop: file.order_field.value, order: file.order_sort.value === 'desc' ? 'descending': 'ascending' }"
                style="width: 100%"
                :height="file.table_height"
                size="small"
                @selection-change="service.tableSelectionChange"
                @sort-change="service.fileSortChange"
            >
              <el-table-column type="selection" width="55" />
              <el-table-column prop="file_name" label="文件名" sortable min-width="200">
                <template #default="scope">
                  <div @dblclick="service.editFile(scope.row)" v-if="file.current_input_file == null || file.current_input_file.id != scope.row.id">{{ scope.row.file_name }}</div>
                  <el-input
                      v-model="scope.row.file_name"
                      clearable
                      size="small"
                      v-if="file.current_input_file != null && file.current_input_file.id == scope.row.id"
                  >
                    <template #append>
                      <el-button @click="service.saveFile" size="small">保存</el-button>
                    </template>
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column prop="update_time" label="修改时间" sortable min-width="150" />
              <el-table-column prop="ext" label="扩展名" sortable min-width="100" />
              <el-table-column prop="size" label="大小" sortable min-width="100" >
                <template #default="scope">
                  {{ service.formatFileSize(scope.row.size) }}
                </template>
              </el-table-column>
              <el-table-column prop="remark" label="备注" min-width="100" />
              <el-table-column label="操作"  min-width="60">
                <template #default="scope">
                  <el-link :href="scope.row.url" type="primary"  target="_blank">查看</el-link>
                </template>
              </el-table-column>
            </el-table>
          </template>

          <div class="pagination">
            <el-pagination
                @size-change="service.handleSizeChange"
                @current-change="service.handleCurrentChange"
                :current-page="file.current_page"
                :page-sizes="[5,10,20,30,40,50,100,200,300,500]"
                :page-size="file.page_size"
                :layout="file.page_layout"
                :pager-count="5"
                :total="file.total">
            </el-pagination>
          </div>

        </el-main>
      </el-container>
    </el-container>
  </div>

  <!-- 创建/修改文件夹 -->
  <el-dialog
      v-model="folder.folder_dlg"
      :title="folder.folder_dlg_title"
      width="40%"
  >
    <el-form>
      <el-form-item label="父级：" v-if="folder.is_new">
        <el-tree-select
            v-model="folder.current_pid"
            :props="folder.defaultProps"
            :default-expand-all="true"
            :data="folder.data"
            @current-change="service.changeFolderParent"
            node-key="id"
            clearable
            check-strictly />
      </el-form-item>
      <el-form-item label="名称：">
        <el-input v-model="folder.folder_name" placeholder="请输入文件夹名称" clearable />
      </el-form-item>

    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="folder.folder_dlg = false">取消</el-button>
        <el-button type="primary" @click="service.saveFolder">保存</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 移动文件夹-->
  <el-dialog
      v-model="folder.move_folder_dlg"
      title="移动文件夹"
      width="40%"
  >
    <el-form>
      <el-form-item label="移动到：" >
        <el-tree-select
            v-model="folder.move_pid"
            :props="folder.defaultProps"
            :default-expand-all="true"
            :data="folder.data"
            @current-change="service.changeMoveFolderParent"
            node-key="id"
            clearable
            check-strictly />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="folder.move_folder_dlg = false">取消</el-button>
        <el-button type="primary" @click="service.moveFolder">保存</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 移动文件-->
  <el-dialog
      v-model="file.move_file_dlg"
      title="移动文件"
      width="40%"
  >
    <el-form>
      <el-form-item label="移动到：" >
        <el-tree-select
            v-model="file.move_pid"
            :props="folder.defaultProps"
            :default-expand-all="true"
            :data="folder.data"
            @current-change="service.changeMoveFileParent"
            node-key="id"
            clearable
            check-strictly />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="file.move_file_dlg = false">取消</el-button>
        <el-button type="primary" @click="service.moveFile">保存</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 备注文件-->
  <el-dialog
      v-model="file.remark_file_dlg"
      title="备注文件"
      width="40%"
  >
    <el-form>
      <el-form-item label="内容：" >
        <el-input
            v-model="file.remark_content"
            :rows="2"
            type="textarea"
            placeholder="请输入备注内容"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="file.remark_file_dlg = false">取消</el-button>
        <el-button type="primary" @click="service.remarkFile">保存</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 上传文件 -->
  <el-dialog
      v-model="file.upload_file_dlg"
      title="上传文件"
      width="50%"
      @closed="service.closeUploadDlg"
  >
    <el-form>
      <el-form-item label="所在目录：" >
        <el-tree-select
            v-model="file.current_folder_id"
            :props="folder.defaultProps"
            :default-expand-all="true"
            :data="folder.data"
            @current-change="service.changeUploadFolder"
            node-key="id"
            clearable
            check-strictly />
      </el-form-item>
      <el-form-item label="上传文件：" >
        <el-upload
            ref="uploadRef"
            :http-request="service.handleHttpRequest"
            :auto-upload="false"
            :multiple="true"
            :headers="headers"
            :data="Object.assign(data,{folder_id: file.current_folder_id})"
            :on-success="service.onUploadSuccess"
            :on-error="service.onUploadError"
            :before-upload="service.beforeUpload"
            :on-preview="service.onPreview"
            :on-remove="service.onRemove"
            :on-progress="service.onProgress"
            :on-change="service.onChange"
            :on-exceed="service.onExceed"
        >
          <template #trigger>
            <div class="select-upload-btn"><el-button type="primary">选择文件</el-button></div>
          </template>
          <el-button class="start-upload-btn" type="success" @click="service.submitUpload(uploadRef)">
            开始上传
          </el-button>
          <template #tip>
            <div class="el-upload__tip">
              支持多个文件同时上传，点击“选择文件”后，按住Ctrl+鼠标单击或鼠标框选进行多文件选择。
            </div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- 帮助 -->
  <el-dialog
      v-model="is_help_dlg"
      title="帮助"
      width="50%"
  >


    <div class="help-doc"><strong>使用说明：</strong></div>
    <ul>
      <li>文件夹相关操作
        <p>在工具条中点击对应文件夹操作按钮即可，其中修改、移动及删除操作需要先在左边的文件夹列表中选择要操作的文件夹名称，再点击相应按钮。</p>
      </li>
      <li>文件相关操作
        <p>在工具条中点击对应文件操作按钮即可，其中移动及删除操作需要先在文件列表中选择要操作的文件，再点击相应按钮。</p>
        <p>文件列表在缩略图和列表展示方式下，都支持双击文件名进行文件名修改。</p>
      </li>
    </ul>
    <template #footer>
        <el-button @click="is_help_dlg = false">关闭</el-button>
    </template>
  </el-dialog>



</template>

<script lang="ts" setup>
import Service from './Service'
import {defineEmits, toRefs, ref} from "vue"
import {ElTable, UploadInstance} from "element-plus";
const emit = defineEmits(['loadFolder','saveFolder','moveFolder','delFolder','loadFile','uploadFile','saveFile','moveFile','delFile','selectFile', 'onUploadSuccess', 'onUploadError','beforeUpload', 'onPreview', 'onRemove','onProgress','onChange', 'onExceed','remarkFile','upload'])
const props = defineProps({
  //当前文件夹根目录
  root_path: {
    type: String,
    default: 'uploads'
  },
  //每页显示条数
  page_size: {
    type: Number,
    default: 30,
  },
  //文件列表展示方式
  list_show: {
    type: String,
    default: 'card'
  },
  //上传文件后端API
  upload_api: {
    type: String,
    default: ''
  },
  //工具条配置
  tool_config:{
    type: Array,
    default: String['new_folder','update_folder','move_folder','del_folder','upload','move_file','del_file','remark_file']
  },
  //设置上传的请求头部
  headers: {
    type: Object,
    default: null
  },
  //上传时附带的额外参数
  data: {
    type: Object,
    default: null
  }

})

const {root_path, page_size, list_show, upload_api, tool_config} = toRefs(props)
const uploadRef = ref<UploadInstance>()
const tableRef = ref<InstanceType<typeof ElTable>>()

//实例化服务类
const service = new Service({
  root_path: root_path,
  page_size: page_size,
  list_show: list_show,
  tableRef: tableRef,
  tool_config: tool_config
},emit)

const {
  vuecmf_fileexplorer_ref,
  folder_list_width,
  folder_display,
  filterFolderKeyWord,
  collapsePadding,
  is_collapse,
  scrollbar_height,
  folder_tree_ref,
  tool,
  is_help_dlg,
  folder,
  file,
} = service.getConfig()

</script>

<script lang="ts" >
import { defineComponent } from 'vue'
import "bootstrap-icons/font/bootstrap-icons.css"
import { ElTreeSelect,ElContainer,ElHeader,ElMain,ElTree,ElCard,ElAside,ElLink,ElDialog, ElScrollbar } from 'element-plus'

export default defineComponent({
  name: 'vuecmf-fileexplorer',
  components: {ElTreeSelect, ElContainer,ElHeader,ElMain,ElTree,ElCard,ElAside,ElLink,ElDialog, ElScrollbar }
});
</script>

<style lang="scss" >
.start-upload-btn{  margin-left: 10px;}
.select-upload-btn{ padding-top: 2px;}
.text-center{ text-align: center; }
.help-doc{ padding-top: 15px;}
.about-title{
  text-align: center;
  div{ font-size: 12px;}
}
.vuecmf-fileexplorer{

  /* 工具条 */
  .el-header{
    --el-header-height:auto !important;
    background-color: #f7f8f9;
    padding: 5px 0;
    .el-button{ margin:3px 6px 3px 0; padding: 8px 9px; }
    i {
      font-size: 16px; margin-right: 4px;
    }
  }

  /* 左边文件夹列表 */
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .el-tree { width: 100%; }
  .collapse-folder{
    cursor: pointer;
  }
  .box-card{
    .el-card__header{
      padding: 9px;
    }
    .el-card__body {
      padding: 6px 0 0 0;
    }
  }

  /* 列表 */
  .el-main {
    --el-main-padding: 0 10px 0 10px; background-color: #f7f8f9;
  }
  .main-top{
    display: flex;
    padding-bottom: 6px;
    .file-search{ max-width: 220px;}
    .el-input+.el-input{ margin-left: 6px;}
    i{ font-size: 16px;}
    .el-button{ padding: 8px; }
  }

  .el-button--small{ padding: 3px;}

  /*缩略图*/
  .card-img-top{ width: 100%; }
  .card-warpper{ padding: 5px; min-width: 6rem;}
  .card-container,.card-container-select{
    cursor: pointer;
    .el-card__body {
      padding: 8px;
    }
    .card-title{
      font-size: 12px; padding: 5px 0;
    }
    .card-text{
      font-size: 12px; padding-bottom: 5px;
    }
  }
  /* 缩略图默认状态 */
  .card-container{
    .card-title{
      color: #000000;
    }
    .card-text{
      color: #7b7d83;
    }
  }
  /* 缩略图选中状态 */
  .card-container-select{
    background-color: #409EFF;
    .card-title, .card-text, i{
      color: #ffffff;
    }
  }

  .file-icon{
    text-align: center;
    i{
      font-size: 4rem;
    }
  }

  .pagination {
    display: flex;
    justify-content: center;
    margin: 10px auto;
  }

}


</style>
