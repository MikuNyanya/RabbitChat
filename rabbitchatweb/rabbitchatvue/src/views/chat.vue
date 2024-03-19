<script>

export default {
    data() {
        return {
            testText: "测试",
            path: "ws://localhost:21010/websocket/",
            socket: "",
            authStr:"FjMmc0CNlzWI4WsaaHGO0Yhz/gm4pEpjpBsxM24awr2/xBoiAh0tpw==",
            uid: "1",
            toUid: "2",
            message: "这里填写消息",
        }
    },
    mounted() {
        // 初始化
        // this.init()
    },
    methods: {
        init: function () {
            if (typeof (WebSocket) === "undefined") {
                alert("您的浏览器不支持socket")
            } else {
                // 实例化socket
                this.socket = new WebSocket(this.path+this.uid+"?chatAuth="+encodeURIComponent(this.authStr))
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
                chatAuth:this.authStr,
                messageSendType:1,
                message:message,
                fromUid:this.uid,
                toUid:this.toUid
            }

            this.socket.send(JSON.stringify(msgInfo))
        },
        close: function () {
            console.log("socket已经关闭")
        },
        destroy:function (){
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

<template>
    <div>
        <textarea id="testText" v-model="testText" style="z-index: 1;width: 500px;height: 100px" disabled/>
        <br/>
        <input id="uid" v-model="uid" style="width: 1000px"/>
        <br/>
        <input id="authStr" v-model="authStr" style="width: 1000px"/>
        <br/>
        <input id="path" v-model="path" style="width: 1000px"/>
        <br/>
        <button @click="init()">连接</button>
        <br/>
        <button @click="close()">退出</button>
        <br/><br/>
        <textarea id="message" v-model="message" style="z-index: 1;width: 500px;height: 100px"/>
        <br/>
        <input id="pwd" v-model="toUid"/>
        <br/>
        <button @click="send(this.message)">发送</button>

    </div>
</template>

<style scoped>
h1 {
    font-weight: 500;
    font-size: 2.6rem;
    position: relative;
    top: -10px;
}

h3 {
    font-size: 1.2rem;
}

.greetings h1,
.greetings h3 {
    text-align: center;
}

@media (min-width: 1024px) {
    .greetings h1,
    .greetings h3 {
        text-align: left;
    }
}
</style>
