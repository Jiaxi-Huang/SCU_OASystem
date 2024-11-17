import { RouteRecordRaw } from 'vue-router'

export default interface permissionStateTypes {
  username: String
  role: String
  department: String
  intro: String
  permissions: String[]
  accessRoutes: Array<RouteRecordRaw>
  routes: Array<RouteRecordRaw>
  authedRoutes: String[]
  // eslint-disable-next-line semi
}
