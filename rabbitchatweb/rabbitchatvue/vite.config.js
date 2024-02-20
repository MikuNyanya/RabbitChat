import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
//https://blog.csdn.net/weixin_45795521/article/details/131416086
export default defineConfig({
    base: "/", //资源引用路径
    plugins: [
        vue(),
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    server: {
        port: 21020, //端口号 默认5173
        host: "localhost",
        https: false, //是否开启https
        open: true, //服务启动时自动在浏览器打开应用
        // proxy: { //配置多个代理
        //     '/dev-api': {
        //         target: "http://localhost/",
        //         changeOrigin: true,///设置访问目标地址允许跨域
        //         rewrite: (p) => p.replace(/^\/dev-api/, '')
        //     },
        //     '/prod-api': {
        //         target: "https://xxxx.com/",
        //         changeOrigin: true,///设置访问目标地址允许跨域
        //         rewrite: (p) => p.replace(/^\/prod-api/, '')
        //     },
        // },
    },
    build: {}
})
