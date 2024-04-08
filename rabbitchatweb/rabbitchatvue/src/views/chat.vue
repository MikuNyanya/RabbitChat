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
            <div class="chat_main" v-if="chat_hide">
                <div class="chat_head">
                    <el-button class="chat_close" @click="close_chat">X</el-button>
                </div>
                <div class="chat_history">
                    <div class="msg_main_other">
                        <div class="msg_logo"></div>
                        <div class="msg_nick">这是兔子</div>
                        <br/>
                        <br/>
                        <div class="msg_msg">
                            消息文本息文本显示消文本息文本显示消息示消息示消息示消息示消息示消息示消息示消息示消息示消息示消息示消息文本显示消息文本显示
                        </div>
                    </div>
                    <div class="msg_main_r">
                        <div class="msg_logo_r"></div>
                        <div class="msg_nick_r">这还是兔子</div>
                        <br/>
                        <br/>
                        <div class="msg_msg_r">
                            消息文本息文本显示消文本息文本显示消息示消息示消息示消息示消息示消息示消息示消息示消息示消息示消息示消息文本显示消息文本显示
                        </div>
                    </div>

<!--                    <textarea id="testText" v-model="testText" style="z-index: 1;width: 500px;height: 100px" disabled/>-->
                </div>
                <div class="chat_tool">
                    <el-button class="msg_send" type="primary" @click="send(this.message)" style="">发送
                    </el-button>
                </div>
                <div class="chat_input">
                    <textarea id="message" v-model="message" style="width: 80%;height: 80%"/>
                </div>
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
            chat_hide:true,
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

        },
        close_chat(){
            this.chat_hide = false;
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

.chat_container .el-menu-item {
}

.chat_container {
    height: 100%;
}

.chat_main {
    width: 80%;
    height: 100%;
}

.chat_head{
    height: 5%;
    background: rgba(255, 100, 1, 1);
    border-radius: 20px 20px 0 0;
}

.chat_close{
    width: 20px;
    height: 20px;
    background: rgba(0, 0, 0, 0.5);
    float: right;
    margin: 10px;
    padding: 0;
    border: 0;
    border-radius:50%;

    line-height: 20px;
    text-align: center;
    color: white;
    font-size: 10px;
}

.chat_history {
    background: rgba(255, 255, 255, 0.5);
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
    border-radius: 0 0 20px 20px;
}

.msg_main_other {
//background: rgba(255, 255, 155, 0.3); padding: 10px;
}

.msg_logo {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    float: left;

    background-image: url(@/assets/img/rabbit_logo.jpg);
    background-size: cover; /*图片平铺*/
    background-repeat: no-repeat;
    background-position: center; /*居中显示*/
}

.msg_nick {
    height: 40px;
    line-height: 40px;
    padding-left: 10px;
    float: left;

//background: rgba(255, 1, 150, 0.3);
}

.msg_msg {
    width: 50%;
    margin-left: 30px;
    padding: 8px;
    border-radius: 10px;
    background: rgba(239, 96, 17, 0.8);
}

.msg_main_r {
//background: rgba(255, 255, 155, 0.3); padding: 10px;
    float: right;
}

.msg_logo_r {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    float: right;

    background-image: url(@/assets/img/rabbit_logo.jpg);
    background-size: cover; /*图片平铺*/
    background-repeat: no-repeat;
    background-position: center; /*居中显示*/
}

.msg_nick_r {
    height: 40px;
    line-height: 40px;
    padding-right: 10px;
    float: right;

//background: rgba(255, 1, 150, 0.3);
}

.msg_msg_r {
    width: 50%;
    margin-right: 30px;
    padding: 8px;
    border-radius: 10px;
    background: rgba(239, 96, 17, 0.8);
    float: right;
}

.msg_send{
    float: right;
    margin: 5px 10px;
}
</style>
