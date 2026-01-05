<template>
  <el-card shadow="never" class="welcome-card">
    <template #header>
      <h2>欢迎使用学生管理系统</h2>
    </template>
    <div class="welcome-content">
      <p>该系统提供学生信息和课程信息的管理功能，包括添加、编辑、查询和删除等操作。</p>
      <div class="function-list">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :span="12">
            <el-card class="function-card" @click="$router.push('/student/list')">
              <div class="flex items-center">
                <el-icon :size="36" class="mr-4 text-primary"><User /></el-icon>
                <div>
                  <h3>学生管理</h3>
                  <p class="text-gray-500 text-sm">管理学生基本信息、查询和维护</p>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="function-card" @click="$router.push('/course/list')">
              <div class="flex items-center">
                <el-icon :size="36" class="mr-4 text-primary"><Document /></el-icon>
                <div>
                  <h3>课程管理</h3>
                  <p class="text-gray-500 text-sm">管理课程信息、学分和授课教师</p>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { User } from '@element-plus/icons-vue';

</script>

<style scoped>
.welcome-card {
  margin-top: 20px;
  padding: 20px;
}

.welcome-content {
  font-size: 16px;
  line-height: 1.8;
}

.function-card {
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 20px;
}

.function-card:active {
  transform: translateY(-2px); /* 点击时轻微下沉 */
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.08);
}

.function-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.function-list {
  margin-top: 30px;
}
</style>