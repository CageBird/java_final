<template>
  <div class="f-header">
    <span class="logo">
      <el-icon class="mr-2" :size="30">
        <ElementPlus />
      </el-icon>
      <span class="flex-1 mr-2"> 学生管理系统 </span>
      <el-icon class="icon-btn" @click="DoRefresh">
        <Refresh />
      </el-icon>
    </span>
  </div>
</template>

<script setup>
const DoRefresh = () => location.reload();
</script>

<style scoped>

.f-header {
  @apply flex items-center bg-indigo-500 text-light-50 fixed top-0 left-0 right-0 text-2xl;
  height: 64px;
  z-index: 9999;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15); 
}

.logo {
  @apply flex justify-center items-center;
}

.icon-btn {
  @apply flex justify-center items-center;
  height: 64px;
  cursor: pointer;
}

:global(.page-content) {
  padding-top: 64px;
}
</style>