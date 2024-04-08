import './assets/main.css'


import {createApp} from 'vue'
import App from './App.vue'
import axios from "axios";
import router from './router/router.js'
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';

const app = createApp(App)

//注册路由
app.use(router)

//ElementUI
app.use(ElementPlus)

//全局axios
app.config.globalProperties.$axios = axios

app.mount('#app')
