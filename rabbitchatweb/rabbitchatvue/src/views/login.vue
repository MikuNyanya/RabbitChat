<script>

import axios from "axios";

export default {
    data() {
        return {
            account: "rabbit",
            pwd: "rabbit",
            errorMsg: ""
        }
    },
    mounted() {
        // 初始化

    },
    methods: {
        login: function () {
            // axios.get('api/test/test').then(function (response) {
            //
            // })
            let that = this;
            //执行登录 并获取授权
            axios.post('api/user/login', {account: this.account, password: this.pwd}).then(function (response) {
                if (response.data.status != 0) {
                    that.errorMsg = response.data.errorMessage;
                    return;
                }
                let loginData = JSON.parse(response.data.data);

                sessionStorage.setItem("chatAuth",loginData.chatAuth);
                sessionStorage.setItem("uid",loginData.uid);
                that.$router.push('/chat')
            })
        },
        goRegister(){
            this.$router.push('/register')
        }
    },
    destroyed() {

    }
}
</script>

<template>
    <div>
        <input class="msgLab" v-model="errorMsg" disabled/>
        <br/>
        <input id="uid" v-model="account" style="width: 200px"/>
        <br/>
        <input id="authStr" v-model="pwd" style="width: 200px" type="password"/>
        <br/>
        <button @click="login">登录</button> <button @click="goRegister">注册</button>
    </div>
</template>

<style scoped>
.msgLab {
    background: transparent;
    border: none;
    color: white;
    font-size: larger;
}
</style>
