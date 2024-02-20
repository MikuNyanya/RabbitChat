<script setup>
defineProps({
  msg: {
    type: String,
    required: true
  }
})


</script>

<template>
  <div>
    <button ></button>
    <h1 class="green" style="z-index: 1">测试文本</h1>

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
