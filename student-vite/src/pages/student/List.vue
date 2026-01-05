<template>
  <!-- 外层容器：匹配课程列表的布局结构 -->
  <div class="student-list-container">
    <!-- 核心修改：移除固定高度，改用课程列表的卡片样式 -->
    <el-card shadow="never" class="student-card">
      <template #header>
        <div class="card-header">
          <span class="title">学生列表</span>
        </div>
      </template>

      <!-- 加载状态提示：匹配课程列表样式 -->
      <el-alert
        v-if="isLoading"
        title="正在加载数据..."
        type="info"
        :closable="false"
        show-icon
        class="mb-4 loading-alert"
      />

      <!-- 操作栏：重构为课程列表的左右布局，保留原有功能 -->
      <div class="operation-bar">
        <div class="left-actions">
          <el-button type="primary" size="small" @click="handleAdd"
            >添加</el-button
          >
          <el-button
            type="danger"
            size="small"
            @click="handleBatchDelete"
            :disabled="selectedRows.length === 0"
            class="ml-2"
          >
            批量删除
          </el-button>
        </div>

        <div class="right-actions">
          <el-input
            v-model="search"
            size="small"
            placeholder="输入姓名搜索"
            class="search-input"
            @keyup.enter="fetchDatabyPageAndName"
          />
          <el-button
            type="primary"
            size="small"
            @click="fetchDatabyPageAndName"
            class="ml-2"
          >
            搜索
          </el-button>
          <el-button size="small" @click="resetSearchForm" class="ml-2">
            重置
          </el-button>
          <el-button
            type="primary"
            size="small"
            @click="refreshData"
            class="ml-2"
          >
            <el-icon :size="16">
              <Refresh />
            </el-icon>
            刷新
          </el-button>
        </div>
      </div>

      <!-- 表格容器：匹配课程列表的滚动容器样式 -->
      <div class="table-container">
        <el-table
          :data="tableDatabyPage"
          stripe
          border
          style="width: 100%"
          @selection-change="handleSelectionChange"
          v-loading="isLoading"
          element-loading-text="加载中..."
          :header-cell-style="{ background: '#f5f7fa' }"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column label="序号" width="60">
            <template #default="scope">
              {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="studentId" label="学号" width="120" />
          <el-table-column prop="studentName" label="姓名" width="100" />
          <el-table-column prop="gender" label="性别" width="80">
            <template #default="scope">
              {{ formatGender(scope.row.gender) }}
            </template>
          </el-table-column>
          <el-table-column prop="birthDate" label="出生日期" width="110" />
          <el-table-column prop="enrollmentDate" label="入学日期" width="110" />
          <!-- 系部名称映射 -->
          <el-table-column label="系部" min-width="150">
            <template #default="scope">
              {{ departmentMap[scope.row.departmentId] || "未知系部" }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button
                link
                type="primary"
                size="small"
                @click="handleEdit(scope.row)"
                >修改</el-button
              >
              <el-button
                link
                type="danger"
                size="small"
                @click="handleDelete(scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页容器：匹配课程列表样式 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="prev, pager, next, sizes, total"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          :total="total"
          v-if="isShow"
          @current-change="changePage"
          @size-change="handleSizeChange"
          :current-page="currentPage"
        />
      </div>

      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="520px"
        @close="handleDialogClose"
        destroy-on-close
      >
        <el-form
          ref="studentFormRef"
          :model="studentForm"
          :rules="studentRules"
          label-width="85px"
          v-loading="formLoading"
        >
          <el-form-item label="学号" prop="studentId">
            <el-input
              v-model="studentForm.studentId"
              placeholder="请输入学号"
              :disabled="isEdit"
            />
          </el-form-item>
          <el-form-item label="姓名" prop="studentName">
            <el-input
              v-model="studentForm.studentName"
              placeholder="请输入姓名"
            />
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="studentForm.gender">
              <el-radio label="1">男</el-radio>
              <el-radio label="2">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="出生日期" prop="birthDate">
            <el-date-picker
              v-model="studentForm.birthDate"
              type="date"
              placeholder="选择出生日期（可选）"
              value-format="YYYY-MM-DD"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="入学日期" prop="enrollmentDate" required>
            <el-date-picker
              v-model="studentForm.enrollmentDate"
              type="date"
              placeholder="选择入学日期（必填）"
              value-format="YYYY-MM-DD"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="院系ID" prop="departmentId">
            <el-select
              v-model="studentForm.departmentId"
              placeholder="请选择院系ID"
              style="width: 100%"
              :disabled="formLoading"
            >
              <el-option label="1" value="1" />
              <el-option label="2" value="2" />
              <el-option label="3" value="3" />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false" :disabled="formLoading"
              >取消</el-button
            >
            <el-button
              type="primary"
              @click="handleSubmit"
              :loading="formLoading"
              >确定</el-button
            >
          </span>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Refresh } from "@element-plus/icons-vue";
import dayjs from "dayjs";
import {
  getStudentListByPage,
  getStudentListByPageAndQuery,
  addStudent,
  updateStudent,
  deleteStudent,
  deleteBatchStudent,
  getStudentById,
} from "../../api/student";

// 系部ID-名称映射（和课程列表一致）
const departmentMap = {
  1: "信息与计算科学系",
  2: "统计学系",
  3: "数学与应用数学系",
};

// 原有响应式数据
const tableDatabyPage = ref([]);
const pageSize = ref(5);
const total = ref(0);
const isShow = ref(true);
const currentPage = ref(1);
const search = ref("");
const selectedRows = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref("");
const isEdit = ref(false);
const studentFormRef = ref();
const isLoading = ref(false);
const formLoading = ref(false);

// 表单数据
const studentForm = reactive({
  studentId: "",
  studentName: "",
  gender: "1",
  birthDate: "",
  enrollmentDate: dayjs().format("YYYY-MM-DD"),
  departmentId: "",
});

// 表单验证规则
const studentRules = reactive({
  studentId: [
    { required: true, message: "学号不能为空", trigger: "blur" },
    { min: 1, max: 20, message: "学号长度在 1 到 20 个字符", trigger: "blur" },
  ],
  studentName: [
    { required: true, message: "姓名不能为空", trigger: "blur" },
    { min: 1, max: 50, message: "姓名长度在 1 到 50 个字符", trigger: "blur" },
  ],
  gender: [{ required: true, message: "请选择性别", trigger: "change" }],
  enrollmentDate: [
    { required: true, message: "请选择入学日期", trigger: "change" },
  ],
  departmentId: [
    { required: true, message: "请选择院系ID", trigger: "change" },
    {
      min: 1,
      max: 50,
      message: "院系ID长度在 1 到 50 个字符",
      trigger: "change",
    },
  ],
});

// 格式化性别显示
const formatGender = (gender) => {
  if (gender === "1") return "男";
  if (gender === "2") return "女";
  return gender || "未知";
};

// 生命周期钩子
onMounted(() => {
  fetchData();
});

// 数据查询
function fetchData() {
  isLoading.value = true;

  if (search.value.trim()) {
    fetchDatabyPageAndName();
  } else {
    getStudentListByPage(currentPage.value, pageSize.value)
      .then((res) => {
        tableDatabyPage.value = res.data.records || [];
        pageSize.value = res.data.size || 5;
        total.value = res.data.total || 0;
        currentPage.value = res.data.current || 1;
      })
      .catch((error) => {
        console.error("获取数据失败:", error);
        tableDatabyPage.value = [];
        ElMessage.error("获取数据失败");
      })
      .finally(() => {
        isLoading.value = false;
      });
  }
}

function fetchDatabyPageAndName() {
  isLoading.value = true;
  currentPage.value = 1;

  getStudentListByPageAndQuery(currentPage.value, pageSize.value, {
    studentName: search.value.trim(),
  })
    .then((res) => {
      tableDatabyPage.value = res.data.records || [];
      pageSize.value = res.data.size || 5;
      total.value = res.data.total || 0;
      currentPage.value = res.data.current || 1;
    })
    .catch((error) => {
      console.error("条件查询失败:", error);
      tableDatabyPage.value = [];
      ElMessage.error("查询失败");
    })
    .finally(() => {
      isLoading.value = false;
    });
}

function changePage(page) {
  currentPage.value = page;
  fetchData();
}

function handleSizeChange(size) {
  pageSize.value = size;
  currentPage.value = 1;
  fetchData();
}

function resetSearchForm() {
  search.value = "";
  currentPage.value = 1;
  fetchData();
}

function refreshData() {
  fetchData();
  ElMessage.success("刷新成功");
}

function handleSelectionChange(selection) {
  selectedRows.value = selection;
}

function handleAdd() {
  dialogTitle.value = "添加学生";
  isEdit.value = false;
  resetForm();
  dialogVisible.value = true;
}

async function handleEdit(row) {
  dialogTitle.value = "编辑学生";
  isEdit.value = true;
  formLoading.value = true;

  try {
    const res = await getStudentById(row.studentId);
    const studentData = res.data || {};
    Object.assign(studentForm, {
      studentId: studentData.studentId || "",
      studentName: studentData.studentName || "",
      gender: studentData.gender ? String(studentData.gender) : "1",
      birthDate: studentData.birthDate || "",
      enrollmentDate:
        studentData.enrollmentDate || dayjs().format("YYYY-MM-DD"),
      departmentId: studentData.departmentId || "",
    });
    dialogVisible.value = true;
  } catch (error) {
    console.error("获取学生详情失败:", error);
    ElMessage.error("获取学生信息失败");
  } finally {
    formLoading.value = false;
  }
}

async function handleSubmit() {
  try {
    const valid = await studentFormRef.value.validate();
    if (!valid) return;

    formLoading.value = true;

    const formData = {
      studentId: studentForm.studentId.trim(),
      studentName: studentForm.studentName.trim(),
      gender: studentForm.gender,
      birthDate: studentForm.birthDate,
      enrollmentDate: studentForm.enrollmentDate,
      departmentId: studentForm.departmentId.trim(),
    };

    if (isEdit.value) {
      const res = await updateStudent(formData);
      if (res.data && res.data > 0) {
        ElMessage.success("修改成功");
        dialogVisible.value = false;
        fetchData();
      } else {
        if (res.data && typeof res.data === "object") {
          ElMessage.error(res.data.message || "修改失败");
        } else {
          ElMessage.error("修改失败，可能数据未发生变化");
        }
      }
    } else {
      const res = await addStudent(formData);
      if (res.data && res.data > 0) {
        ElMessage.success("添加成功");
        dialogVisible.value = false;
        fetchData();
      } else {
        if (res.data && typeof res.data === "object") {
          ElMessage.error(res.data.message || "添加失败");
        } else {
          ElMessage.error("添加失败，可能学号已存在");
        }
      }
    }
  } catch (error) {
    console.error("表单提交失败:", error);
    if (error.response) {
      const status = error.response.status;
      const errorData = error.response.data;
      if (status === 400) {
        ElMessage.error(errorData.message || "数据格式错误，请检查输入");
      } else if (status === 409) {
        ElMessage.error(errorData.message || "学号已存在，请使用其他学号");
      } else if (status === 404) {
        ElMessage.error(errorData.message || "学生不存在");
      } else {
        ElMessage.error(errorData.message || `操作失败 (${status})`);
      }
    } else if (error.request) {
      ElMessage.error("服务器未响应，请检查后端服务");
    } else {
      ElMessage.error("操作失败: " + error.message);
    }
  } finally {
    formLoading.value = false;
  }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除学生 "${row.studentName}" 吗？`, "删除确认", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      try {
        const res = await deleteStudent(row.studentId);
        if (res.data && res.data > 0) {
          ElMessage.success("删除成功");
          fetchData();
        } else {
          ElMessage.error("删除失败");
        }
      } catch (error) {
        console.error("删除失败:", error);
        ElMessage.error(
          "删除失败: " + (error.response?.data?.message || error.message)
        );
      }
    })
    .catch(() => {});
}

function handleBatchDelete() {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请先选择要删除的学生");
    return;
  }

  const studentIds = selectedRows.value.map((row) => row.studentId);
  const names = selectedRows.value.map((row) => row.studentName).join("、");

  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 个学生吗？\n${names}`,
    "批量删除确认",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(async () => {
      try {
        const res = await deleteBatchStudent(studentIds);
        if (res.data && res.data > 0) {
          ElMessage.success(`成功删除 ${res.data} 个学生`);
          selectedRows.value = [];
          fetchData();
        } else {
          ElMessage.error("删除失败");
        }
      } catch (error) {
        console.error("批量删除失败:", error);
        ElMessage.error("批量删除失败");
      }
    })
    .catch(() => {});
}

function resetForm() {
  Object.assign(studentForm, {
    studentId: "",
    studentName: "",
    gender: "1",
    birthDate: "",
    enrollmentDate: dayjs().format("YYYY-MM-DD"),
    departmentId: "",
  });
  if (studentFormRef.value) {
    nextTick(() => {
      studentFormRef.value.clearValidate();
    });
  }
}

function handleDialogClose() {
  resetForm();
}
</script>

<style scoped>
/* 完全复用课程列表的样式规范 */
.student-list-container {
  width: 100%;
  min-height: 100vh;
  padding: 20px;
  box-sizing: border-box;
  background-color: #f9fafb;
}

.student-card {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.loading-alert {
  margin-bottom: 16px !important;
}

/* 操作栏样式：和课程列表完全一致 */
.operation-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  flex-wrap: wrap;
  gap: 10px;
}

.left-actions {
  display: flex;
  align-items: center;
}

.right-actions {
  display: flex;
  align-items: center;
}

.search-input {
  width: 240px;
}

/* 表格容器：匹配课程列表的滚动容器 */
.table-container {
  width: 100%;
  height: calc(100vh - 280px);
  min-height: 300px;
  overflow-y: auto;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  margin-bottom: 20px;
}

/* 分页容器：居中显示 */
.pagination-container {
  display: flex;
  justify-content: center;
  padding: 10px 0;
}

/* 间距样式 */
.ml-2 {
  margin-left: 8px;
}

.mb-4 {
  margin-bottom: 16px;
}

/* 响应式适配：和课程列表一致 */
@media (max-width: 768px) {
  .operation-bar {
    flex-direction: column;
    align-items: flex-start;
  }

  .right-actions {
    width: 100%;
    flex-wrap: wrap;
  }

  .search-input {
    width: 100%;
    margin-bottom: 8px;
  }

  .table-container {
    height: calc(100vh - 350px);
  }
}

/* 表格样式优化：和课程列表一致 */
:deep(.el-table) {
  --el-table-header-text-color: #333;
  --el-table-row-hover-bg-color: #f8f9fa;
}

:deep(.el-table__header th) {
  font-weight: 600;
}

:deep(.el-table__fixed-right) {
  height: 100% !important;
}

:deep(.el-loading-mask) {
  z-index: 100;
}

/* 对话框按钮样式 */
:deep(.dialog-footer) {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>