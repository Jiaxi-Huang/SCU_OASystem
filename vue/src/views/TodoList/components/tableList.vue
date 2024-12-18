<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
      <el-form-item>
        <el-button type="primary" @click="onAddTodo" :icon="CirclePlus">添加待办事项</el-button>
      </el-form-item>
      <el-form-item>
        <el-popconfirm confirm-button-text="确定" cancel-button-text="取消"  icon-color="red" title="确定删除该条记录吗？" @confirm="onDeleteMulti">
          <template #reference>
            <el-button type="danger" :icon="Delete">批量删除待办事项</el-button>
          </template>
        </el-popconfirm>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onDownload" :icon="Download">打印</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onToExcel" :icon="Download">导出</el-button>
      </el-form-item>
      <el-form-item label="按" style="width: 180px">
        <el-select v-model="search_field" placeholder="无" style="width: 180px">
          <el-option label="无" value="none"></el-option>
          <el-option label="待办事项名称" value="todo_title"></el-option>
          <el-option label="待办事项内容" value="todo_ctnt"></el-option>
          <el-option label="待办事项ID" value="todo_id"></el-option>
          <el-option label="待办事项截止" value="todo_ddl"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="search_key" placeholder="全局搜索"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearchSubmit">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onRefresh" :icon="Refresh"></el-button>
      </el-form-item>
    </el-form>
    <el-table ref="filterTableRef" class="table-list" row-key="todo_id" :data="paginatedData" style="width: 100%"
              @filter-change="handleFilterChange" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column
        prop="todo_ddl"
        label="截止日期"
        sortable
        width="180"
        column-key="todo_ddl"
      >
      </el-table-column>
      <el-table-column prop="todo_title" label="待办事项标题" width="180" truncated> </el-table-column>
      <el-table-column prop="todo_ctnt" label="待办事项内容" truncated> </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="按标题快速搜索" @input="watchSearch" />
        </template>
        <template #default="scope">
          <el-button size="mini" @click="modifyPop(scope.row)">修改</el-button>
          <el-button size="mini" @click="detailPop(scope.row)">查看详情</el-button>
          <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
      <el-table-column
        prop="todo_fin"
        column-key="todo_fin"
        label="状态"
        width="100"
        :filters="fin_options"
        :filtered-value="filters.todo_fin"
        :filter-multiple="false"
        filter-placement="bottom-end"
      >
        <template #default="scope">
          <el-tag :type="scope.row.todo_fin === '已完成' ? 'primary' : 'success'" disable-transitions>{{ scope.row.todo_fin}}</el-tag>
        </template>
      </el-table-column>
    </el-table>


    <!--    V-MODEL!!!!!-->
    <el-dialog v-model="modifyFormVisible" title="修改待办事项">
      <el-form :model="form">
        <el-form-item label="标题" :label-width="formLabelWidth">
          <el-input v-model="form.todo_title" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="内容" :label-width="formLabelWidth">
          <el-input v-model="form.todo_ctnt" autosize type="textarea"/>
        </el-form-item>
        <el-form-item label="截止日期" :label-width="formLabelWidth">
          <div>
            <el-col :span="11">
              <el-date-picker v-model="form.ddl_date"
                              type="date" placeholder="选择日期" style="width: 100%"
                              value-format="YYYY-MM-DD"
              ></el-date-picker>
            </el-col>
            <el-col class="line" :span="1">&nbsp;&nbsp;&nbsp;&nbsp;</el-col>
            <el-col :span="12">
              <el-time-picker v-model="form.ddl_time" placeholder="选择时间" style="width: 100%"
                              format="HH:mm" value-format="HH:mm"
              ></el-time-picker>
            </el-col>
          </div>
        </el-form-item>
        <el-form-item label="状态" :label-width="formLabelWidth">
          <el-select v-model="form.todo_fin" placeholder="Select" style="width: 240px">
            <el-option
                v-for="item in fin_options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建日期" :label-width="formLabelWidth">
          <div>
            <el-col :span="11">
              <el-date-picker v-model="form.crt_date"
                              type="date" placeholder="选择日期" style="width: 100%"
                              value-format="YYYY-MM-DD"
              ></el-date-picker>
            </el-col>
            <el-col class="line" :span="1">&nbsp;&nbsp;&nbsp;&nbsp;</el-col>
            <el-col :span="12">
              <el-time-picker v-model="form.crt_time" placeholder="选择时间" style="width: 100%"
                              format="HH:mm" value-format="HH:mm"
              ></el-time-picker>
            </el-col>
          </div>
        </el-form-item>
        <el-form-item label="添加者ID" :label-width="formLabelWidth">
          <el-text class="mx-1" type="info">{{ form.adder_id }}</el-text>
        </el-form-item>
        <el-form-item label="待办事项ID" :label-width="formLabelWidth">
          <el-text class="mx-1" type="info">{{ form.todo_id }}</el-text>
        </el-form-item>
        <el-form-item label="从属用戶ID" :label-width="formLabelWidth">
          <el-text class="mx-1" type="info">{{ form.user_id }}</el-text>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEdit()">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog v-model="detailFormVisible" title="待办事项详情">
      <el-form :model="form">
        <el-form-item label="标题&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.todo_title }}
        </el-form-item>
        <el-form-item label="内容&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.todo_ctnt }}
        </el-form-item>
        <el-form-item label="截止日期&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.todo_ddl }}
        </el-form-item>
        <el-form-item label="状态&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.todo_fin }}
        </el-form-item>
        <el-form-item label="创建日期&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.todo_crt }}
        </el-form-item>
        <el-form-item label="添加者ID&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.adder_id }}
        </el-form-item>
        <el-form-item label="待办事項ID&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{ form.todo_id }}
        </el-form-item>
        <el-form-item label="从属用戶ID&nbsp;&nbsp;" :label-width="formLabelWidth">
          {{form.user_id }}
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <el-pagination
      :current-page="currentPage"
      :page-sizes="[5, 10, 15, 20, 25]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="record_cnt"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    >
    </el-pagination>

  </div>
</template>
<script lang="ts">
import { computed, defineComponent, onMounted, reactive, ref, toRefs, watch } from 'vue'
import { useRouter } from 'vue-router'
import permission from '@/directive/permission'
import Service from '../api/index'
import {ElMessage, ElMessageBox} from "element-plus";
import {CirclePlus, Delete, Download, Refresh} from "@element-plus/icons-vue";

export default defineComponent({
  name: 'todoTableList',
  computed: {
    Refresh() {
      return Refresh
    },
    Download() {
      return Download
    },
    Delete() {
      return Delete
    },
    CirclePlus() {
      return CirclePlus
    }
  },
  directives: {
    permission
  },
  setup() {
    // 思考 ref 响应式和 reactive 响应式的区别； 修改对象属性值，是否会刷新数据

    const router = useRouter()
    const filterTableRef = ref(null)
    const state = reactive({
      tableData: [
        {
          todo_ddl: '后台沒打开',
          todo_title: '后台沒打开',
          todo_fin: '后台沒打开',
          todo_ctnt: 'NO'
        },
      ],
      currentPage: 1,
      pageSize: 5,
      search: '',
      modifyFormVisible: false,
      detailFormVisible: false,
      form: {},

      paginatedData: [],
      record_cnt: 0,

      fin_options: [
        {
          value:'未完成',
          text:'未完成',
        },
        {
          value:'已完成',
          text:'已完成',
        },
      ],

      filters: { // 筛选条件
        todo_fin: [] // 筛选的状态，可以是 '已完成' 或 '未完成'
      },

      selectionRows: [] as { todo_id: number }[],

      search_field: "none",
      search_key: "",
    })
    const formInline = reactive({
      user: '',
      region: ''
    })
    // const total = computed(() => state.tableData.length)
    // A quick fix here
    const total = 1

    onMounted(() => {
      // eslint-disable-next-line no-console
      page_size_get_set()
      filterPresetTest()
      getPersonalTodoList()
    })
    // methods
    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('date')
    }

    const page_size_get_set = () => {
      const pageSizeTmp = sessionStorage.getItem("TODOLIST_PAGE_SIZE");
      if (!pageSizeTmp) {
        sessionStorage.setItem("TODOLIST_PAGE_SIZE", 5);
        state.pageSize = 5;
      } else {
        // ####parseInt#### is IMPORTANT!!!!
        state.pageSize = parseInt(pageSizeTmp, 10);
      }
    }

    const filterPresetTest = () => {
      if (sessionStorage.getItem("showUncompleted") === '1') {
        state.filters.todo_fin[0] = "未完成"
        sessionStorage.setItem("showUncompleted", 0)
      }
    }

    const handleSelectionChange = (selection:any[]) => {
      state.selectionRows = selection.map(item => item.todo_id)
      console.log("SelectionRows",state.selectionRows)
    }

    const getPersonalTodoList = () => {
      // console.log("getPersonalTodoList exc")
      try {
        Service.postGetTodoList().then((res) => {
          if (res) {
            state.tableData = []
            // console.log('getPersonalTodoList get')
            // console.log(res)
            const data = res.data
            state.record_cnt = data.length
            state.tableData = data
            updatePaginatedData()  // 更新分页数据
          } else {
            console.log('getPersonalTodoList RES MISS')
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }

    }

    const clearFilter = () => {
      filterTableRef.value.clearFilter()
    }

    const formatter = (row: { address: any }) => row.address
    const filterTag = (value: any, row: { Tag: any }) => row.tag === value
    // const filterStatus = (value: any, row: { todo_fin: any }) => row.todo_fin === value
    const filterHandler = (value: any, row: { [x: string]: any }, column: { property: any }) => {
      const { property } = column
      return row[property] === value
    }

    // 更新分页数据
    const updatePaginatedData = () => {
      let recordsToFilter = state.tableData;
      if (state.search) {
        recordsToFilter = recordsToFilter.filter((record) =>
            record.todo_title.toLowerCase().includes(state.search.toLowerCase()) // 忽略大小写
        );
      }

      if (state.filters.todo_fin[0] && state.filters.todo_fin[0] !== '') {
        recordsToFilter = recordsToFilter.filter((record) =>
            record.todo_fin == state.filters.todo_fin[0]
        );
      }

      // 更新分页数据
      // console.log(state.pageSize)
      const start = (state.currentPage - 1) * state.pageSize;
      const end = state.currentPage * state.pageSize;
      state.paginatedData = recordsToFilter.slice(start, end);  // 根据当前页和页大小提取分页数据
      state.record_cnt = recordsToFilter.length;  // 更新总记录数
    };


    const watchSearch = () => {
      updatePaginatedData();  // 过滤数据并更新分页
    };
    watch(() => state.search, watchSearch);


    const modifyPop = (row) => {
      state.form = row
      state.form.ddl_date = row.todo_ddl.split(" ")[0]
      state.form.ddl_time = row.todo_ddl.split(" ")[1]
      state.form.crt_date = row.todo_crt.split(" ")[0]
      state.form.crt_time = row.todo_crt.split(" ")[1]
      state.modifyFormVisible = true
    }

    const detailPop = (row) => {
      state.detailFormVisible = true
      state.form = row
    }

    const handleEdit = () => {
      // eslint-disable-next-line no-console
      state.modifyFormVisible = false
      let record = state.form
      record.todo_ddl = [record.ddl_date, record.ddl_time].join(" ")
      record.todo_crt = [record.crt_date, record.crt_time].join(" ")
      state.form = {}
      try {
        Service.postModifyTodo(record).then((res) => {
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
    const handleDelete = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      // console.log(index, row)
      let record = {
        todo_id: row.todo_id
      }
      try {
        Service.deleteTodo(record).then((res) => {
          if (res) {
            // console.log(res)
            getPersonalTodoList()
          } else {
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }
      state.tableData.splice(index, 1)
    }
    const handleSizeChange = (val: any) => {
      state.pageSize = val;
      sessionStorage.setItem("TODOLIST_PAGE_SIZE", val)
      updatePaginatedData();  // 更新分页数据
    }

    const handleCurrentChange = (val: number) => {
      state.currentPage = val;
      updatePaginatedData();  // 更新分页数据
    }

    const onDeleteMulti = () => {
      try {
        Service.deleteMulti(state.selectionRows).then((res) => {
          if (res) {
            getPersonalTodoList()
            ElMessage({
              type: 'success',
              message: " 成功删除"
            })
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }

    }

    const onSubmit = () => {
      // eslint-disable-next-line no-console
      console.log('submit!')
    }

    const onAddTodo = () => {
      // eslint-disable-next-line no-console
      router.replace('/todoList/todoAdd')
    }

    const handleFilterChange = (filters: any) => {
      state.filters.todo_fin = filters.todo_fin;   // 只有一个条件在 `filters` 中
      console.log(state.filters.todo_fin);
      console.log(filters.todo_fin);
      updatePaginatedData(); // 更新分页数据
    }

    const onDownload = () => {
      ElMessageBox.confirm('确定要打印为PDF吗', '温馨提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        // 确认后调用获取 PDF 的方法
        Service.getPDF().then(() => {
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

    const onToExcel = () => {
      ElMessageBox.confirm('确定要导出为Excel吗', '温馨提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        // 确认后调用获取 PDF 的方法
        Service.getExcel().then(() => {
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
    const onSearchSubmit = () => {
      const record = {
        field: state.search_field,
        key: state.search_key.toString().trim(),
      }
      if (record.field === "none") {
        ElMessage({
          type: 'warning',
          message: "没有选择条件"
        })
        return
      }
      if (record.key === "") {
        ElMessage({
          type: 'warning',
          message: "没有输入搜索内容"
        })
        return
      }
      try {
        Service.searchBy(record).then((res) => {
          // console.log(res)
          if (res.status ===  0) {
            ElMessage({
              type: 'success',
              message: "搜索结果已返回"
            })
            const data = res.data
            state.record_cnt = data.length
            state.tableData = data
            updatePaginatedData()  // 更新分页数据
          } else if (res.status === 1) {
            ElMessage({
              type: 'warning',
              message: "没有找到符合条件的待办事项"
            })
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message
        })
      }

    }

    const onRefresh = () => {
      getPersonalTodoList()
    }

    return {
      formInline,
      total,
      ...toRefs(state),
      handleCurrentChange,
      handleSizeChange,
      onSubmit,
      onAddTodo,
      handleEdit,
      handleDelete,
      filterTableRef,
      resetDateFilter,
      clearFilter,
      formatter,
      filterTag,
      handleFilterChange,
      filterHandler,
      modifyPop,
      detailPop,
      watchSearch,
      page_size_get_set,
      filterPresetTest,
      handleSelectionChange,
      onDeleteMulti,
      onDownload,
      onToExcel,
      onSearchSubmit,
      onRefresh,
    }
  }
})
</script>
<style lang="stylus" scoped>
.table-container{
    .form-inline{
        margin:15px;
        text-align:left;
    }
    .table-list{
        margin:15px;
    }

}
</style>

