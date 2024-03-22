<script>

import axios from "axios";

export default {
    data() {
        return {
            name:"",
            account: "",
            pwd: "",
            errorMsg: ""
        }
    },
    mounted() {
        // 初始化

    },
    methods: {
        register: function () {
            // axios.get('api/test/test').then(function (response) {
            //
            // })
            let that = this;
            //执行登录 并获取授权
            axios.post('api/user/register', {account: this.account, password: this.pwd,name:this.name}).then(function (response) {
                if (response.data.status != 0) {
                    that.errorMsg = response.data.errorMessage;
                    return;
                }
                that.$router.push('/login')
            })
        }
    },
    destroyed() {

    }
}
</script>

<template>
    <div>
        <input class="msgLab" v-model="errorMsg" disabled/>
        <table style="color: white">
            <tr>
                <td><label class="msgLab">用户名</label></td>
                <td><input v-model="name" style="width: 200px"/></td>
            </tr>
            <tr>
                <td><label class="msgLab">账号</label></td>
                <td><input v-model="account" style="width: 200px"/></td>
            </tr>
            <tr>
                <td><label class="msgLab">密码</label></td>
                <td><input v-model="pwd" type="password" style="width: 200px"/></td>
            </tr>
        </table>
        <button @click="register">注册</button>
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
