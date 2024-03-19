import {createRouter, createWebHashHistory} from "vue-router";
import index from '../views/Index.vue'
import chat from '../views/chat.vue'
import login from '../views/login.vue'

const routes = [
    {
        path: '/',
        name: index,
        component: index
    },
    {
        path: '/chat',
        component: chat
    },
    {
        path: '/login',
        component: login
    }
]

const router = createRouter({
    history:createWebHashHistory(),
    routes,
})

export default router