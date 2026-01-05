<template>
  <el-card shadow="never" class="about-card">
    <template #header>
      <h2>关于系统</h2>
    </template>
    <div class="about-content">
      <el-descriptions column="1" border>
        <el-descriptions-item label="系统名称">学生管理系统</el-descriptions-item>
        <el-descriptions-item label="系统版本">v1.0.0</el-descriptions-item>
        <el-descriptions-item label="主要功能">
          <ul class="ml-4 list-disc">
            <li>学生信息管理（添加、编辑、删除、查询）</li>
            <li>课程信息管理（添加、编辑、删除、查询）</li>
            <li>分页与条件搜索</li>
            <li>批量操作功能</li>
          </ul>
        </el-descriptions-item>
        <el-descriptions-item label="技术栈">
          <div class="flex flex-wrap gap-2">
            <el-tag>Vue 3</el-tag>
            <el-tag>Element Plus</el-tag>
            <el-tag>Vue Router</el-tag>
            <el-tag>Axios</el-tag>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="使用说明">
          通过左侧导航菜单选择需要操作的功能模块，系统支持键盘快捷键和鼠标操作。
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </el-card>
</template>

<script setup>
// 关于页面无需额外逻辑
</script>

<style scoped>
.about-card {
  margin-top: 20px;
  padding: 20px;
}

.about-content {
  margin-top: 10px;
}

/* 替换 ::v-deep 为 :deep() */
:deep(.el-descriptions__cell) {
  padding: 15px 10px;
}

:deep(.el-descriptions__label) {
  font-weight: 500;
}
</style>