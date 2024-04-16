<template>
    <el-container class="chat_container">
        <el-aside width="200px">
            <el-menu>
                <el-scrollbar>
                    <!--                    <el-menu-item>个人信息</el-menu-item>-->
                </el-scrollbar>
            </el-menu>

        </el-aside>
        <el-main style="padding: 5px">
            <div class="chat_main" v-if="chat_hide">
                <div class="chat_head">
                    <div style="text-align: center;font-size: 25px;color: white">兔子窝</div>
                    <!--                    <el-button class="chat_close" @click="close_chat">X</el-button>-->
                </div>

                <el-scrollbar class="chat_history" ref="chatHistoryScrollbar">
                    <div class="chat_history" v-html="chatHistoryHtml" ref="chatHistory"></div>
                </el-scrollbar>
                <div class="chat_tool">
                    <el-button class="msg_send" type="primary" @click="send(message)">发送
                    </el-button>
                </div>
                <div class="chat_input">
                    <textarea id="message" v-model="message"/>
                </div>
            </div>
        </el-main>

        <el-aside width="200px">
            <h5 class="userListTitle">用户列表</h5>
            <el-scrollbar class="userListScrollbar">
                <div class="userList" v-html="userListHtml"></div>
            </el-scrollbar>
        </el-aside>
    </el-container>
</template>


<script>

export default {
    data() {
        return {
            testText: "测试",
            path: "ws://localhost:21010/websocket/",
            imgpath: "/src/assets/img/",
            socket: "",
            authStr: "",
            uid: "",
            toUid: "1",
            toGroupId: "0",
            userName: "",
            userImg: "",
            message: "这里填写消息",
            chat_hide: true,
            //聊天框消息内容
            chatHistoryHtml: "",
            chatHistoryPage: 1,
            userListHtml: "",
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
            console.log("接收到服务器消息")
            console.log(msg.data);
            this.testText = msg.data;

            let dataObj = JSON.parse(msg.data);

            if (dataObj.message == "与服务器连接成功") {
                this.getUserList();
                this.getUserInfo();
                this.getChatRecordHistory();
            }

            if (dataObj.messageSendType === 1 || dataObj.messageSendType === 2) {
                if (dataObj.fromUid === this.uid) {
                    //自己的消息
                    // let msg_temp = "<div class=\"msg_main_r\">\n" +
                    //     "<div class=\"msg_logo_r\" style=\"background-image: url(/src/assets/img/rabbit_logo.jpg);\"></div>\n" +
                    //     // "<div class=\"msg_nick_r\">"+ dataObj.fromUname + "</div>\n" +
                    //     "<br/>\n" +
                    //     "<br/>\n" +
                    //     "<div class=\"msg_msg_r\" style=\"width: "+msg_width_css+"\">\n"+ dataObj.message + "</div>\n" +
                    //     "</div>";
                    // this.chatHistoryHtml += msg_temp
                } else {
                    this.chatAdd(dataObj.fromUserImg, dataObj.fromUname, dataObj.message);
                }

                //消息框滚动到最底部
                //新消息需要先添加进去，再刷新滚动条，所以延迟一点时间刷新
                setTimeout(() => {
                    this.$refs.chatHistoryScrollbar.setScrollTop(this.$refs.chatHistory.scrollHeight);
                    console.log(this.$refs.chatHistory.scrollHeight + " " + this.$refs.chatHistory.clientHeight)
                }, 1);
            }

            if (dataObj.messageSendType === 0) {
                //服务器消息

            }

            if (dataObj.messageSendType === 3) {
                //用户列表
                let userList = JSON.parse(dataObj.message);
                console.log(userList);
                for (let i in userList) {
                    let u = userList[i];

                    let userListHtml = " <div class=\"userInfo\">\n" +
                        "                    <div class=\"ulistlogo\" style=\"background-image: url(" + this.imgpath + u.userImg + ");\"></div>\n" +
                        "                    <div class=\"ulistname\" >" + u.name + "</div>\n" +
                        "                </div>";

                    this.userListHtml += userListHtml
                }
            }

            if (dataObj.messageSendType === 4) {
                let userInfo = JSON.parse(dataObj.message);
                this.userName = userInfo.name;
                this.userImg = userInfo.userImg;
            }

            if (dataObj.messageSendType === 5) {
                let chatHistory = JSON.parse(dataObj.message);
                for (let i in chatHistory) {
                    this.chatAdd(chatHistory[i].fromUserImg, chatHistory[i].fromUserName, chatHistory[i].content,5);
                }

                setTimeout(() => {
                    this.$refs.chatHistoryScrollbar.setScrollTop(this.$refs.chatHistory.scrollHeight);
                }, 1);
            }
        },
        // 发送消息给被连接的服务端
        send: function (message) {
            var msgInfo = {
                chatAuth: this.authStr,
                messageSendType: 2,
                message: message,
                fromUid: this.uid,
                toUid: this.toUid,
                toGroupId: this.toUid
            }

            let msg_width_css = "auto";
            if (msgInfo.message.length > 40) {
                msg_width_css = "45%";
            }
            //在聊天框加入自己的消息
            // let msg_temp = "<div class=\"msg_main_r\">\n" +
            //     "<div class=\"msg_logo_r\" style=\"background-image: url(/src/assets/img/rabbit_logo.jpg);\"></div>\n" +
            //     // "<div class=\"msg_nick_r\">"+ dataObj.fromUname + "</div>\n" +
            //     "<br/>\n" +
            //     "<br/>\n" +
            //     "<div class=\"msg_msg_r\" style=\"width: "+msg_width_css+"\">\n"+ msgInfo.message + "</div>\n" +
            //     "</div>";
            let msg_temp = "<div class=\"msg_main_other\">\n" +
                "<div class=\"msg_logo\" style=\"background-image: url(" + this.imgpath + this.userImg + ");\"></div>" +
                "<div class=\"msg_nick\">" + this.userName + "</div>\n" +
                "<br/>\n" +
                "<br/>\n" +
                "<div class=\"msg_msg\" style=\"width: " + msg_width_css + "\">" + msgInfo.message + "</div>\n" +
                "</div>";
            this.chatHistoryHtml += msg_temp

            setTimeout(() => {
                this.$refs.chatHistoryScrollbar.setScrollTop(this.$refs.chatHistory.scrollHeight);
            }, 1);

            this.socket.send(JSON.stringify(msgInfo))
        },
        getUserList() {
            var msgInfo = {
                chatAuth: this.authStr,
                messageSendType: 3,
                fromUid: this.uid
            }

            this.socket.send(JSON.stringify(msgInfo))
        },
        getUserInfo() {
            var msgInfo = {
                chatAuth: this.authStr,
                messageSendType: 4,
                fromUid: this.uid
            }

            this.socket.send(JSON.stringify(msgInfo))
        },
        getChatRecordHistory() {
            var param = {
                toId: 0,
                page: this.chatHistoryPage,
                pageSize: 20,
            }

            var msgInfo = {
                chatAuth: this.authStr,
                messageSendType: 5,
                fromUid: this.uid,
                message: JSON.stringify(param),
            }
            this.chatHistoryPage++;
            this.socket.send(JSON.stringify(msgInfo))
        },
        close: function () {
            console.log("socket已经关闭")
        },
        destroy: function () {
            // 销毁监听
            this.socket.onclose = this.close

        },
        close_chat() {
            this.chat_hide = false;
        },
        chatAdd(logo, name, message,type) {
            let msg_width_css = "auto";
            if (message.length > 40) {
                msg_width_css = "45%";
            }

            //别人的消息
            let msg_temp = "<div class=\"msg_main_other\">\n" +
                "<div class=\"msg_logo\" style=\"background-image: url("+this.imgpath + logo + ");\"></div>" +
                "<div class=\"msg_nick\">" + name + "</div>\n" +
                "<br/>\n" +
                "<br/>\n" +
                "<div class=\"msg_msg\" style=\"width: " + msg_width_css + "\">" + message + "</div>\n" +
                "</div>";
            if(type && type === 5) {
                this.chatHistoryHtml= msg_temp + this.chatHistoryHtml;
            }else {
                this.chatHistoryHtml += msg_temp
            }

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
    width: 100%;
    height: 100%;
}

.chat_head {
    height: 5%;
    background: rgba(255, 100, 1, 1);
}

.chat_close {
    width: 20px;
    height: 20px;
    background: rgba(0, 0, 0, 0.5);
    float: right;
    margin: 10px;
    padding: 0;
    border: 0;
    border-radius: 50%;

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
}

:deep(.msg_main_other) {
    width: 100%;
    float: left;
    padding: 5px;
}

:deep(.msg_logo) {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    float: left;

    background-image: url(@/assets/img/rabbit_logo.jpg);
    background-size: cover; /*图片平铺*/
    background-repeat: no-repeat;
    background-position: center; /*居中显示*/
}

:deep(.msg_nick) {
    height: 40px;
    line-height: 40px;
    padding-left: 10px;
    float: left;

}

:deep(.msg_msg) {
    //width: 50%;
    margin-left: 30px;
    padding: 8px;
    border-radius: 10px;
    background: rgba(239, 96, 17, 0.7);
    float: left;
    word-wrap: break-word;
}

:deep(.msg_main_r) {
    width: 100%;
}

:deep(.msg_logo_r) {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    float: right;

    background-image: url(@/assets/img/rabbit_logo.jpg);
    background-size: cover; /*图片平铺*/
    background-repeat: no-repeat;
    background-position: center; /*居中显示*/
}

:deep(.msg_nick_r) {
    height: 40px;
    line-height: 40px;
    padding-right: 10px;
    float: right;
}

:deep(.msg_msg_r) {
    border-radius: 10px;
    background: rgba(239, 96, 17, 0.8);
    float: right;
}

.msg_send {
    float: right;
    margin: 5px 10px;
}

.userListTitle {
    height: 30px;
    font-size: 20px;
    color: white;
    text-align: center;
    line-height: 30px;
    border-radius: 0 0 50% 50%;
    background: rgba(239, 96, 17, 0.8);
}

.userListScrollbar {
    height: 90%;
}

.userList {
    width: 100%;
    height: 100%;
}

:deep(.userInfo) {
    width: 100%;
    height: 50px;
    line-height: 50px;
}

:deep(.ulistlogo) {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    float: left;
    margin: 5px;

    background-image: url(@/assets/img/IzumiKonata_logo.jpg);
    background-size: cover; /*图片平铺*/
    background-repeat: no-repeat;
    background-position: center; /*居中显示*/
}

:deep(.ulistname) {
    width: 60%;
    font-size: 15px;
    float: left;
    padding-left: 5px;

    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

#message {
    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, 0.4);
    border: 0;
    padding: 5px;
}
</style>
