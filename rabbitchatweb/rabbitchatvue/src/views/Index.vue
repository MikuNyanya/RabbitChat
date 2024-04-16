<script>

export default {
    data() {
        return {}
    },
    mounted() {
        // 初始化

    },
    methods: {
        open: function () {
            // this.$router.push("/login");
            this.$router.push("/chat");
        }

    },
    destroyed() {

    }
}
</script>

<template>
    <div class="content">
        <el-row class="row" type="flex" justify="center" align="middle">
            <el-col>
                <el-button class="bigbutton" type="primary" @click="open">兔 子 兔 子 兔</el-button>
            </el-col>
        </el-row>
    </div>
</template>

<style scoped>
.content {
    width: 100%;
    height: 100%;
}

.row {
    height: 100%;
    width: 100%;
    text-align: center;
}
.bigbutton {
    font-size: 30px;
    width: 300px;
    height: 100px;
    background-color: rgba(8, 176, 230, 0.9);
    border: 0;
    border-radius: 20px;
}
</style>
