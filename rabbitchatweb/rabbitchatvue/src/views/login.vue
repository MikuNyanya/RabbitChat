<script>

import axios from "axios";

export default {
    data() {
        return {
            account: "",
            pwd: "",
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

                sessionStorage.setItem("chatAuth", loginData.chatAuth);
                sessionStorage.setItem("uid", loginData.uid);
                that.$router.push('/chat')
            })
        },
        goRegister() {
            this.$router.push('/register')
        }
    },
    destroyed() {

    }
}
</script>

<template>
    <div class="content">

        <el-row class="row" type="flex" justify="center" align="middle">
            <el-col :span="12" :offset="6">
                <el-form label-width="100px" class="formc">
                    <el-form-item>
                        <el-text class="msgLab" type="danger">{{errorMsg}}</el-text>
                    </el-form-item>
                    <el-form-item class="labelColor" label="账号">
                        <el-input class="input" v-model="account"/>
                    </el-form-item>
                    <el-form-item class="labelColor" label="密码">
                        <el-input class="input" v-model="pwd" type="password"/>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="login" type="primary" style="margin-left: 20px">登录</el-button>
                        <el-button @click="goRegister">注册</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>


    </div>
</template>

<style scoped>
.msgLab {
    //border: none;
    //color: #f38787;
    font-size: 25px;
}

.content {
    width: 100%;
    height: 100%;
}

.row {
    height: 100%;
    width: 100%;
    text-align: center;
}
.formc {
    width: 400px;
    background-color: rgba(255,255,255,0.3);
    padding: 10px;
    border-radius: 20px;
}

.input {
    width: 200px;
    margin: 5px;
}


</style>

<style>
.labelColor .el-form-item__label{
    color: white;
}

</style>
