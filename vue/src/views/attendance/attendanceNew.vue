<template>
  <div v-loading="loading" class="new">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="100px" title="新增考勤">
      <el-form-item label="员工名" prop="userName">
        <el-input v-model="form.userName" placeholder="请输入员工名"></el-input>
      </el-form-item>

      <el-form-item label="考勤日期">
        <div>
          <el-col :span="30">
            <el-date-picker v-model="form.attendanceDate"
                            type="date" placeholder="选择日期" style="width: 100%"
                            value-format="YYYY-MM-DD"
            ></el-date-picker>
          </el-col>
        </div>
      </el-form-item>
      <el-form-item label="上班打卡时间">
        <el-col :span="12">
          <el-time-picker v-model="form.checkIn" placeholder="选择时间" style="width: 100%"
                          format="HH:mm:ss" value-format="HH:mm:ss"
          ></el-time-picker>
        </el-col>
      </el-form-item>
      <el-form-item label="上班打卡位置" prop="inLocation"  >
        <el-select v-model="form.inLocation" placeholder="请选择位置" >
          <el-option v-for="inLocation in locations" :key="inLocation.value" :label="inLocation.label" :value="inLocation.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="下班打卡时间">
        <el-col :span="12">
          <el-time-picker v-model="form.checkOut" placeholder="选择时间" style="width: 100%"
                          format="HH:mm:ss" value-format="HH:mm:ss"
          ></el-time-picker>
        </el-col>
      </el-form-item>
      <el-form-item label="下班打卡位置" prop="outLocation">
        <el-select v-model="form.outLocation" placeholder="请选择位置">
          <el-option v-for="outLocation in locations" :key="outLocation.value" :label="outLocation.label" :value="outLocation.value"></el-option>
        </el-select>
      </el-form-item>
      <el-row class="btn-container">
        <el-button size="mini" type="primary" @click="submitForm()"> <i class="fa fa-plus"> </i> 新增 </el-button>
      </el-row>
    </el-form>
  </div>
</template>
<script lang="ts">
import { defineComponent, reactive, toRefs, ref } from 'vue'
import Service from './api/index'
export default defineComponent({
  name: 'RoleNew',
  emits: ['success'],
  setup(props, { emit }) {
    const rules = {
      userName: [
        { required: true, message: '请输入员工名', trigger: 'blur' },
      ],
      attendanceDate: [
        { required: true, message: '请选择考勤日期', trigger: 'change' },
      ],
      checkIn: [
        { required: true, message: '请输入上班考勤时间', trigger: 'change' },
      ],
      checkOut: [
        { required: true, message: '请输入下班考勤时间', trigger: 'change' },
      ],
      inLocation: [
        { required: false, message: '请输入上班考勤位置', trigger: 'change' },
      ],
      outLocation: [
        { required: false, message: '请输入下班考勤位置', trigger: 'change' },
      ],
    }
    const locations = [
      { value: '内网ip', label: '内网ip' },
      { value: '成都', label: '成都' },
      { value: '上海', label: '上海' },
      { value: '北京', label: '北京' },
    ]
    const url = `/role/add`
    const formRef = ref()
    // 只将响应式数据进行响应式处理
    const state = reactive({
      form: {
        userName: '',
        attendanceDate: '',
        checkIn: '',
        checkOut:'',
        inLocation:'',
        outLocation:''
      },
      loading: false
    })
    /**
     * @description 提交新建角色处理函数
     */
    const submitForm = async() => {
      formRef.value.validate(async(valid: any) => {
        if (valid) {
          const data ={
            userName: state.form.userName,
            attendanceDate: state.form.attendanceDate,
            checkIn: state.form.checkIn,
            checkOut: state.form.checkOut,
            inLocation: state.form.inLocation,
            outLocation: state.form.outLocation
          }
          const res = await Service.postAddAttendance(data)
          if(res.status ===0) {
            emit('success')
          }
        }
      })
    }
    return {
      submitForm,
      rules,
      locations,
      formRef,
      url,
      ...toRefs(state)
    }
  }
})
</script>
<style lang="stylus" scoped></style>
