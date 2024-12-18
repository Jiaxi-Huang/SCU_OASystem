import { RouteRecordRaw } from 'vue-router'

export default interface permissionStateTypes {
  email: String
  username: String
  role: String
  department: String
  intro: String
  phone: String
  //permissions: String[]
  accessRoutes: Array<RouteRecordRaw>
  routes: Array<RouteRecordRaw>
  authedRoutes: String[]
  avatar: String
  // eslint-disable-next-line semi
}
