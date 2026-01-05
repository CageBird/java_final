<template>
  <div class="course-list-container">
    <el-card shadow="never" class="course-card">
      <template #header>
        <div class="card-header">
          <span class="title">课程列表</span>
        </div>
      </template>

      <el-alert
        v-if="isLoading"
        title="正在加载数据..."
        type="info"
        :closable="false"
        show-icon
        class="mb-4 loading-alert"
      />

      <div class="operation-bar">
        <div class="left-actions">
          <el-button type="primary" size="small" @click="handleAdd">
            添加
          </el-button>
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
            placeholder="输入课程名称搜索"
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
          <el-table-column prop="courseId" label="课程ID" width="120" />
          <el-table-column prop="courseName" label="课程名称" min-width="150" />
          <el-table-column prop="credit" label="学分" width="80" />
          <el-table-column label="课时" width="80">
            <template #default="scope">
              {{ scope.row?.courseHour || "-" }}
            </template>
          </el-table-column>
          <!-- 授课教师：ID映射名称 -->
          <el-table-column label="授课教师" min-width="120">
            <template #default="scope">
              {{ scope.row?.teacherName || "-" }}
            </template>
          </el-table-column>
          <!-- 所属院系：ID映射名称 -->
          <el-table-column label="所属院系" min-width="120">
            <template #default="scope">
              {{ scope.row?.departmentName || "-" }}
            </template>
          </el-table-column>
          <!-- 操作列：恢复修改/删除 -->
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button
                link
                type="primary"
                size="small"
                @click="handleEdit(scope.row)"
              >
                修改
              </el-button>
              <el-button
                link
                type="danger"
                size="small"
                @click="handleDelete(scope.row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="pagination-container">
        <el-pagination
          background
          layout="prev, pager, next, sizes, total"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          :total="total"
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
          ref="courseFormRef"
          :model="courseForm"
          :rules="courseRules"
          label-width="85px"
          v-loading="formLoading"
        >
          <el-form-item label="课程ID" prop="courseId">
            <el-input
              v-model="courseForm.courseId"
              placeholder="请输入5位课程ID（数字/字母）"
              :disabled="isEdit"
              maxlength="5"
              show-word-limit
              @input="(v) => (courseForm.courseId = v.replace(/\s/g, ''))"
            />
          </el-form-item>
          <el-form-item label="课程名称" prop="courseName">
            <el-input
              v-model="courseForm.courseName"
              placeholder="请输入课程名称"
            />
          </el-form-item>
          <el-form-item label="学分" prop="credit">
            <el-input
              v-model.number="courseForm.credit"
              placeholder="请输入学分"
              type="number"
              min="0"
              step="0.5"
            />
          </el-form-item>
          <el-form-item label="课时" prop="courseHour">
            <el-input
              v-model.number="courseForm.courseHour"
              placeholder="请输入课时"
              type="number"
              min="0"
            />
          </el-form-item>
          <el-form-item label="院系ID" prop="departmentId">
            <el-select
              v-model="courseForm.departmentId"
              placeholder="请选择院系ID"
            >
              <el-option label="1" value="1" />
              <el-option label="2" value="2" />
              <el-option label="3" value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="教师ID" prop="teacherId">
            <el-select
              v-model="courseForm.teacherId"
              placeholder="请选择教师ID"
            >
              <el-option label="1" value="1" />
              <el-option label="2" value="2" />
              <el-option label="3" value="3" />
              <el-option label="4" value="4" />
              <el-option label="5" value="5" />
              <el-option label="6" value="6" />
              <el-option label="7" value="7" />
              <el-option label="8" value="8" />
              <el-option label="9" value="9" />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false" :disabled="formLoading">
              取消
            </el-button>
            <el-button
              type="primary"
              @click="handleSubmit"
              :loading="formLoading"
            >
              确定
            </el-button>
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
import {
  getCourseListByPage,
  getCourseListByPageAndQuery,
  addCourse,
  updateCourse,
  deleteCourse,
  deleteBatchCourse,
  getCourseById,
} from "../../api/course";

const departmentMap = {
  1: "信息与计算科学系",
  2: "统计学系",
  3: "数学与应用数学系",
};
const teacherMap = {
  1: "张明",
  2: "李华",
  3: "王强",
  4: "刘芳",
  5: "陈伟",
  6: "黄静",
  7: "周涛",
  8: "吴敏",
  9: "郑洁",
};

const tableDatabyPage = ref([]);
const pageSize = ref(5);
const total = ref(0);
const currentPage = ref(1);
const search = ref("");
const selectedRows = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref("");
const isEdit = ref(false);
const courseFormRef = ref();
const isLoading = ref(false);
const formLoading = ref(false);

// 表单数据
const courseForm = reactive({
  courseId: "",
  courseName: "",
  credit: 0.0,
  courseHour: "",
  departmentId: "",
  teacherId: "",
});

// 表单验证规则
const courseRules = reactive({
  courseId: [
    { required: true, message: "课程ID不能为空", trigger: "blur" },
    {
      len: 5,
      message: "课程ID必须为5位字符",
      trigger: ["blur", "change"],
    },
    {
      pattern: /^[0-9a-zA-Z]{5}$/,
      message: "课程ID只能是5位数字/字母",
      trigger: "blur",
    },
  ],
  courseName: [
    { required: true, message: "课程名称不能为空", trigger: "blur" },
  ],
  credit: [
    { required: true, message: "学分不能为空", trigger: "blur" },
    {
      type: "number",
      min: 0.5,
      max: 9.9,
      message: "学分范围0.5-9.9",
      trigger: "blur",
    },
  ],
  courseHour: [
    { required: true, message: "课时不能为空", trigger: "blur" },
    { type: "number", min: 0, message: "课时不能小于0", trigger: "blur" },
  ],
  departmentId: [
    { required: true, message: "请选择院系ID", trigger: ["change", "blur"] },
  ],
  teacherId: [
    { required: true, message: "请选择教师ID", trigger: ["change", "blur"] },
  ],
});

// 初始化加载数据
onMounted(() => {
  fetchData();
});

// 数据查询（含字段映射）
function fetchData() {
  isLoading.value = true;
  const requestFunc = search.value.trim()
    ? getCourseListByPageAndQuery(currentPage.value, pageSize.value, {
        courseName: search.value.trim(),
      })
    : getCourseListByPage(currentPage.value, pageSize.value);

  requestFunc
    .then((res) => {
      const data = res.data || {};
      tableDatabyPage.value = (data.records || []).map((item) => {
        const courseId = item.course_id || item.courseId;
        const courseName = item.course_name || item.courseName;
        const credit = item.credit;
        const courseHour = item.hours || item.courseHour || "-";
        const departmentId = item.department_id || item.departmentId;
        const teacherId = item.teacher_id || item.teacherId;

        return {
          courseId,
          courseName,
          credit,
          courseHour,
          departmentId,
          teacherId,
          departmentName:
            departmentMap[departmentId] || `院系ID:${departmentId}`,
          teacherName: teacherMap[teacherId] || `教师ID:${teacherId}`,
        };
      });
      pageSize.value = data.size || data.pageSize || 5;
      total.value = data.total || 0;
      currentPage.value = data.current || data.pageNum || 1;
    })
    .catch((error) => {
      console.error("数据查询失败:", error);
      tableDatabyPage.value = [];
      total.value = 0;
      ElMessage.error(
        "查询失败：" + (error.message || "接口请求异常，请稍后重试")
      );
    })
    .finally(() => {
      isLoading.value = false;
    });
}

// 搜索功能
function fetchDatabyPageAndName() {
  currentPage.value = 1;
  fetchData();
}

// 分页切换
function changePage(page) {
  currentPage.value = page;
  fetchData();
}

// 每页条数改变
function handleSizeChange(size) {
  pageSize.value = size;
  currentPage.value = 1;
  fetchData();
}

// 重置搜索
function resetSearchForm() {
  search.value = "";
  currentPage.value = 1;
  fetchData();
}

// 刷新数据
function refreshData() {
  fetchData();
  ElMessage.success("刷新成功");
}

// 复选框选中事件
function handleSelectionChange(selection) {
  selectedRows.value = selection; // 同步选中数据
}

// 添加课程
function handleAdd() {
  dialogTitle.value = "添加课程";
  isEdit.value = false;
  resetForm();
  dialogVisible.value = true;
}

// 编辑课程
async function handleEdit(row) {
  if (!row?.courseId) {
    ElMessage.warning("课程ID不存在，无法编辑");
    return;
  }
  dialogTitle.value = "编辑课程";
  isEdit.value = true;
  formLoading.value = true;

  try {
    const res = await getCourseById(row.courseId);
    const courseData = res.data || {};
    console.log("编辑回显数据：", courseData); // 打印回显数据

    // 适配后端返回的驼峰字段（关键！）
    Object.assign(courseForm, {
      courseId: courseData.courseId || "", // 驼峰courseId
      courseName: courseData.courseName || "",
      credit: courseData.credit || 0.0,
      courseHour: courseData.hours || "", // 后端返回hours → 前端courseHour
      departmentId: (courseData.departmentId || "").toString(),
      teacherId: (courseData.teacherId || "").toString(),
    });

    dialogVisible.value = true;
  } catch (error) {
    console.error("获取课程详情失败:", error);
    ElMessage.error("获取课程信息失败：" + (error.message || "接口请求异常"));
  } finally {
    formLoading.value = false;
  }
}

// 提交表单
async function handleSubmit() {
  try {
    const valid = await courseFormRef.value.validate();
    if (!valid) return;

    // 1. 课程ID清洗
    let courseId = courseForm.courseId
      .trim()
      .replace(/\s+/g, "")
      .replace(/　/g, "");
    courseId = String(courseId);
    if (courseId.length !== 5) {
      ElMessage.error("课程ID必须是5位字符！");
      return;
    }

    // 2. 构造参数（驼峰）
    const hours = Number(courseForm.courseHour) || 0;
    const formData = {
      courseId: courseId,
      courseName: courseForm.courseName.trim(),
      credit: Number(courseForm.credit),
      hours: hours,
      departmentId: Number(courseForm.departmentId),
      teacherId: Number(courseForm.teacherId),
    };

    console.log("最终提交参数（驼峰）：", formData);
    console.log("courseId长度：", formData.courseId.length);

    // 3. 调用接口
    const res = isEdit.value
      ? await updateCourse(formData)
      : await addCourse(formData);

    // 修复：判断后端返回值（1表示成功）
    if (res.data === 1 || res.code === 200) {
      ElMessage.success(isEdit.value ? "修改成功" : "添加成功");
      dialogVisible.value = false;
      // 强制刷新列表（关键！）
      fetchData();
    } else {
      ElMessage.error(
        isEdit.value
          ? "修改失败"
          : "添加失败：" + (res.data || res.msg || "操作失败")
      );
    }
  } catch (error) {
    console.error("提交错误：", error);
    ElMessage.error(`提交失败：${error.response?.data || error.message}`);
  } finally {
    formLoading.value = false;
  }
}
// 删除单条课程
// 单条删除
function handleDelete(row) {
  if (!row?.courseId || !row?.courseName) {
    ElMessage.warning("课程信息异常，无法删除");
    return;
  }
  ElMessageBox.confirm(`确定要删除课程 "${row.courseName}" 吗？`, "删除确认", {
    type: "warning",
  })
    .then(async () => {
      try {
        const res = await deleteCourse(row.courseId);
        console.log("删除接口返回值：", res.data); // 打印返回值，确认是"删除成功"

        // ========== 核心修复：兼容后端返回值 ==========
        // 成功条件：返回"删除成功"字符串 或 数字1 或 状态码200
        const isSuccess =
          res.data === "删除成功" ||
          res.data === 1 ||
          (res.status === 200 && res.data !== "课程不存在");

        if (isSuccess) {
          ElMessage.success("删除成功"); // 正确提示
          fetchData(); // 立即刷新列表，无需手动刷新
        } else {
          ElMessage.warning("删除失败：" + (res.data || "课程不存在"));
        }
      } catch (error) {
        console.error("删除失败：", error);
        ElMessage.error(
          "删除失败：" + (error.response?.data?.message || error.message)
        );
      }
    })
    .catch(() => {});
}

// 批量删除（依赖selectedRows）
function handleBatchDelete() {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请先选择要删除的课程");
    return;
  }

  const courseIds = selectedRows.value
    .map((row) => row.courseId)
    .filter(Boolean);
  const names = selectedRows.value
    .map((row) => row.courseName)
    .filter(Boolean)
    .join("、");

  if (courseIds.length === 0) {
    ElMessage.warning("选中的课程ID异常，无法删除");
    return;
  }

  ElMessageBox.confirm(
    `确定要删除选中的 ${courseIds.length} 个课程吗？\n${names}`,
    "批量删除确认",
    { type: "warning" }
  )
    .then(async () => {
      try {
        const res = await deleteBatchCourse(courseIds);
        if (res.data > 0 || res.code === 200) {
          ElMessage.success(`成功删除 ${res.data || courseIds.length} 个课程`);
          selectedRows.value = [];
          fetchData();
        } else {
          ElMessage.error("批量删除失败：" + (res.msg || "操作失败"));
        }
      } catch (error) {
        ElMessage.error("批量删除失败：" + (error.message || "接口请求异常"));
      }
    })
    .catch(() => {});
}

// 重置表单
function resetForm() {
  Object.assign(courseForm, {
    courseId: "",
    courseName: "",
    credit: 0.0,
    courseHour: "",
    departmentId: "",
    teacherId: "",
  });
  if (courseFormRef.value) {
    nextTick(() => courseFormRef.value.clearValidate());
  }
}

// 关闭对话框
function handleDialogClose() {
  resetForm();
}
</script>

<style scoped>
/* 样式保留所有布局，确保按钮/分页正常显示 */
.course-list-container {
  width: 100%;
  min-height: 100vh;
  padding: 20px;
  box-sizing: border-box;
  background-color: #f9fafb;
}

.course-card {
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

/* 操作栏样式：确保按钮横向排列 */
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

/* 表格容器：确保高度足够 */
.table-container {
  width: 100%;
  height: calc(100vh - 280px);
  min-height: 300px;
  overflow-y: auto;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  margin-bottom: 20px;
}

/* 分页容器：确保居中显示 */
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

/* 响应式适配 */
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

/* 表格样式优化 */
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