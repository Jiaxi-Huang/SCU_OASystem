<template>
  <div>
    <el-card class="card-ctrl">
      <el-row>
        <el-col :span="10" style="text-align: left">
          <el-button type="info" size="medium" @click="onExport"><el-icon><download /></el-icon>导出操作日志</el-button>
          <el-button type="info" size="medium" @click="onPrint"><el-icon><printer /></el-icon>打印操作日志</el-button>
        </el-col>
        <el-col :span="14" style="text-align: right">
          <el-input
              v-model="searchKeyword"
              placeholder="请输入关键词"            style="width: 200px; margin-right: 10px"
          ></el-input>
        </el-col>
      </el-row>
      <br />
      <el-table v-loading="loading" :data="displayData()" stripe class="table" @selection-change="handleSelectionChange">
        <el-table-column prop="id" label="操作ID" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="userId" label="用户ID" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="logContent" label="操作内容" align="center" sortable @sort-change="handleSortChange"></el-table-column>
        <el-table-column prop="logDate" label="操作时间" align="center" sortable @sort-change="handleSortChange"></el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          :current-page="param.page"
          :page-size="param.size"
          layout="sizes,prev,pager,next,total"
          :page-sizes="[10, 20, 50, 100]"
          :total="param.total"
          background
          @current-change="onCurrentChange"
          @size-change="onSizeChange"
        >
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive, toRefs, computed, onMounted, watch} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {Download, Edit, Minus, Plus, Printer, Refresh} from '@element-plus/icons-vue'
import Service from './api/index'

export default defineComponent({
  name: 'LogManage',
  components: {
    Printer,
    Download,
    Edit,
    Minus,
    Plus,
    Refresh
  },
  setup() {
    const state = reactive({
      url: {
        r: '/log/list',
        d: '/log/delete'
      },
      param: {
        total: 0,
        size: 10,
        page: 1
      },
      data: [
        //{ userName: '超级管理员', userDepartment:'',userRole:'',userPhone:''},
      ],
      filteredData: [],
      loading: false,
      is_search: false,
      add_visible: false,
      edit_visible: false,
      detail_visible: false,
      posted: {
        userRow: {
          id: null,
          userId: null,
          logContent: '',
          logDate: ''
        }
      },
      sortField : '',
      sortOrder : '',
      ids: [] as { id: number }[],
      selectionRows: [] as { id: number }[], //
      searchKeyword: '' // 添加 searchKeyword 变量
    })
    /**
     * @description 对数据进行排序
     */
    const sortData = () => {
      if (!state.sortField) return state.data;
      return [...state.data].sort((a, b) => {
        const fieldA = a[state.sortField];
        const fieldB = b[state.sortField];

        if (fieldA < fieldB) return state.sortOrder === 'asc' ? -1 : 1;
        if (fieldA > fieldB) return state.sortOrder === 'asc' ? 1 : -1;
        return 0;
      });
    }
    /**
     * @description 选择对应列排序
     */
    const handleSortChange = ({ prop, order }: { prop: string, order: 'ascending' | 'descending' | null }) => {
      state.sortField = prop;
      state.sortOrder = order === 'ascending' ? 'asc' : 'desc';
    }
    /**
     * @description 获取选择行的userId列表
     */
    const handleSelectionChange = (selection:any[]) => {
      state.selectionRows = selection.map(item => item.id)
      console.log("SelectionRows",state.selectionRows)
    }
    /**
     * @description 请求接口获取当前设置角色，默认始终有超级管理员角色
     */
    const fetchData = async () => {
      state.is_search = false
      const data = {'accessToken': sessionStorage.getItem('accessToken')}
      const adminUserInfo = await Service.postAdminQueryLogList(data)
      if (adminUserInfo.status === 0) {
        state.data = adminUserInfo.data
        state.ids = adminUserInfo.data.map((item:any) => item.id)
        state.param.total = state.data.length
      }

    }
    const onCurrentChange = (val: number) => {
      state.param.page = val
    }
    const onSizeChange = (val: number) => {
      state.param.page = 1
      state.param.size = val
    }
    const onCreate = () => {
      state.add_visible = true
    }
    const onCreateSuccess = (val: any) => {
      state.add_visible = false
      fetchData()
    }
    const onEditSuccess = () => {
      state.edit_visible = false
      fetchData()
    }
    const displayData = () => {
      return state.is_search ? sortData().filter(item =>
          item.logContent.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
          item.logDate.toLowerCase().includes(state.searchKeyword.toLowerCase())
      ).slice((state.param.page-1)*state.param.size, state.param.page*state.param.size) : sortData().slice((state.param.page-1)*state.param.size, state.param.page*state.param.size);
    }
    const onSearch = () => {
      if (state.searchKeyword !== '') {
        state.is_search = true
        state.param.page = 1; // 重置页码为第一页
        state.filteredData = state.data.filter(item =>
            item.logContent.toLowerCase().includes(state.searchKeyword.toLowerCase()) ||
            item.logDate.toLowerCase().includes(state.searchKeyword.toLowerCase())
        );
        state.param.total = state.filteredData.length
      }
      else{
        state.is_search = false
        state.param.page = 1; // 重置页码为第一页
        state.param.total = state.data.length
      }
    }
      const useConfirmDelete = async (row: any) => {
        ElMessageBox.confirm('此操作将删除该条操作日志, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
            .then(async () => {
              // 此处执行接口异步删除日志
              const data = {
                id: row.id,
                accessToken: sessionStorage.getItem('accessToken')
              }
              const res = await Service.postAdminDeleteLog(data);
              if (res.status === 0) {
                ElMessage({
                  type: 'success',
                  message: '删除成功'
                })
                fetchData()
              } else {
                ElMessage({
                  type: 'error',
                  message: '删除失败'
                })
              }
            })
            .catch(() => {
              ElMessage({
                type: 'info',
                message: '已取消删除'
              })
            })
      }
      const onDelete = (index: any, row: any) => {
        console.log(index, row)
        useConfirmDelete(row)
      }
    /**
     * @description 导出列表所选行
     */
    const onExport = async() => {
      ElMessageBox.confirm('确定要导出为Excel吗', '温馨提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async() => {
        // 确认后调用获取 PDF 的方法
        const ids = state.selectionRows.length > 0 ? state.selectionRows : state.ids
        const data ={
          'accessToken': sessionStorage.getItem('accessToken'),
          'ids': ids
        }
        Service.postAdminExportLog(data).then(() => {
          ElMessage({
            type: 'success',
            message: 'Excel 文件正在下载...'
          });
        }).catch(() => {
          ElMessage({
            type: 'error',
            message: '下载失败，请重试'
          });
        });
      }).catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消'
        });
      });
    }
    /**
     * @description 打印列表所选行
     */
    const onPrint = async() => {
      ElMessageBox.confirm('确定要下载PDF吗', '温馨提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async() => {
        // 确认后调用获取 PDF 的方法
        const ids = state.selectionRows.length > 0 ? state.selectionRows : state.ids
        const data ={
          'accessToken': sessionStorage.getItem('accessToken'),
          'ids': ids
        }
        Service.postAdminPrintLog(data).then(() => {
          ElMessage({
            type: 'success',
            message: 'PDF 文件正在下载...'
          });
        }).catch(() => {
          ElMessage({
            type: 'error',
            message: '下载失败，请重试'
          });
        });
      }).catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消'
        });
      });
    }
      // 使用 watch 监视 searchKeyword 的变化
      watch(() => state.searchKeyword, (newVal) => {
          onSearch()
      })
      //初始调用
      onMounted(() => {
        fetchData()
        displayData()
      })
      return {
        ...toRefs(state),
        displayData,
        handleSelectionChange,
        onCurrentChange,
        onSizeChange,
        onCreate,
        onCreateSuccess,
        onEditSuccess,
        onDelete,
        onSearch,
        onExport,
        onPrint,
        fetchData,
        handleSortChange
      }
    }
})
</script>
<style lang="stylus" scoped></style>
