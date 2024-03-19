import './assets/main.css'


import {createApp} from 'vue'
import App from './App.vue'
import axios from "axios";
import router from './router/router.js'

const app = createApp(App)

//注册路由
app.use(router)

//全局axios
app.config.globalProperties.$axios = axios

app.mount('#app')
