<template>
  <div class="department-container">
    <el-card shadow="never" class="title-card">
      <h2 class="title-text">院系列表</h2>
    </el-card>

    <!-- 功能标签页 -->
    <el-tabs v-model="activeTab" type="card" class="tabs-container">
      <el-tab-pane label="院系列表" name="list"></el-tab-pane>
      <el-tab-pane label="院系学生统计" name="count"></el-tab-pane>
    </el-tabs>

    <!-- 1. 院系列表标签页 -->
    <div v-if="activeTab === 'list'">
      <!-- 搜索框 -->
      <div class="flex items-center mb-4">
        <el-input
          v-model="searchId"
          size="small"
          placeholder="请输入院系ID查询"
          prefix-icon="el-icon-search"
          class="search-input"
          :style="{ width: '240px' }"
          @keyup.enter="getDepartmentById"
        />
        <el-button type="primary" size="small" @click="getDepartmentById">查询</el-button>
        <el-button size="small" @click="resetSearch">重置</el-button>
      </div>

      <el-card v-if="currentDepartment" shadow="never" class="detail-card">
        <template #header>
          <h3>院系详情（ID：{{ currentDepartment.departmentId }}）</h3>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="院系ID">{{ currentDepartment.departmentId }}</el-descriptions-item>
          <el-descriptions-item label="院系名称">{{ currentDepartment.departmentName }}</el-descriptions-item>
          <el-descriptions-item label="办公室位置">{{ currentDepartment.officeLocation || '无' }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentDepartment.phone || '无' }}</el-descriptions-item>
          <el-descriptions-item label="系主任">{{ currentDepartment.headName || '无' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentDepartment.createTime || '无' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 所有院系列表 -->
      <el-card shadow="never" class="list-card">
        <template #header>
          <h3>全部院系列表</h3>
        </template>
        <el-table
          :data="departmentList"
          border
          stripe
          v-loading="loading"
          style="width: 100%"
          element-loading-text="加载中..."
        >
          <el-table-column prop="departmentId" label="院系ID" width="80" />
          <el-table-column prop="departmentName" label="院系名称" width="150" />
          <el-table-column prop="officeLocation" label="办公室位置" />
          <el-table-column prop="phone" label="联系电话" />
          <el-table-column prop="headName" label="系主任" />
          <el-table-column prop="createTime" label="创建时间" width="180" />
        </el-table>
      </el-card>
    </div>

    <!-- 2. 院系学生统计标签页（新增搜索功能） -->
    <div v-if="activeTab === 'count'">
      <!-- 统计页搜索框：支持按ID/名称搜索 -->
      <div class="flex items-center mb-4">
        <el-input
          v-model="searchCountKey"
          size="small"
          placeholder="输入院系ID/名称搜索统计数据"
          prefix-icon="el-icon-search"
          class="search-input"
          :style="{ width: '240px' }"
          @keyup.enter="fetchFilteredCountData"
        />
        <el-button type="primary" size="small" @click="fetchFilteredCountData">查询</el-button>
        <el-button size="small" @click="resetCountSearch">重置</el-button>
      </div>

      <el-card shadow="never" v-loading="countLoading" class="count-card">
        <template #header>
          <h3>各院系学生数量统计</h3>
        </template>
        <el-table
          :data="filteredStudentCountList"
          border
          stripe
          style="width: 100%"
        >
          <el-table-column prop="departmentId" label="院系ID" width="50" />
          <el-table-column prop="departmentName" label="院系名称" width="150" />
          <el-table-column prop="studentCount" label="学生数量">
            <template #default="scope">
              <el-tag type="success">{{ scope.row.studentCount }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import request from '@/utils/request';
import { ref, onMounted, computed } from "vue";
import { ElMessage } from "element-plus"; 

const activeTab = ref("list");
const loading = ref(false);
const countLoading = ref(false);

// 院系列表搜索相关
const searchId = ref("");
const departmentList = ref([]);
const currentDepartment = ref(null);

// 统计页搜索相关（新增）
const searchCountKey = ref(""); // 统计页搜索关键词（ID/名称）
const studentCountList = ref([]); // 原始统计数据
// 计算属性：根据搜索关键词筛选统计数据
const filteredStudentCountList = computed(() => {
  if (!searchCountKey.value.trim()) {
    return studentCountList.value; // 无关键词返回全部
  }
  const key = searchCountKey.value.trim().toLowerCase();
  return studentCountList.value.filter(item => {
    // 兼容驼峰/下划线字段，支持按ID或名称搜索
    const deptId = (item.departmentId || item.department_id) + "";
    const deptName = (item.departmentName || item.department_name) + "";
    return deptId.includes(key) || deptName.toLowerCase().includes(key);
  });
});

// 1. 获取所有院系
const getDepartmentList = async () => {
  try {
    loading.value = true;
    const res = await request.get("/department/queryAll");
    departmentList.value = res || [];
  } catch (error) {
    ElMessage.error("获取院系列表失败：" + (error.message || "服务器错误"));
    console.error(error);
  } finally {
    loading.value = false;
  }
};

// 2. 院系列表按ID查询
const getDepartmentById = async () => {
  if (!searchId.value) {
    ElMessage.warning("请输入院系ID");
    return;
  }
  const departmentId = Number(searchId.value);
  if (isNaN(departmentId)) {
    ElMessage.error("院系ID必须是数字");
    return;
  }

  try {
    const res = await request.get(`/department/findById/${departmentId}`);
    currentDepartment.value = {
      departmentId: res.departmentId || res.department_id,
      departmentName: res.departmentName || res.department_name,
      officeLocation: res.officeLocation || res.office_location,
      phone: res.phone,
      headName: res.headName || res.head_name,
      createTime: res.createTime || res.create_time
    };
    if (!currentDepartment.value.departmentId) {
      ElMessage.info("未查询到该院系信息");
    }
  } catch (error) {
    console.error("按ID查询院系错误详情：", {
      status: error.response?.status,
      url: error.config?.url,
      message: error.message
    });
    const errMsg = {
      404: "查询接口不存在，请检查后端路径",
      500: "服务器内部错误，请检查后端SQL",
      400: "参数错误，院系ID格式不正确",
    }[error.response?.status] || `查询院系失败：${error.message}`;
    ElMessage.error(errMsg);
    currentDepartment.value = null;
  }
};

// 3. 院系列表重置
const resetSearch = () => {
  searchId.value = "";
  currentDepartment.value = null;
  ElMessage.success("重置成功");
};

// 4. 获取所有院系学生统计数据
const getStudentCount = async () => {
  try {
    countLoading.value = true;
    const res = await request.get("/department/countStudents");
    // 格式化原始数据（兼容下划线/驼峰）
    studentCountList.value = (res || []).map(item => ({
      departmentId: item.departmentId || item.department_id,
      departmentName: item.departmentName || item.department_name,
      studentCount: item.studentCount || item.student_count || 0
    }));
  } catch (error) {
    ElMessage.error("获取学生统计失败：" + (error.message || "服务器错误"));
    console.error(error);
    studentCountList.value = [];
  } finally {
    countLoading.value = false;
  }
};

// 5. 统计页查询（筛选）（新增）
const fetchFilteredCountData = () => {
  // 前端筛选，无需重新请求接口
  if (filteredStudentCountList.value.length === 0 && searchCountKey.value.trim()) {
    ElMessage.info("未查询到匹配的统计数据");
  } else {
    ElMessage.success("筛选成功");
  }
};

// 6. 统计页重置（新增）
const resetCountSearch = () => {
  searchCountKey.value = "";
  ElMessage.success("重置成功");
};

// 初始化加载
onMounted(() => {
  getDepartmentList();
  getStudentCount();
});
</script>

<style scoped>
.department-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px;
}

.title-card {
  margin-bottom: 16px;
}

.title-text {
  font-size: 18px;
  font-weight: 700;
}

.tabs-container {
  margin-bottom: 16px;
}

.flex {
  display: flex;
}

.items-center {
  align-items: center;
}

.mb-4 {
  margin-bottom: 16px;
}

.search-input {
  margin-right: 8px;
}

:deep(.el-table) {
  margin-bottom: 20px;
}

:deep(.el-loading-text) {
  color: #606266;
}

.detail-card {
  margin-bottom: 16px;
}
</style>