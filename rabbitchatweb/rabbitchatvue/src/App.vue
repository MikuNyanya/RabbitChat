<script setup>
// import HelloWorld from './components/HelloWorld.vue'
import Index from './views/Index.vue'
import chat from './views/login.vue'

document.body.style.backgroundImage = "url(src/assets/img/bg.jpg)";
document.body.style.backgroundSize = "cover";
document.body.style.backgroundPosition = "center";
document.body.style.backgroundRepeat = "no-repeat";
</script>

<template>


    <div>
        <!--      <HelloWorld msg="You did it!" />-->
<!--        <Index/>-->
<!--        <chat/>-->
        <router-view/>
    </div>
</template>

<style scoped>
</style>

