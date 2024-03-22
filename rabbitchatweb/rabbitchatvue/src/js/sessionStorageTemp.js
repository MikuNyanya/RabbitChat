//sessionStorage存储的数据是在整个会话期间保持有效的
//这意味着无论导航到哪个路由，只要在同一个浏览器标签或窗口中，都可以访问和获取相同的数据
//sessionStorage是一个会话级别的存储，数据在同一会话内的不同页面之间共享。
export default {
    //添加一个session
    addsession(k, v) {
        sessionStorage.setItem(k, v);
    },
    //根据key获取session
    getSession(k) {
        return sessionStorage.getItem(k);
    },
    //根据key删除一个session
    deletsession(k) {
        sessionStorage.removeItem(k);
    }
}

//该文件只做模板 不在实际代码中使用