<template>
  <div v-loading="loading" class="new">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="100px" title="新增员工">
      <el-form-item label="名称">
        <span v-if="row && row.userName">{{ row.userName }}</span>
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
        <el-button size="mini" type="primary" @click="saveData()"> <i class="fa fa-plus"> </i> 修改 </el-button>
      </el-row>
    </el-form>
  </div>
</template>
<script lang="ts">
import { computed, defineComponent, onMounted, watchEffect, reactive, toRef, toRefs } from 'vue'
import { useStore } from '@/store'
import Service from './api/index'

interface stateTypes {
  url: String
  purl: String
  loading: Boolean
  form: {
    userName:string
    attendanceDate:string
    checkIn:string
    inLocation:string
    checkOut:string
    outLocation:string
  }
  menu: {
    loading: Boolean
    url: String
    data: { key: String; label: String }[]
    form: String[]
  }
}
export default defineComponent({
  name: 'RolesEdit',
  props: {
    currentRow: {
      type: Object,
      default: () => ({ userName:'',attendanceDate:'',checkIn:'',inLocation:'',checkOut: '',outLocation:''})
    }
  },
  emits: ['success'],

  setup(props, { emit }) {
    // 析构获取 props 属性 basePath
    const currentRow = toRef(props, 'currentRow')
    const store = useStore()
    const lang = computed(() => store.getters['settingsModule/getLangState'])

    const state = reactive<stateTypes>({
      url: `/role/allow`,
      purl: `/role/permissions`,
      loading: false,
      form: {
        userName: '',
        attendanceDate:'',
        checkOut: '',
        inLocation:'',
        checkIn: '',
        outLocation:''
      },
      menu: {
        loading: false,
        url: `/menu/list`,
        data: [],
        form: []
      }
    })
    const rules = {
      userId: [
        { required: true, message: '请输入员工ID', trigger: 'blur' },
        { type:'number', message: '请输入有效数字', trigger: 'blur' }
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

    const row = computed(() => currentRow.value)
    /**
     * @description 保存当前角色信息
     */
    const saveData = async() => {
      console.log('form is ', state.menu.form)
      //  省略接口：向后端接口传递已经授权菜单名称；  state.menu.form
      const data = {
        attendanceDate: state.form.attendanceDate,
        checkIn: state.form.checkIn,
        checkOut: state.form.checkOut,
        inLocation: state.form.inLocation,
        outLocation: state.form.outLocation,
        userId: row.value.userId,
        id: row.value.id
      }
      console.log(data);
      const res = await Service.postEditAttendance(data)
      if(res.status === 0) {
        emit('success')
      }
    }

    // 使用watchEffect 监听所用到的变化时做出的副作用反应；
    watchEffect(() => {
      if (row.value) {
        state.form.userName = row.value.userName
        state.form.checkOut = row.value.checkOut
        state.form.checkIn = row.value.checkIn
        state.form.inLocation = row.value.inLocation
        state.form.outLocation = row.value.outLocation
        state.form.attendanceDate = row.value.attendanceDate
      }
    })
    return {
      ...toRefs(state),
      rules,
      lang,
      locations,
      row,
      saveData
    }
  }
})
</script>
<style lang="stylus" scoped>
.btns {
  text-align: right;
}

.el-transfer {
  display: inline-block;
  text-align: left;
}

.transfer {
  text-align: center;
}

.card-header {
  text-align: left;
}
</style>
