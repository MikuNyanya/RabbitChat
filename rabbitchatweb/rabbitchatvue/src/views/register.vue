<script>

import axios from "axios";
import { ElMessage } from 'element-plus'

export default {
    data() {
        return {
            useImg: "",
            name: "",
            account: "",
            pwd: "",
            errorMsg: "",
            logoList: [],
        }
    },
    mounted() {
        // 初始化
        this.getLogoList();
    },
    methods: {
        register: function () {
            console.log(this.useImg);

            let that = this;
            //执行登录 并获取授权
            axios.post('api/user/register', {
                account: this.account,
                password: this.pwd,
                name: this.name,
                userImg: this.useImg,
            }).then(function (response) {
                if (response.data.status != 0) {
                    ElMessage.error(response.data.errorMessage)
                    return;
                }

                ElMessage.success("注册成功 3秒后返回登录页面")

                setTimeout(() => {
                    that.$router.push('/login')
                }, 3000);
            })
        },
        getLogoList() {
            let that = this;
            axios.get('api/user/logoList').then(function (response) {
                that.logoList = JSON.parse(response.data.data);
            })
        },
        parseLogoUrl(str) {
            return "/src/assets/img/" + str;
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
                        <el-text class="msgLab" type="danger">{{ errorMsg }}</el-text>
                    </el-form-item>
                    <el-form-item class="labelColor" label="选择兔子">
                        <el-radio-group v-model="useImg">
                            <el-scrollbar class="logoScr">
                                <div class="userLogo">
                                    <el-radio-button class="logoRad" v-for="item in logoList" :value=item>
                                        <el-avatar class="logoAva" :src="parseLogoUrl(item)"/>
                                    </el-radio-button>
                                </div>
                            </el-scrollbar>
                        </el-radio-group>
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
    background-color: rgba(255, 255, 255, 0.3);
    padding: 10px;
    border-radius: 20px;
}

.input {
    width: 300px;
    margin: 5px;
}

.userLogo {
    width: 350px;
    height: 130px;
    line-height: 120px;
    display: flex;
}

.logoScr {
    display: flex;
    background-color: rgba(255, 255, 255, 0.3);
    border-radius: 10px;
}

.logoRad {
    padding: 5px;
}

.logoAva {
    display: flex;
    width: 80px;
    height: 80px;
}


:deep(.logoRad .el-radio-button__inner) {
    background-color: #faf8f8;
    border-radius: 50%;
    padding: 5px;
}

:deep(.logoRad:first-child .el-radio-button__inner) {
    border-radius: 50%;
}

:deep(.logoRad:last-child .el-radio-button__inner) {
    border-radius: 50%;
}
</style>