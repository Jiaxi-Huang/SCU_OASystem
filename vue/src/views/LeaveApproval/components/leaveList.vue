<template>
  <div>
    <div class="table-container">
      <el-form :inline="true" :model="formInline" class="form-inline">
        <el-form-item>
          <el-button type="primary" @click="onLeaveRequest">请假申请</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="showMyLeave">我的请假申请</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="showReviewLeave">我处理的请假申请</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="showNotifyLeave">抄送给我的请假申请</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="showAdminLeave" v-if="isAdmin">所有请假申请</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onDownload" :icon="Download">打印</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onToExcel" :icon="Download">导出</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onShowStatistics()" :icon="Odometer">统计信息</el-button>
        </el-form-item>
      </el-form>
      <!---------------------------------------管理员所有请假列表--------------------------------------->
      <el-table ref="filterTableRef"
                class="table-list"
                row-key="date"
                v-loading="loading"
                :data="adminData"
                style="width: 100%"
                v-show="isAdminLeaveShow">
        <el-table-column prop="leave_submitted_at" label="提交日期" sortable width="180" column-key="leave_submitted_at"></el-table-column>
        <el-table-column prop="leave_start_time" label="开始时间" width="180" truncated> </el-table-column>
        <el-table-column prop="leave_end_time" label="结束时间" truncated> </el-table-column>
        <el-table-column prop="leave_reason" label="原因" truncated> </el-table-column>
        <el-table-column align="right">
          <template #header>
            <el-input v-model="search" size="small" placeholder="搜索" />
          </template>
          <template #default="scope">
            <el-button size="small" @click="modifyPop(scope.row)">修改</el-button>
            <el-button size="small" @click="detailPop(scope.row)">查看详情</el-button>
            <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
        <el-table-column
          prop="leave_status"
          label="处理状态"
          width="100"
          :filters="[
            { text: '已通过', value: '已通过' },
            { text: '未通过', value: '未通过' },
            { text: '未审核', value: '未审核' },
          ]"
          :filter-method="filterStatus"
          filter-placement="bottom-end"
        >
          <template #default="scope">
            <el-tag
                :type="scope.row.leave_status === '已通过' ? 'success' : (scope.row.leave_status === '未审核' ? 'primary' : 'danger')"
                disable-transitions>
              {{ scope.row.leave_status }}
            </el-tag>           </template>
        </el-table-column>
      </el-table>
      <!---------------------------------------我的请假列表--------------------------------------->
      <el-table ref="filterTableRef"
                class="table-list"
                row-key="date"
                v-loading="loading"
                :data="myData"
                style="width: 100%"
                v-show="isMyLeaveShow">
        <el-table-column prop="leave_submitted_at" label="提交日期" sortable width="180" column-key="leave_submitted_at"></el-table-column>
        <el-table-column prop="leave_start_time" label="开始时间" width="180" truncated> </el-table-column>
        <el-table-column prop="leave_end_time" label="结束时间" truncated> </el-table-column>
        <el-table-column prop="leave_reason" label="原因" truncated> </el-table-column>
        <el-table-column align="right">
          <template #header>
            <el-input v-model="search" size="small" placeholder="搜索" />
          </template>
          <template #default="scope">
            <el-button size="small" @click="detailPop(scope.row)">查看详情</el-button>
            <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
        <el-table-column
            prop="leave_status"
            label="处理状态"
            width="100"
            :filters="[
            { text: '已通过', value: '已通过' },
            { text: '未通过', value: '未通过' },
            { text: '未审核', value: '未审核' },
          ]"
            :filter-method="filterStatus"
            filter-placement="bottom-end"
        >
          <template #default="scope">
            <el-tag
                :type="scope.row.leave_status === '已通过' ? 'success' : (scope.row.leave_status === '未审核' ? 'primary' : 'danger')"
                disable-transitions>
              {{ scope.row.leave_status }}
            </el-tag>           </template>
        </el-table-column>
      </el-table>
      <!---------------------------------------我处理的请假列表--------------------------------------->
      <el-table ref="filterTableRef"
                class="table-list"
                row-key="date"
                v-loading="loading"
                :data="reviewData"
                style="width: 100%"
                v-show="isReviewLeaveShow">
        <el-table-column prop="leave_submitted_at" label="提交日期" sortable width="180" column-key="leave_submitted_at"></el-table-column>
        <el-table-column prop="leave_start_time" label="开始时间" width="180" truncated> </el-table-column>
        <el-table-column prop="leave_end_time" label="结束时间" truncated> </el-table-column>
        <el-table-column prop="leave_reason" label="原因" truncated> </el-table-column>
        <el-table-column align="right">
          <template #header>
            <el-input v-model="search" size="small" placeholder="搜索" />
          </template>
          <template #default="scope">
            <el-button size="small" @click="modifyPop(scope.row)">处理</el-button>
            <el-button size="small" @click="detailPop(scope.row)">查看详情</el-button>
            <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
        <el-table-column
            prop="leave_status"
            label="处理状态"
            width="100"
            :filters="[
            { text: '已通过', value: '已通过' },
            { text: '未通过', value: '未通过' },
            { text: '未审核', value: '未审核' },
          ]"
            :filter-method="filterStatus"
            filter-placement="bottom-end"
        >
          <template #default="scope">
            <el-tag
                :type="scope.row.leave_status === '已通过' ? 'success' : (scope.row.leave_status === '未审核' ? 'primary' : 'danger')"
                disable-transitions>
              {{ scope.row.leave_status }}
            </el-tag>           </template>
        </el-table-column>
      </el-table>
      <!---------------------------------------抄送给我的请假列表--------------------------------------->
      <el-table ref="filterTableRef"
                class="table-list"
                row-key="date"
                v-loading="loading"
                :data="notifyData"
                style="width: 100%"
                v-show="isNotifyLeaveShow">
        <el-table-column prop="leave_submitted_at" label="提交日期" sortable width="180" column-key="leave_submitted_at"></el-table-column>
        <el-table-column prop="leave_start_time" label="开始时间" width="180" truncated> </el-table-column>
        <el-table-column prop="leave_end_time" label="结束时间" truncated> </el-table-column>
        <el-table-column prop="leave_reason" label="原因" truncated> </el-table-column>
        <el-table-column align="right">
          <template #header>
            <el-input v-model="search" size="small" placeholder="搜索" />
          </template>
          <template #default="scope">
            <el-button size="small" @click="detailPop(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
        <el-table-column
            prop="leave_status"
            label="处理状态"
            width="100"
            :filters="[
            { text: '已通过', value: '已通过' },
            { text: '未通过', value: '未通过' },
            { text: '未审核', value: '未审核' },
          ]"
            :filter-method="filterStatus"
            filter-placement="bottom-end"
        >
          <template #default="scope">
            <el-tag
                :type="scope.row.leave_status === '已通过' ? 'success' : (scope.row.leave_status === '未审核' ? 'primary' : 'danger')"
                disable-transitions>
              {{ scope.row.leave_status }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      <el-dialog v-model="modifyFormVisible" title="修改请假申请">
        <el-form :model="form">
          <el-form-item label="请假记录ID">
            <el-input v-model="form.leave_id" disabled></el-input>
          </el-form-item>
          <el-form-item label="提交用户ID">
            <el-input v-model="form.leave_user_id" disabled></el-input>
          </el-form-item>
          <el-form-item label="开始时间">
            <el-input v-model="form.leave_start_time" autocomplete="on"></el-input>
          </el-form-item>
          <el-form-item label="结束时间">
            <el-input v-model="form.leave_end_time" autosize type="textarea"/>
          </el-form-item>
          <el-form-item label="请假类型">
            <el-input v-model="form.leave_type"></el-input>
          </el-form-item>
          <el-form-item label="请假原因">
            <el-input v-model="form.leave_reason"></el-input>
          </el-form-item>
          <el-form-item label="处理状态">
            <el-input v-model="form.leave_status"></el-input>
          </el-form-item>
          <el-form-item label="提交日期">
            <el-input v-model="form.leave_submitted_at" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="modifyFormVisible = false">取消</el-button>
          <el-button type="primary" @click="handleEdit()">确定</el-button>
        </div>
      </el-dialog>
  
      <el-dialog v-model="detailFormVisible" title="待办事项详情">
        <el-form :model="form">
          <el-form-item label="请假记录ID&nbsp;&nbsp;">
            {{ form.leave_id }}
          </el-form-item>
          <el-form-item label="提交用户ID&nbsp;&nbsp;">
            {{ form.leave_user_id }}
          </el-form-item>
          <el-form-item label="开始时间&nbsp;&nbsp;">
            {{ form.leave_start_time }}
          </el-form-item>
          <el-form-item label="结束时间&nbsp;&nbsp;">
            {{ form.leave_end_time }}
          </el-form-item>
          <el-form-item label="请假类型&nbsp;&nbsp;">
            {{ form.leave_type}}
          </el-form-item>
          <el-form-item label="请假原因&nbsp;&nbsp;">
            {{ form.leave_reason }}
          </el-form-item>
          <el-form-item label="处理状态&nbsp;&nbsp;">
            {{ form.leave_status }}
          </el-form-item>
          <el-form-item label="提交日期&nbsp;&nbsp;">
            {{ form.leave_submitted_at }}
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="detailFormVisible = false">确定</el-button>
        </div>
      </el-dialog>

      <el-dialog v-model="statisticsVisible" title="统计信息">
        <el-card shadow="hover" class="card">
          <p>
            <span><i class="icon-square red"></i> 总记录数 </span> <span>{{ tableData.length }}</span>
          </p>
          <div class="e-chart" style="height: 201px; width: 100%">
            <div ref="refAverageSales" style="width: inherit; height: inherit;"></div>
          </div>
          <div class="chart-widget-list">
            <p>
              <span><i class="icon-square green"></i> 已通过</span><span>{{ statistics.pass }}</span>
            </p>
            <p>
              <span><i class="icon-square deep-blue"></i> 未审核 </span> <span>{{ statistics.unfin }}</span>
            </p>
            <p>
              <span><i class="icon-square red"></i> 未通过 </span> <span>{{ statistics.rej }}</span>
            </p>
          </div>
        </el-card>

        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="statisticsVisible = false">确认</el-button>
        </div>
      </el-dialog>

      <el-pagination
        :hide-on-single-page="true"
        :current-page="currentPage"
        :page-sizes="[5, 10, 15, 20, 25]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
  
    </div>
  </div>
  </template>
  <script lang="ts">
  import {computed, defineComponent, nextTick, onMounted, reactive, ref, toRefs, watch} from 'vue'
  import { useRouter } from 'vue-router'
  import permission from '@/directive/permission'
  import Service from '../api/index'
  import {ElMessage} from "element-plus";
  import {Download, Odometer} from "@element-plus/icons-vue";
  import * as XLSX from 'xlsx';
  import {ElMessageBox} from "element-plus/es";
  import {useInitPieChart} from "./useInitPieCharts";

  export default defineComponent({
    name: 'leaveList',
    computed: {
      Download() {
        return Download
      },
      Odometer() {
        return Odometer
      }
    },
    directives: {
      permission
    },
    setup() {
  
      const router = useRouter()
      const filterTableRef = ref()
      const refAverageSales = ref<HTMLElement | null>(null)

      const state = reactive({
        loading: false,
        tableData: [
          {
            leave_id: 231554651,
            leave_user_id: 1,
            leave_start_time: new Date().toDateString(),
            leave_end_time: new Date().toDateString(),
            leave_reason: '后台没打开',
            leave_type: '事假',
            leave_status: '未处理',
            leave_submitted_at: new Date().toDateString(),
          },
        ],
        currentPage: 1,
        pageSize: 5,
        search: '',
        modifyFormVisible: false,
        detailFormVisible: false,
        statisticsVisible: false,
        form: {},
        statistics: {pass:0, rej:0, unfin:0},
        record_cnt: 0,
        filters: {
          status: ''
        },
        status_options: [
          {
            value:'未审核',
            label:'未审核',
          },
          {
            value:'已通过',
            label:'已通过',
          },
          {
            value:'未通过',
            label:'未通过',
          }
        ],
        myData: [
          {
            leave_id: 231554651,
            leave_user_id: 1,
            leave_start_time: new Date().toDateString(),
            leave_end_time: new Date().toDateString(),
            leave_reason: '我的后台没打开',
            leave_type: '事假',
            leave_status: '未处理',
            leave_submitted_at: new Date().toDateString(),
          },
        ],
        reviewData: [
          {
            leave_id: 231554651,
            leave_user_id:1,
            leave_start_time: new Date().toDateString(),
            leave_end_time: new Date().toDateString(),
            leave_reason: '审核后台没打开',
            leave_type: '事假',
            leave_status: '未处理',
            leave_submitted_at: new Date().toDateString(),
          },
        ],
        notifyData: [
          {
            leave_id: 31554651,
            leave_user_id: 1,
            leave_start_time: new Date().toDateString(),
            leave_end_time: new Date().toDateString(),
            leave_reason: '抄送后台没打开',
            leave_type: '事假',
            leave_status: '未处理',
            leave_submitted_at: new Date().toDateString(),
          },
        ],
        adminData: [
          {
            leave_id: 31554651,
            leave_user_id: 1,
            leave_start_time: new Date().toDateString(),
            leave_end_time: new Date().toDateString(),
            leave_reason: '管理员后台没打开',
            leave_type: '事假',
            leave_status: '未处理',
            leave_submitted_at: new Date().toDateString(),
          },
        ],
        isMyLeaveShow: true,
        isReviewLeaveShow: false,
        isNotifyLeaveShow: false,
        isAdminLeaveShow: false,
      })
      const formInline = reactive({
        user: '',
        region: ''
      })
      const total = 1

      const showMyLeave = () => {
        state.isMyLeaveShow = true;
        state.isReviewLeaveShow = false;
        state.isNotifyLeaveShow = false;
        state.isAdminLeaveShow = false;
        state.tableData = state.myData; // 设置当前表格的数据为 "我的报销申请" 数据
        updatePaginatedData();
      };
      // 显示 "我处理的报销申请" 表格
      const showReviewLeave = () => {
        state.isMyLeaveShow = false;
        state.isReviewLeaveShow = true;
        state.isNotifyLeaveShow = false;
        state.isAdminLeaveShow = false;
        state.tableData = state.reviewData; // 设置当前表格的数据为 "我处理的报销申请" 数据
        updatePaginatedData();
      };
      // 显示 "抄送给我的报销申请" 表格
      const showNotifyLeave = () => {
        state.isMyLeaveShow = false;
        state.isReviewLeaveShow = false;
        state.isNotifyLeaveShow = true;
        state.isAdminLeaveShow = false;
        state.tableData = state.notifyData; // 设置当前表格的数据为 "抄送给我的报销申请" 数据
        updatePaginatedData();
      };
      const showAdminLeave = () => {
        state.isMyLeaveShow = false;
        state.isReviewLeaveShow = false;
        state.isNotifyLeaveShow = false;
        state.isAdminLeaveShow = true;
        state.tableData = state.adminData; // 设置当前表格的数据为 "抄送给我的报销申请" 数据
        updatePaginatedData();
      };
  
      onMounted(() => {
        getLeaveRecord()
        filterLeaveStatus()
        updatePaginatedData()
      })
      const resetDateFilter = () => {
        filterTableRef.value.clearFilter('date')
      }
      const isAdmin = computed(() => {
        const role = localStorage.getItem("role");
        return role === "admin";
      });
      const filterLeaveStatus = () => {
        if (sessionStorage.getItem("showPendingLeave") === '1') {
          state.filters.status = "未审核";
        }
        sessionStorage.setItem("showPendingLeavebursement", String(0));
      }
      const updatePaginatedData = () => {
        let recordsToFilter = state.tableData;
        if (state.search) {
          recordsToFilter = recordsToFilter.filter((record) =>
              record.leave_id.toString().includes(state.search.toLowerCase())
          );
        }
        if (state.filters.status === '未审核') {
          recordsToFilter = recordsToFilter.filter((record) =>
              record.leave_status === state.filters.status
          );
        }
        const start = (state.currentPage - 1) * state.pageSize;
        const end = state.currentPage * state.pageSize;
        state.tableData = recordsToFilter.slice(start, end);  // 更新分页数据
        state.record_cnt = recordsToFilter.length;  // 更新总记录数
      };
      const getLeaveRecord = () => {
        try {
          Service.postGetMyLeaveRecord().then((res) => {
            if (res) {
              state.myData = []
              var data = res.data
              for (let i = 0; i < data.length; i++) {
                var record = {
                  leave_id: data[i].leave_id,
                  leave_user_id: data[i].user_id,
                  leave_review_user_id: data[i].review_user_id,
                  leave_start_time: data[i].start_date,
                  leave_end_time: data[i].end_date,
                  leave_type: data[i].type,
                  leave_reason: data[i].reason,
                  leave_status: data[i].status,
                  leave_submitted_at: data[i].submitted_at,
                }
                state.myData.push(record)
              }
            } else {
              console.log('postGetLeaveApproval RES MISS')
            }
          });
          Service.postGetReviewLeaveRecord().then((res) => {
            if (res) {
              state.reviewData = []
              var data = res.data
              for (let i = 0; i < data.length; i++) {
                var record = {
                  leave_id: data[i].leave_id,
                  leave_user_id: data[i].user_id,
                  leave_review_user_id: data[i].review_user_id,
                  leave_start_time: data[i].start_date,
                  leave_end_time: data[i].end_date,
                  leave_type: data[i].type,
                  leave_reason: data[i].reason,
                  leave_status: data[i].status,
                  leave_submitted_at: data[i].submitted_at,
                }
                state.reviewData.push(record)
              }
            } else {
              console.log('postGetLeaveApproval RES MISS')
            }
          });
          Service.postGetNotifyLeaveRecord().then((res) => {
            if (res) {
              state.notifyData = []
              var data = res.data
              for (let i = 0; i < data.length; i++) {
                var record = {
                  leave_id: data[i].leave_id,
                  leave_user_id: data[i].user_id,
                  leave_review_user_id: data[i].review_user_id,
                  leave_start_time: data[i].start_date,
                  leave_end_time: data[i].end_date,
                  leave_type: data[i].type,
                  leave_reason: data[i].reason,
                  leave_status: data[i].status,
                  leave_submitted_at: data[i].submitted_at,
                }
                state.notifyData.push(record)
              }
            } else {
              console.log('postGetLeaveApproval RES MISS')
            }
          });
        } catch (err) {
          ElMessage({
            type: 'warning',
            message: err.message
          })
        }
        if (isAdmin) {
          try {
            Service.postGetAdminLeaveRecord().then((res) => {
              if (res) {
                state.adminData = []
                var data = res.data
                for (let i = 0; i < data.length; i++) {
                  var record = {
                    leave_id: data[i].leave_id,
                    leave_user_id: data[i].user_id,
                    leave_review_user_id: data[i].review_user_id,
                    leave_start_time: data[i].start_date,
                    leave_end_time: data[i].end_date,
                    leave_type: data[i].type,
                    leave_reason: data[i].reason,
                    leave_status: data[i].status,
                    leave_submitted_at: data[i].submitted_at,
                  }
                  state.adminData.push(record)
                }
              } else {
                console.log('postGetLeaveApproval RES MISS')
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

      const clearFilter = () => {
        filterTableRef.value.clearFilter()
      }

      const watchSearch = () => {
        updatePaginatedData();  // 过滤数据并更新分页
      };
      watch(() => state.search, watchSearch);

      const filterStatus = (value: any, row: { status: any }) => row.leave_status === value

      const modifyPop = (row) => {
        state.modifyFormVisible = true
        state.form = row
      }

      const detailPop = (row) => {
        state.detailFormVisible = true
        state.form = row
      }

      const handleEdit = () => {
        // eslint-disable-next-line no-console
        state.modifyFormVisible = false
        let record = state.form
        state.form = {}
        try {
          Service.postModifyLeaveApproval(record).then((res) => {
            if (res) {
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
        console.log(index, row)
        let record = {
          leave_id: row.leave_id
        }
        try {
          Service.deleteLeaveApproval(record).then((res) => {
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
        state.tableData.splice(index, 1)
      }
      const handleSizeChange = (val: any) => {
        // eslint-disable-next-line no-console
        console.log(`每页 ${val} 条`)
        state.pageSize = val
        // request api to change tableData
      }

      const handleCurrentChange = (val: any) => {
        console.log(`当前页: ${val}`)
        state.currentPage = val
      }


      const onLeaveRequest = () => {
        // eslint-disable-next-line no-console
        router.replace('/leaveApproval/leaveRequest')
      }

      const onDownload = () => {
        ElMessageBox.confirm('确定要打印当前表格数据吗？', '温馨提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          const table = document.querySelector('.el-table') as HTMLElement;

          if (table) {
            const printWindow = window.open('', '', 'height=600,width=800');
            if (printWindow) {
              printWindow.document.write('<html><head><title>打印请假数据</title>');
              printWindow.document.write('<style>table { width: 100%; border-collapse: collapse; } th, td { border: 1px solid #000; padding: 8px; text-align: left; }</style>');
              printWindow.document.write('</head><body>');
              printWindow.document.write('<h2>请假数据</h2>');
              printWindow.document.write(table.outerHTML);
              printWindow.document.write('</body></html>');
              printWindow.document.close();

              setTimeout(() => {
                printWindow.print();
                printWindow.close();
              }, 50000);
            }
          } else {
            ElMessage({
              type: 'error',
              message: '无法获取表格数据'
            });
          }
        }).catch(() => {
          ElMessage({
            type: 'info',
            message: '已取消'
          });
        });
      };

      const onToExcel = () => {
        ElMessageBox.confirm('确定要导出当前表格数据为 Excel 吗？', '温馨提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          const data = state.tableData.map((item) => ({
            leave_id: item.leave_id,
            leave_user_id: item.leave_user_id,
            leave_start_time: item.leave_start_time,
            leave_end_time: item.leave_end_time,
            leave_type: item.leave_type,
            leave_reason: item.leave_reason,
            leave_status: item.leave_status,
            leave_submitted_at: item.leave_submitted_at,
          }));

          const worksheet = XLSX.utils.json_to_sheet(data);
          const workbook = XLSX.utils.book_new();
          XLSX.utils.book_append_sheet(workbook, worksheet, '请假数据');

          XLSX.writeFile(workbook, '请假数据.xlsx');

          ElMessage({
            type: 'success',
            message: 'Excel 文件已生成并下载'
          });
        }).catch(() => {
          ElMessage({
            type: 'info',
            message: '已取消'
          });
        });
      };

      const onShowStatistics = () => {
        state.statisticsVisible = true
        let pass = 0, rej = 0, unfin = 0;
        state.tableData.forEach(record => {
          if(record.leave_status == '已通过'){
            pass++;
          }else if(record.leave_status == '未通过'){
            rej++;
          }else{
            unfin++;
          }
        })
        state.statistics.pass = pass;
        state.statistics.unfin = unfin;
        state.statistics.rej = rej;
        const data = [
          {
            name: "已通过",
            value: pass
          },
          {
            name: "未通过",
            value: rej
          },
          {
            name: "未审核",
            value: unfin
          }];
        nextTick(() => {
          if (refAverageSales.value) {
            useInitPieChart(refAverageSales.value, data)
          } else {
            console.log("refAverageSales not exist!")
          }
        })
      }


      return {
        formInline,
        total,
        ...toRefs(state),
        handleCurrentChange,
        handleSizeChange,
        onLeaveRequest,
        handleEdit,
        handleDelete,
        filterTableRef,
        resetDateFilter,
        clearFilter,
        filterStatus,
        modifyPop,
        detailPop,
        filterLeaveStatus,
        showMyLeave,
        showReviewLeave,
        showNotifyLeave,
        showAdminLeave,
        isAdmin,
        onDownload,
        onToExcel,
        onShowStatistics,
        refAverageSales,
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
  
