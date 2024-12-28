<template>
  <div v-loading="loading" class="new">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="right" label-width="100px" title="新增员工">
      <el-form-item label="名称">
        <span v-if="row && row.userName">{{ row.userName }}</span>
      </el-form-item>
      <el-form-item label="上班打卡时间">
        <el-col :span="12">
          <el-time-picker v-model="form.checkIn" placeholder="选择时间" style="width: 100%"
                          format="HH:mm:ss" value-format="HH:mm:ss"
          ></el-time-picker>
        </el-col>
      </el-form-item>
      <el-form-item label="下班打卡时间">
        <el-col :span="12">
          <el-time-picker v-model="form.checkOut" placeholder="选择时间" style="width: 100%"
                          format="HH:mm:ss" value-format="HH:mm:ss"
          ></el-time-picker>
        </el-col>
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
    checkIn:string
    checkOut:string
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
      default: () => ({ userName:'',checkIn:'',checkOut: ''})
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
        checkOut: '',
        checkIn: ''
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
      checkIn: [
        { required: true, message: '请选择部门', trigger: 'change' },
      ],
      checkOut: [
        { required: true, message: '请输入员工职能', trigger: 'change' },
      ]
    }

    const row = computed(() => currentRow.value)
    /**
     * @description 保存当前角色信息
     */
    const saveData = async() => {
      console.log('form is ', state.menu.form)
      //  省略接口：向后端接口传递已经授权菜单名称；  state.menu.form
      const data = {
        checkIn: state.form.checkIn,
        checkOut: state.form.checkOut,
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
      }
    })
    return {
      ...toRefs(state),
      rules,
      lang,
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
