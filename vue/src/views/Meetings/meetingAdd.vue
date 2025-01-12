<template>
  <div class="FormInfo">
    <el-row>
      <el-col :offset="1" :span="22">
        <div class="grid-content bg-purple-dark">
          <el-card class="box-card">
            <div style="text-align: left">
              <span>新建会议</span>
              <el-divider></el-divider>
            </div>
            <el-form ref="activityForm" style="text-align: left" :model="sizeForm" label-width="80px" size="mini" :rules="rules">
              <el-form-item label="标题"  prop="mtin_title">
                <el-input v-model="sizeForm.mtin_title"></el-input>
              </el-form-item>
              <el-form-item label="内容"  prop="mtin_ctnt">
                <el-input v-model="sizeForm.mtin_ctnt" autosize type="textarea"/>
              </el-form-item>
              <el-form-item label="地点" prop="mtin_loc">
                <el-input v-model="sizeForm.mtin_loc" autosize type="textarea"/>
              </el-form-item>
              <el-form-item label="持续时间">
                <el-select v-model="sizeForm.mtin_len" placeholder="Select" style="width: 240px">
                  <el-option
                      v-for="item in time_options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="开始时间">
                <div>
                  <el-col :span="11">
                    <el-date-picker v-model="sizeForm.date1"
                                    type="date" placeholder="选择日期" style="width: 100%"
                                    value-format="YYYY-MM-DD"
                    ></el-date-picker>
                  </el-col>
                  <el-col class="line" :span="1">&nbsp;&nbsp;&nbsp;&nbsp;</el-col>
                  <el-col :span="12">
                    <el-time-picker v-model="sizeForm.date2" placeholder="选择时间" style="width: 100%"
                                    format="HH:mm" value-format="HH:mm"
                    ></el-time-picker>
                  </el-col>
                </div>
              </el-form-item>
              <el-form-item size="large">
                <el-button type="primary" @click="submitForm">立即创建</el-button>
                <el-button>取消</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Edit, DeleteFilled, Check, ArrowLeft } from '@element-plus/icons-vue'
import Service from "@/views/Meetings/api/index";

export default defineComponent({
  name: 'AddMeeting',
  components: {
    Edit,
    DeleteFilled,
    Check,
    ArrowLeft
  },
  props: {
    userIds: {
      type: Array,
      default: () => []
    }
  },
  emits: ['success'],
  setup() {
    const router = useRouter()
    const sizeForm = reactive({
      mtin_title: '',
      mtin_ctnt: '',
      mtin_loc: '',
      mtin_fin: null,
      mtin_len: '',
      date1: '',
      date2: '',
      type: [],
    })
    const activityForm = ref()

    // 校验规则
    const rules = reactive({
      mtin_title: [
        {
          required: true,
          message: '标题不能为空',
          trigger: 'blur',
        },
        {
          validator: (rule, value, callback) => {
            if (value.length > 20) {
              callback(new Error('标题不能超过20个字'));
            } else {
              callback();
            }
          },
          trigger: 'change',
        },
      ],
      mtin_loc: [
        {
          required: true,
          message: '地点不能为空',
          trigger: 'blur',
        },
        {
          validator: (rule, value, callback) => {
            if (value.length > 20) {
              callback(new Error('标题不能超过20个字'));
            } else {
              callback();
            }
          },
          trigger: 'change',
        },
      ],
      mtin_ctnt: [
        {
          required: true,
          message: '内容不能为空',
          trigger: 'blur',
        },
        {
          validator: (rule, value, callback) => {
            if (value.length > 120) {
              callback(new Error('内容不能超过120个字'));
            } else {
              callback();
            }
          },
          trigger: 'change',
        },
      ],
    });

    onMounted(() => {
      // eslint-disable-next-line no-console
      // console.log('show sizeFormRef.value', activityForm.value)
    })
    // methods
    const submitForm = () => {
      activityForm.value.validate(async(valid: any) => {
        if (valid) {
          const record = {
            mtin_title: sizeForm.mtin_title,
            mtin_ctnt: sizeForm.mtin_ctnt,
            mtin_st: `${sizeForm.date1  } ${  sizeForm.date2}`,
            mtin_loc:sizeForm.mtin_loc,
            mtin_len: sizeForm.mtin_len,
            mtin_fin : 0,
            user_ids: [],
            accessToken: sessionStorage.getItem('accessToken'),
          }
          const res = await Service.postCreateMeeting(record)
          if(res.status === 0){
            ElMessage({
              type: 'success',
              message: '创建会议成功，已经添加至您的会议列表'
            })
            sizeForm.mtin_title = '';
            sizeForm.mtin_ctnt = '';
            sizeForm.date1 = '';
            sizeForm.date2 = '';
            sizeForm.mtin_loc = '';
            sizeForm.mtin_len = '';
          }
          else{
            ElMessage({
              type: 'warning',
              message: res.message
            })
          }
        }
      })
    }
    const resetForm = () => {
      activityForm.value.resetFields()
    }
    const handleBack = () => {
      router.go(-1)
    }

    const time_options = [
      {
        value:'15min',
        text:'15min',
      },
      {
        value:'30min',
        text:'30min',
      },
      {
        value:'45min',
        text:'45min',
      },
      {
        value:'60min',
        text:'60min',
      },
      {
        value:'90min',
        text:'90min',
      },
    ]

    return {
      handleBack,
      sizeForm,
      activityForm,
      submitForm,
      resetForm,
      time_options,
      rules,
    }
  }
})
</script>

<style lang="stylus" scoped>
.FormInfo{
    margin-top:20px;
    .demo-ruleForm{
        text-align :left;
    }
    .info{
        text-align: left;
    padding-left: 20px;
    margin-bottom: 20px;
    font-size: 12px;
    }
     .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
  }

  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .box-card {
    width:100%;
  }

   .el-row {
       margin-bottom: 20px;
     }
}
</style>
