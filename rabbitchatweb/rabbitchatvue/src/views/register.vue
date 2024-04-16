<script>

import axios from "axios";

export default {
    data() {
        return {
            name: "",
            account: "",
            pwd: "",
            errorMsg: "",
            url:"",
            logoList:[],
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
            axios.post('api/user/register', {
                account: this.account,
                password: this.pwd,
                name: this.name
            }).then(function (response) {
                if (response.data.status != 0) {
                    that.errorMsg = response.data.errorMessage;
                    return;
                }
                that.$router.push('/login')
            })
        },
        getLogoList() {
            let that = this;
            axios.get('api/test/test').then(function (response) {
                let temp = JSON.parse(response.data);
                that.url = temp.url;
                that.logoList = JSON.parse(temp.logoList);


            })
        },
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
                        <el-text class="msgLab" type="danger">{{ errorMsg }}</el-text>
                    </el-form-item>
                    <el-form-item class="labelColor" label="昵称">
                        <el-input class="input" v-model="name"/>
                    </el-form-item>
                    <el-form-item class="labelColor" label="账号">
                        <el-input class="input" v-model="account"/>
                    </el-form-item>
                    <el-form-item class="labelColor" label="密码">
                        <el-input class="input" v-model="pwd" type="password"/>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="register" type="primary" style="margin-left: 100px">注册</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </div>
</template>

<style scoped>
.msgLab {
    font-size: 20px;
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
    width: 500px;
    background-color: rgba(255,255,255,0.3);
    padding: 10px;
    border-radius: 20px;
}

.input {
    width: 300px;
    margin: 5px;
}
</style>
