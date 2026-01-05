<template>
  <div class="f-aside">
    <el-menu
      router
      :default-active="currentRoutePath"
      class="el-menu-vertical-demo"
    >
      <el-sub-menu index="1">
        <template #title>
          <el-icon><Location /></el-icon>
          <span>首页</span>
        </template>
        <el-menu-item index="/index">首页</el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="2">
        <template #title>
          <el-icon><User /></el-icon>
          <span>学生管理</span>
        </template>
        <el-menu-item index="/student/list">学生列表</el-menu-item>
        <el-menu-item index="/student/input">新生录入</el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="3">
        <template #title>
          <el-icon><Document /></el-icon>
          <span>课程管理</span>
        </template>
        <el-menu-item index="/course/list">课程列表</el-menu-item>
        <el-menu-item index="/course/input">课程添加</el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="4">
        <template #title>
          <el-icon><OfficeBuilding /></el-icon>
          院系管理
        </template>
        <el-menu-item index="/department/list">院系列表</el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="5">
        <template #title>
          <el-icon><Location /></el-icon>
          <span>关于</span>
        </template>
        <el-menu-item index="/about">关于</el-menu-item>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<script setup>
import { useRoute, onBeforeRouteUpdate } from "vue-router";
import { ref } from "vue";

// 定义响应式变量存储当前路由
const currentRoutePath = ref("");
const route = useRoute();

// 初始化当前路由
currentRoutePath.value = route.path;

// 监听路由变化，实时更新选中状态
onBeforeRouteUpdate((to) => {
  currentRoutePath.value = to.path;
});
</script>

<style>
.f-aside {
  width: 250px;
  top: 64px;
  bottom: 0;
  left: 0;
  position: fixed;
  background-color: #fff;
  overflow-y: auto;
}
</style>