<template>
  <div class="FormInfo">
    <el-row>
      <el-col :offset="1" :span="22">
        <div class="grid-content bg-purple-dark">
          <el-card class="box-card">
            <div style="text-align: left">
              <span>待办事项添加</span>
              <el-divider></el-divider>
            </div>
            <el-form ref="activityForm" style="text-align: left" :model="sizeForm" label-width="80px" size="mini">
              <el-form-item label="标题">
                <el-input v-model="sizeForm.todo_title"></el-input>
              </el-form-item>
              <el-form-item label="内容" :label-width="formLabelWidth">
                <el-input v-model="sizeForm.todo_ctnt" autosize type="textarea"/>
              </el-form-item>
              <el-form-item label="截止时间">
                <el-col :span="11">
                  <el-date-picker v-model="sizeForm.date1"
                                  type="date" placeholder="选择日期" style="width: 100%"
                                  value-format="YYYY-MM-DD"
                  ></el-date-picker>
                </el-col>
                <el-col class="line" :span="1">&nbsp;&nbsp;&nbsp;&nbsp;-</el-col>
                <el-col :span="12">
                  <el-time-picker v-model="sizeForm.date2" placeholder="选择时间" style="width: 100%"
                                  format="HH:mm" value-format="HH:mm"
                  ></el-time-picker>
                </el-col>
              </el-form-item>
              <el-form-item label="从属">
                <el-checkbox-group v-model="sizeForm.type">
                  <el-checkbox-button label="为自己添加"></el-checkbox-button>
                  <el-checkbox-button label="为他人添加"></el-checkbox-button>
                </el-checkbox-group>
              </el-form-item>

              <el-form-item label="自己的ID" v-show="forSelf">
                <el-input v-model="sizeForm.adder_id" placeholder="暂时这样，以后会自动填入且无法修改"></el-input>
              </el-form-item>

              <el-form-item label="别人的ID" v-show="forOther">
                <el-input v-model="sizeForm.user_id" placeholder="这个地方没想好怎么填起来方便"></el-input>
              </el-form-item>

              <el-form-item size="large">
                <el-button type="primary" @click="submitForm">立即创建</el-button>
                <el-button>取消</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div></el-col
      >
    </el-row>
  </div>
</template>
<script lang="ts">
import {computed, defineComponent, onMounted, reactive, ref} from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Edit, DeleteFilled, Check, ArrowLeft } from '@element-plus/icons-vue'
import Service from "@/views/TodoList/api";

export default defineComponent({
  name: 'AdvanceForm',
  components: {
    Edit,
    DeleteFilled,
    Check,
    ArrowLeft
  },
  setup() {
    const router = useRouter()

    const sizeForm = reactive({
      todo_title: '',
      todo_ctnt: '',
      date1: '',
      date2: '',
      type: [],
      adder_id: '',
      user_id: '',
    })

    const forSelf = computed(() => sizeForm.type.includes('为自己添加'));
    const forOther = computed(() => sizeForm.type.includes('为他人添加'));

    const activityForm = ref()

    onMounted(() => {
      // eslint-disable-next-line no-console
      console.log('show sizeFormRef.value', activityForm.value)
    })
    // methods
    const submitForm = () => {
      activityForm.value.validate((valid: any): boolean => {
        if (valid) {
          let record = {
            todo_title: sizeForm.todo_title,
            todo_ctnt: sizeForm.todo_ctnt,
            todo_ddl: sizeForm.date1 + "-" + sizeForm.date2,
            adder_id: sizeForm.adder_id,
            user_id: sizeForm.user_id,
          }
          try {
            Service.addTodo(record).then((res) => {

            });
            ElMessage({
              type: 'success',
              message: '创建成功'
            })
          } catch (err) {
            ElMessage({
              type: 'warning',
              message: err.message
            })
            console.log('submit error')
            return false
          }
          sizeForm.todo_title = ''
          sizeForm.todo_ctnt = ''
          sizeForm.date1 = ''
          sizeForm.date2 = ''
          sizeForm.type = []
          sizeForm.adder_id = ''
          sizeForm.user_id = ''
          return true
        }
        console.log('submit error')
        return false
      })
    }
    const resetForm = () => {
      activityForm.value.resetFields()
    }
    const handleBack = () => {
      router.go(-1)
    }
    const handleEdit = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      tableData[index].edit = true
    }
    /**
     * @description  useXXX写法,代替mixin有待改进的地方
     * */
    const checkEmpty = (row: any) => {
      const result = Object.keys(row).some((key) => row[key] === '')
      return result
    }
    const handleSave = (index: any, row: any): Boolean => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      if (checkEmpty(row)) {
        ElMessage.warning('保存前请完善信息！')
        return false
      }
      // save current row data and update table data;
      tableData[index].edit = false
      tableData[index] = row
      ElMessage({
        type: 'success',
        message: '保存成功'
      })
      return true
    }
    const handleDelete = (index: any, row: any) => {
      // eslint-disable-next-line no-console
      console.log(index, row)
      tableData.splice(index, 1)
    }
    // 新增一条记录
    const handleAddRecord = () => {
      tableData.push({
        province: '',
        city: '',
        name: '',
        address: '',
        edit: true
      })
    }
    return {
      handleAddRecord,
      handleEdit,
      handleSave,
      handleDelete,
      handleBack,
      sizeForm,
      activityForm,
      submitForm,
      resetForm,
      forSelf,
      forOther,
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
