<script>
import axios from "axios";

export default {
    data() {
        return {

        }
    },
    mounted() {
        // 初始化

    },
    methods: {
        open: function () {
            this.$router.push("/login");
        }

    },
    destroyed() {

    }
}
</script>

<template>
    <div>
        <button @click="open">跳转到登录</button>

    </div>
</template>

<style scoped>

</style>
