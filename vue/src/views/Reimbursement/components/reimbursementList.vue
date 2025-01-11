<template>
  <div class="table-container">
    <el-form :inline="true" :model="formInline" class="form-inline">
      <el-form-item>
        <el-button type="primary" @click="onAddReimbursement">提交报销申请</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="showMyReim">我的报销申请</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="showReviewReim">我处理的报销申请</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="showNotifyReim">抄送给我的报销申请</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="showAdminReim" v-if="isAdmin">所有报销申请</el-button>
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
    <el-table id="myReim" ref="filterTableRef" class="table-list" row-key="reimbursement_id" :data="paginatedMyData" style="width: 100%"
              @filter-change="handleFilterChange" v-show="isMyReimShow">
      <el-table-column
          prop="reimbursement_id"
          label="报销编号"
          sortable
          width="180"
          column-key="reimbursement_id"
      >
      </el-table-column>
      <el-table-column prop="user_id" label="提交用户id" width="180" truncated> </el-table-column>
      <el-table-column prop="amount" label="金额" truncated> </el-table-column>
      <el-table-column prop="description" label="描述" truncated> </el-table-column>
      <el-table-column
          prop="status"
          column-key="status"
          label="状态"
          width="100"
          :filters="status_options"
          :filtered-value="filters.status"
          :filter-multiple="false"
          filter-placement="bottom-end"
      >
        <template #default="scope">
          <el-tag
              :type="scope.row.status === '已通过' ? 'success' : (scope.row.status === '未审核' ? 'primary' : 'danger')"
              disable-transitions>
            {{ scope.row.status }}
          </el-tag>        </template>
      </el-table-column>
      <el-table-column prop="submitted_at" label="提交时间" truncated> </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="输入标题字段关键字搜索" />
        </template>
        <template #default="scope">
          <!--          <el-button size="mini" @click="modifyPop(scope.row)">修改</el-button>-->
          <el-button size="mini" @click="detailPop(scope.row)">查看详情</el-button>
          <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-table id="reviewReim" ref="filterTableRef" class="table-list" row-key="reimbursement_id" :data="paginatedReviewData" style="width: 100%"
              @filter-change="handleFilterChange" v-show="isReviewReimShow">
      <el-table-column
          prop="reimbursement_id"
          label="报销编号"
          sortable
          width="180"
          column-key="reimbursement_id"
      >
      </el-table-column>
      <el-table-column prop="user_id" label="提交用户id" width="180" truncated> </el-table-column>
      <el-table-column prop="amount" label="金额" truncated> </el-table-column>
      <el-table-column prop="description" label="描述" truncated> </el-table-column>
      <el-table-column
          prop="status"
          column-key="status"
          label="状态"
          width="100"
          :filters="status_options"
          :filtered-value="filters.status"
          :filter-multiple="false"
          filter-placement="bottom-end"
      >
        <template #default="scope">
          <el-tag
              :type="scope.row.status === '已通过' ? 'success' : (scope.row.status === '未审核' ? 'primary' : 'danger')"
              disable-transitions>
            {{ scope.row.status }}
          </el-tag>        </template>
      </el-table-column>
      <el-table-column prop="submitted_at" label="提交时间" truncated> </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="输入标题字段关键字搜索" />
        </template>
        <template #default="scope">
          <el-button size="mini" @click="modifyPop(scope.row)">处理</el-button>
          <el-button size="mini" @click="detailPop(scope.row)">查看详情</el-button>
          <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" icon="el-icon-info" icon-color="red" title="确定删除该条记录吗？" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-table id="notifyReim" ref="filterTableRef" class="table-list" row-key="reimbursement_id" :data="paginatedNotifyData" style="width: 100%"
              @filter-change="handleFilterChange" v-show="isNotifyReimShow">
      <el-table-column
          prop="reimbursement_id"
          label="报销编号"
          sortable
          width="180"
          column-key="reimbursement_id"
      >
      </el-table-column>
      <el-table-column prop="user_id" label="抄送用户id" width="180" truncated> </el-table-column>
      <el-table-column prop="amount" label="金额" truncated> </el-table-column>
      <el-table-column prop="description" label="描述" truncated> </el-table-column>
      <el-table-column
          prop="status"
          column-key="status"
          label="状态"
          width="100"
          :filters="status_options"
          :filtered-value="filters.status"
          :filter-multiple="false"
          filter-placement="bottom-end"
      >
        <template #default="scope">
          <el-tag
              :type="scope.row.status === '已通过' ? 'success' : (scope.row.status === '未审核' ? 'primary' : 'danger')"
              disable-transitions>
            {{ scope.row.status }}
          </el-tag>        </template>
      </el-table-column>
      <el-table-column prop="submitted_at" label="提交时间" truncated> </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="输入标题字段关键字搜索" />
        </template>
        <template #default="scope">
          <!--          <el-button size="mini" @click="modifyPop(scope.row)">修改</el-button>-->
          <el-button size="mini" @click="detailPop(scope.row)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-table id="adminReim" ref="filterTableRef" class="table-list" row-key="reimbursement_id" :data="paginatedAdminData" style="width: 100%"
              @filter-change="handleFilterChange" v-show="isAdminReimShow">
      <el-table-column
          prop="reimbursement_id"
          label="报销编号"
          sortable
          width="180"
          column-key="reimbursement_id"
      >
      </el-table-column>
      <el-table-column prop="user_id" label="提交用户id" width="180" truncated> </el-table-column>
      <el-table-column prop="amount" label="金额" truncated> </el-table-column>
      <el-table-column prop="description" label="描述" truncated> </el-table-column>
      <el-table-column
          prop="status"
          column-key="status"
          label="状态"
          width="100"
          :filters="status_options"
          :filtered-value="filters.status"
          :filter-multiple="false"
          filter-placement="bottom-end"
      >
        <template #default="scope">
          <el-tag
              :type="scope.row.status === '已通过' ? 'success' : (scope.row.status === '未审核' ? 'primary' : 'danger')"
              disable-transitions>
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="submitted_at" label="提交时间" truncated> </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="mini" placeholder="输入标题字段关键字搜索" />
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
    </el-table>

    <el-dialog v-model="modifyFormVisible" title="修改报销内容">
      <el-form :model="form">
        <el-form-item label="报销编号">
          <el-input v-model="form.reimbursement_id" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="提交用户id">
          <el-input v-model="form.user_id" autosize type="textarea"/>
        </el-form-item>
        <el-form-item label="金额" >
          <el-input v-model="form.amount" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="Select" style="width: 240px">
            <el-option
                v-for="item in status_options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="提交时间">
          <el-input v-model="form.submitted_at" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="modifyFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEdit()">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog v-model="detailFormVisible" title="报销详情">
      <el-form :model="form">
        <el-form-item label="报销编号&nbsp;&nbsp;">
          {{ form.reimbursement_id }}
        </el-form-item>
        <el-form-item label="提交用户id&nbsp;&nbsp;">
          {{ form.user_id }}
        </el-form-item>
        <el-form-item label="金额&nbsp;&nbsp;">
          {{ form.amount }}
        </el-form-item>
        <el-form-item label="描述&nbsp;&nbsp;">
          {{ form.description }}
        </el-form-item>
        <el-form-item label="状态&nbsp;&nbsp;">
          {{ form.status }}
        </el-form-item>
        <el-form-item label="提交时间&nbsp;&nbsp;">
          {{ form.submitted_at }}
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailFormVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog v-model="statisticsVisible" title="统计信息">
      <el-card shadow="hover" class="card">
        <p>
          <span><i class="icon-square red"></i> 总记录数 </span>
          <span>{{ getCurrentTableData().length }}</span> <!-- 动态计算当前表格的总记录数 -->
        </p>
        <div class="e-chart" style="height: 201px; width: 100%">
          <div ref="refAverageSales" style="width: inherit; height: inherit;"></div>
        </div>
        <div class="chart-widget-list">
          <p>
            <span><i class="icon-square green"></i> 已通过</span>
            <span>{{ statistics.pass }}</span>
          </p>
          <p>
            <span><i class="icon-square deep-blue"></i> 未审核 </span>
            <span>{{ statistics.unfin }}</span>
          </p>
          <p>
            <span><i class="icon-square red"></i> 未通过 </span>
            <span>{{ statistics.rej }}</span>
          </p>
        </div>
      </el-card>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="statisticsVisible = false">确认</el-button>
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
import {computed, defineComponent, nextTick, onMounted, reactive, ref, toRefs, watch} from 'vue'
import { useRouter } from 'vue-router'
import permission from '@/directive/permission'
import Service from '../api/index'
import {ElMessage, ElMessageBox} from "element-plus";
import {Download, Odometer, Printer} from "@element-plus/icons-vue";
import {useInitPieChart} from "./useInitPieCharts";
import * as XLSX from 'xlsx';

export default defineComponent({
  name: 'reimbursementList',
  computed: {
    Download() {
      return Download
    },
    Odometer() {
      return Odometer
    }
  },
  components: {Download, Printer, Odometer},
  directives: {
    permission
  },
  setup() {

    const router = useRouter()
    const filterTableRef = ref()
    const refAverageSales = ref<HTMLElement | null>(null)
    const state = reactive({
      tableData: [], // 不再使用
      currentPage: 1,
      pageSize: 5,
      search: '',
      modifyFormVisible: false,
      detailFormVisible: false,
      statisticsVisible: false,
      form: {},
      statistics: { pass: 0, rej: 0, unfin: 0 },
      record_cnt: 0,
      status_options: [
        { value: '未审核', label: '未审核' },
        { value: '已通过', label: '已通过' },
        { value: '未通过', label: '未通过' },
      ],
      filters: {
        status: '',
      },
      // 原始数据
      myReimData: [], // 我的报销申请原始数据
      reviewReimData: [], // 我处理的报销申请原始数据
      notifyReimData: [], // 抄送给我的报销申请原始数据
      adminReimData: [], // 所有报销申请原始数据
      // 分页数据
      paginatedMyData: [], // 我的报销申请分页数据
      paginatedReviewData: [], // 我处理的报销申请分页数据
      paginatedNotifyData: [], // 抄送给我的报销申请分页数据
      paginatedAdminData: [], // 所有报销申请分页数据
      isMyReimShow: true,
      isReviewReimShow: false,
      isNotifyReimShow: false,
      isAdminReimShow: false,
    });

    const formInline = reactive({
      user: '',
      region: ''
    })
    const total = 1
    // 显示 "我的报销申请" 表格
    const showMyReim = () => {
      state.isMyReimShow = true;
      state.isReviewReimShow = false;
      state.isNotifyReimShow = false;
      state.isAdminReimShow = false;
      updatePaginatedData();  // 更新分页数据
    };

    const showReviewReim = () => {
      state.isMyReimShow = false;
      state.isReviewReimShow = true;
      state.isNotifyReimShow = false;
      state.isAdminReimShow = false;
      updatePaginatedData();  // 更新分页数据
    };

    const showNotifyReim = () => {
      state.isMyReimShow = false;
      state.isReviewReimShow = false;
      state.isNotifyReimShow = true;
      state.isAdminReimShow = false;
      updatePaginatedData();  // 更新分页数据
    };

    const showAdminReim = () => {
      state.isMyReimShow = false;
      state.isReviewReimShow = false;
      state.isNotifyReimShow = false;
      state.isAdminReimShow = true;
      updatePaginatedData();  // 更新分页数据
    };

    onMounted(() => {
      getReimbursementList()
      filterReimbursementStatus()
      updatePaginatedData()
    })
    // methods
    const resetDateFilter = () => {
      filterTableRef.value.clearFilter('date')
    }
    const isAdmin = computed(() => {
      const role = localStorage.getItem("role");
      return role === "admin";
    });
    const filterReimbursementStatus = () => {
      if (sessionStorage.getItem("showPendingReimbursement") === '1') {
        state.filters.status = "未审核";
        console.log("showPendingReimbursement", state.filters.status);
      }
      sessionStorage.setItem("showPendingReimbursement", String(0));
    }

    const updatePaginatedData = () => {
      let recordsToFilter = [];

      // 根据当前显示的表格类型，选择对应的原始数据
      if (state.isMyReimShow) {
        recordsToFilter = state.myReimData; // 使用原始数据
      } else if (state.isReviewReimShow) {
        recordsToFilter = state.reviewReimData; // 使用原始数据
      } else if (state.isNotifyReimShow) {
        recordsToFilter = state.notifyReimData; // 使用原始数据
      } else if (state.isAdminReimShow) {
        recordsToFilter = state.adminReimData; // 使用原始数据
      }

      // 根据搜索条件过滤数据
      if (state.search) {
        recordsToFilter = recordsToFilter.filter((record) =>
            record.reimbursement_id.toLowerCase().includes(state.search.toLowerCase())
        );
      }

      // 根据状态过滤数据
      if (state.filters.status) {
        recordsToFilter = recordsToFilter.filter((record) =>
            record.status === state.filters.status
        );
      }

      // 更新总记录数
      state.record_cnt = recordsToFilter.length;

      // 计算分页数据的起始和结束位置
      const start = (state.currentPage - 1) * state.pageSize;
      const end = state.currentPage * state.pageSize;

      // 根据当前显示的表格类型，更新对应的分页数据
      if (state.isMyReimShow) {
        state.paginatedMyData = recordsToFilter.slice(start, end); // 从原始数据中切分
      } else if (state.isReviewReimShow) {
        state.paginatedReviewData = recordsToFilter.slice(start, end); // 从原始数据中切分
      } else if (state.isNotifyReimShow) {
        state.paginatedNotifyData = recordsToFilter.slice(start, end); // 从原始数据中切分
      } else if (state.isAdminReimShow) {
        state.paginatedAdminData = recordsToFilter.slice(start, end); // 从原始数据中切分
      }
    };

    const getReimbursementList = () => {
      try {
        // 获取“我的报销申请”
        Service.getMyReimbursementList().then((res) => {
          console.log("getReimbursementList: ", res);
          if (res) {
            const myReimData = [];
            var data = res.data;
            for (let i = 0; i < data.length; i++) {
              var record = {
                reimbursement_id: data[i].reimbursement_id,
                user_id: data[i].user_id,
                amount: data[i].amount,
                description: data[i].description,
                status: data[i].status,
                submitted_at: data[i].submitted_at,
              };
              myReimData.push(record);
            }
            state.myReimData = myReimData; // 保存到原始数据
            updatePaginatedData(); // 更新分页数据
          } else {
            console.log('获取“我的报销申请”数据失败');
          }
        });

        // 获取“我处理的报销申请”
        Service.getReviewReimbursementList().then((res) => {
          if (res) {
            const reviewReimData = [];
            var data = res.data;
            for (let i = 0; i < data.length; i++) {
              var record = {
                reimbursement_id: data[i].reimbursement_id,
                user_id: data[i].user_id,
                amount: data[i].amount,
                description: data[i].description,
                status: data[i].status,
                submitted_at: data[i].submitted_at,
              };
              reviewReimData.push(record);
            }
            state.reviewReimData = reviewReimData; // 保存到原始数据
            updatePaginatedData(); // 更新分页数据
          } else {
            console.log('获取“我处理的报销申请”数据失败');
          }
        });

        // 获取“抄送给我的报销申请”
        Service.getNotifyReimbursementList().then((res) => {
          if (res) {
            const notifyReimData = [];
            var data = res.data;
            for (let i = 0; i < data.length; i++) {
              var record = {
                reimbursement_id: data[i].reimbursement_id,
                user_id: data[i].user_id,
                amount: data[i].amount,
                description: data[i].description,
                status: data[i].status,
                submitted_at: data[i].submitted_at,
              };
              notifyReimData.push(record);
            }
            state.notifyReimData = notifyReimData; // 保存到原始数据
            updatePaginatedData(); // 更新分页数据
          } else {
            console.log('获取“抄送给我的报销申请”数据失败');
          }
        });
      } catch (err) {
        ElMessage({
          type: 'warning',
          message: err.message,
        });
      }

      // 管理员角色，获取所有报销申请
      if (isAdmin.value) {
        try {
          Service.getAdminReimbursementList().then((res) => {
            if (res) {
              const adminReimData = [];
              var data = res.data;
              for (let i = 0; i < data.length; i++) {
                var record = {
                  reimbursement_id: data[i].reimbursement_id,
                  user_id: data[i].user_id,
                  amount: data[i].amount,
                  description: data[i].description,
                  status: data[i].status,
                  submitted_at: data[i].submitted_at,
                };
                adminReimData.push(record);
              }
              state.adminReimData = adminReimData; // 保存到原始数据
              updatePaginatedData(); // 更新分页数据
            } else {
              console.log('获取管理员报销申请数据失败');
            }
          });
        } catch (err) {
          ElMessage({
            type: 'warning',
            message: err.message,
          });
        }
      }
    };

    const getCurrentTableData = () => {
      if (state.isMyReimShow) {
        return state.myReimData; // 我的报销申请原始数据
      } else if (state.isReviewReimShow) {
        return state.reviewReimData; // 我处理的报销申请原始数据
      } else if (state.isNotifyReimShow) {
        return state.notifyReimData; // 抄送给我的报销申请原始数据
      } else if (state.isAdminReimShow) {
        return state.adminReimData; // 所有报销申请原始数据
      }
      return [];
    };

    const clearFilter = () => {
      filterTableRef.value.clearFilter()
    }

    const watchSearch = () => {
      updatePaginatedData();  // 过滤数据并更新分页
    };
    watch(() => state.search, watchSearch);

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
        Service.updateReimbursement(record).then((res) => {
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
      console.log(index, row)
      let record = {
        reimbursement_id: row.reimbursement_id
      }
      try {
        Service.deleteReimbursement(record).then((res) => {
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
      state.pageSize = val;
      state.currentPage = 1; // 重置为第一页
      updatePaginatedData(); // 更新分页数据
    };

    const handleCurrentChange = (val: any) => {
      state.currentPage = val;
      updatePaginatedData(); // 更新分页数据
    };

    const handleFilterChange = (filters: any) => {
      state.filters.status = filters.status;
      updatePaginatedData();
    }

    const onAddReimbursement = () => {
      // eslint-disable-next-line no-console
      router.replace('/reimbursement/reimbursementAdd')
    }
    const onDownload = () => {
      ElMessageBox.confirm('确定要打印当前表格数据吗？', '温馨提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        const recordsToPrint = getCurrentTableData(); // 获取当前表格的数据

        // 创建打印内容
        const printContent = `
      <html>
        <head>
          <title>打印报销数据</title>
          <style>
            table { width: 100%; border-collapse: collapse; }
            th, td { border: 1px solid #000; padding: 8px; text-align: left; }
          </style>
        </head>
        <body>
          <h2>报销数据</h2>
          <table>
            <thead>
              <tr>
                <th>报销编号</th>
                <th>提交用户ID</th>
                <th>金额</th>
                <th>描述</th>
                <th>状态</th>
                <th>提交时间</th>
              </tr>
            </thead>
            <tbody>
              ${recordsToPrint.map(record => `
                <tr>
                  <td>${record.reimbursement_id}</td>
                  <td>${record.user_id}</td>
                  <td>${record.amount}</td>
                  <td>${record.description}</td>
                  <td>${record.status}</td>
                  <td>${record.submitted_at}</td>
                </tr>
              `).join('')}
            </tbody>
          </table>
        </body>
      </html>
    `;

        // 打开新窗口并打印
        const printWindow = window.open('', '', 'height=600,width=800');
        if (printWindow) {
          printWindow.document.write(printContent);
          printWindow.document.close();

          setTimeout(() => {
            printWindow.print();
            printWindow.close();
          }, 500);
        } else {
          ElMessage({
            type: 'error',
            message: '无法打开打印窗口'
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
        const recordsToExport = getCurrentTableData(); // 获取当前表格的数据

        // 将数据转换为 Excel 格式
        const data = recordsToExport.map((item) => ({
          报销编号: item.reimbursement_id,
          提交用户ID: item.user_id,
          金额: item.amount,
          描述: item.description,
          状态: item.status,
          提交时间: item.submitted_at,
        }));

        const worksheet = XLSX.utils.json_to_sheet(data);
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, '报销数据');

        // 导出 Excel 文件
        XLSX.writeFile(workbook, '报销数据.xlsx');

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
      state.statisticsVisible = true;

      // 获取当前表格的原始数据
      const recordsToCount = getCurrentTableData();

      // 统计通过、未通过和未审核的记录数
      let pass = 0, rej = 0, unfin = 0;
      recordsToCount.forEach(record => {
        if (record.status === '已通过') {
          pass++;
        } else if (record.status === '未通过') {
          rej++;
        } else if (record.status === '未审核') {
          unfin++;
        }
      });

      // 更新统计数据
      state.statistics.pass = pass;
      state.statistics.rej = rej;
      state.statistics.unfin = unfin;

      // 更新图表数据
      const data = [
        { name: "已通过", value: pass },
        { name: "未通过", value: rej },
        { name: "未审核", value: unfin },
      ];

      // 渲染饼图
      nextTick(() => {
        if (refAverageSales.value) {
          useInitPieChart(refAverageSales.value, data);
        } else {
          console.log("refAverageSales not exist!");
        }
      });
    };

    return {
      formInline,
      total,
      ...toRefs(state),
      handleCurrentChange,
      handleSizeChange,
      onAddReimbursement,
      handleEdit,
      handleDelete,
      filterTableRef,
      resetDateFilter,
      getCurrentTableData,
      clearFilter,
      modifyPop,
      detailPop,
      filterReimbursementStatus,
      handleFilterChange,
      showMyReim,
      showReviewReim,
      showNotifyReim,
      showAdminReim,
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
