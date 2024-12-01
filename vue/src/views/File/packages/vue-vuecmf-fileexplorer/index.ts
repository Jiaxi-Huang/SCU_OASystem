
import VuecmfFileexplorer from './src/VuecmfFileexplorer.vue'
import {App} from 'vue'
import 'element-plus/dist/index.css'

/**
 * 为组件提供 install 安装方法，供按需引入
 * @param app
 */
VuecmfFileexplorer.install = (app: App):void => {
    if(VuecmfFileexplorer.installed) return
    VuecmfFileexplorer.installed = true
    app.component(VuecmfFileexplorer.name, VuecmfFileexplorer)
}

// 默认导出组件
export default VuecmfFileexplorer
