<template>
    <el-container class="chat_container">
        <el-aside width="200px">
            <el-menu>
                <el-scrollbar>
                    <el-menu-item>个人信息</el-menu-item>
                </el-scrollbar>
            </el-menu>

        </el-aside>
        <el-main>
            <div class="chat_main">
                <div class="chat_history">
                    <div class="msg_main_other">
                        <div class="msg_logo">头像</div>
                        <div class="msg_nick">昵称</div>
                        <div class="msg_msg" >消息文本显示消息文本显示消息文本显示</div>
                    </div>

                    <textarea id="testText" v-model="testText" style="z-index: 1;width: 500px;height: 100px" disabled/>
                </div>
                <div class="chat_tool">

                </div>
                <div class="chat_input">
                    <textarea id="message" v-model="message"/>
                </div>

                <el-button type="primary" @click="send(this.message)" style="float: right;margin-top: 5px">发送
                </el-button>
            </div>
        </el-main>
    </el-container>
</template>


<script>

export default {
    data() {
        return {
            testText: "测试",
            path: "ws://localhost:21010/websocket/",
            socket: "",
            authStr: "",
            uid: "",
            toUid: "",
            message: "这里填写消息",
        }
    },
    mounted() {
        // 初始化
        this.init()
    },
    methods: {
        init: function () {
            //判断是否已登录 未登录则转到登录页面
            this.authStr = sessionStorage.getItem("chatAuth");
            this.uid = sessionStorage.getItem("uid");
            if (this.authStr == null || this.authStr === ""
                || this.uid == null || this.uid === "") {
                this.$router.push('/login')
            }
            console.log("用户已登录")

            if (typeof (WebSocket) === "undefined") {
                alert("您的浏览器不支持socket")
            } else {
                // 实例化socket
                this.socket = new WebSocket(this.path + this.uid + "?chatAuth=" + encodeURIComponent(this.authStr))
                // 监听socket连接
                this.socket.onopen = this.open
                // 监听socket错误信息
                this.socket.onerror = this.error
                // 监听socket消息
                this.socket.onmessage = this.getMessage
            }
        },
        open: function () {
            console.log("socket连接成功")
            this.testText = "socket连接成功";
        },
        error: function () {
            console.log("连接错误")
        },
        getMessage: function (msg) {
            console.log(msg.data);
            this.testText = msg.data;
        },
        // 发送消息给被连接的服务端
        send: function (message) {
            var msgInfo = {
                chatAuth: this.authStr,
                messageSendType: 1,
                message: message,
                fromUid: this.uid,
                toUid: this.toUid
            }

            this.socket.send(JSON.stringify(msgInfo))
        },
        close: function () {
            console.log("socket已经关闭")
        },
        destroy: function () {
            // 销毁监听
            this.socket.onclose = this.close

        }
    },
    destroyed() {
        // 销毁监听
        this.socket.onclose = this.close
    }
}

</script>

<style scoped>
.chat_container .el-aside {
    background: rgba(255, 255, 255, 0.8);
}

.chat_container .el-main {
    padding: 70px;
}

.chat_container .el-menu-item{
}

.chat_container {
    height: 100%;
}

.chat_main {
    width: 80%;
    height: 100%;
}

.chat_history {
    background: rgba(255, 255, 255, 0.3);
    width: 100%;
    height: 70%;
}

.chat_tool {
    width: 100%;
    height: 5%;
    background: rgba(255, 255, 255, 0.8);
}

.chat_input {
    background: rgba(255, 255, 255, 0.3);
    width: 100%;
    height: 20%;
}

.msg_main_other {
    background: rgba(255, 255, 155, 0.3);
}

.msg_logo {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    float: left;

    background-image: url(@/assets/img/bg.jpg);
    background-size: cover; /*图片平铺*/
    background-repeat: no-repeat;
    background-position: center; /*居中显示*/
}

.msg_nick {

}

.msg_msg {

}

</style>
