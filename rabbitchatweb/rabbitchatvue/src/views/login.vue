<script>
import axios from "axios";

export default {
    data() {
        return {
            account: "",
            pwd: "",
            errorMsg:""
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
            //
            // axios.post('api/user/login',{account:this.account,password:this.pwd}).then(function (response) {
            //
            // })

            this.$router.push('/chat')
        }

    },
    destroyed() {

    }
}
</script>

<template>
    <div>
        <input id="uid" v-model="account" style="width: 200px"/>
        <br/>
        <input id="authStr" v-model="pwd" style="width: 200px"/>
        <br/>
        <button @click="login">登录</button>

    </div>
</template>

<style scoped>

</style>
