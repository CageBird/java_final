<template>
  <el-card shadow="never" class="border-blue-500 w-[700px] mx-auto mt-8">
    <template #header>
      <h2 class="text-xl font-bold">课程添加</h2>
    </template>

    <div class="p-4">
      <el-form
        ref="courseFormRef"
        :model="courseForm"
        :rules="courseRules"
        label-width="85px"
        v-loading="formLoading"
        class="max-w-[600px]"
      >
        <el-form-item label="课程ID" prop="courseId">
          <el-input
            v-model="courseForm.courseId"
            placeholder="请输入5位课程ID（数字/字母）"
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
            placeholder="请输入学分（0.5-9.9）"
            type="number"
            min="0.5"
            step="0.5"
          />
        </el-form-item>

        <el-form-item label="课时" prop="courseHour">
          <el-input
            v-model.number="courseForm.courseHour"
            placeholder="请输入课时（≥0）"
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

        <el-form-item class="flex justify-end gap-3 mt-6">
          <el-button 
            type="default" 
            @click="resetForm" 
            :disabled="formLoading"
          >
            重置
          </el-button>
          <el-button 
            type="primary" 
            @click="handleSubmit" 
            :loading="formLoading"
          >
            提交添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-card>
</template>

<script setup>
import { ref, reactive, nextTick } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";

import { addCourse } from "../../api/course";

const router = useRouter();
const courseFormRef = ref(null); // 表单Ref
const formLoading = ref(false); // 提交加载状态

// 表单数据（直接初始化，无弹窗）
const courseForm = reactive({
  courseId: "",        // 课程ID
  courseName: "",      // 课程名称
  credit: 0.0,         // 学分（默认0.5，避免初始校验失败）
  courseHour: "",       // 课时（默认0，避免初始校验失败）
  departmentId: "",    // 院系ID
  teacherId: ""        // 教师ID
});

//表单验证规则
const courseRules = reactive({
  courseId: [
    { required: true, message: "课程ID不能为空", trigger: "blur" },
    { len: 5, message: "课程ID必须为5位字符", trigger: ["blur", "change"] },
    { pattern: /^[0-9a-zA-Z]{5}$/, message: "课程ID只能是5位数字/字母", trigger: "blur" }
  ],
  courseName: [
    { required: true, message: "课程名称不能为空", trigger: "blur" }
  ],
  credit: [
    { required: true, message: "学分不能为空", trigger: "blur" },
    { type: "number", min: 0.5, max: 9.9, message: "学分范围0.5-9.9", trigger: "blur" }
  ],
  courseHour: [
    { required: true, message: "课时不能为空", trigger: "blur" },
    { type: "number", min: 0, message: "课时不能小于0", trigger: "blur" }
  ],
  departmentId: [
    { required: true, message: '请选择院系ID', trigger: ['change', 'blur'] }
  ],
  teacherId: [
    { required: true, message: '请选择教师ID', trigger: ['change', 'blur'] }
  ]
});

//提交表单
async function handleSubmit() {
  try {
    // 1. 表单校验
    const valid = await courseFormRef.value.validate();
    if (!valid) return;

    // 2. 清洗课程ID（去空格、强制字符串）
    let courseId = courseForm.courseId.trim().replace(/\s+/g, '').replace(/　/g, '');
    courseId = String(courseId);
    if (courseId.length !== 5) {
      ElMessage.error("课程ID必须是5位字符！");
      return;
    }

    // 3. 构造提交参数（驼峰格式，匹配后端）
    const formData = {
      courseId: courseId,
      courseName: courseForm.courseName.trim(),
      credit: Number(courseForm.credit),
      hours: Number(courseForm.courseHour),
      departmentId: Number(courseForm.departmentId),
      teacherId: Number(courseForm.teacherId)
    };

    console.log("提交参数：", formData);
    formLoading.value = true;

    // 4. 调用添加接口
    const res = await addCourse(formData);

    // 5. 校验返回结果
    const isSuccess = res.data === 1 || res.data === "添加成功" || res.status === 200;
    if (isSuccess) {
      ElMessage.success("课程添加成功！");
      resetForm(); // 添加成功后重置表单
    } else {
      ElMessage.error("添加失败：" + (res.data || "操作失败"));
    }
  } catch (error) {
    console.error("提交失败：", error);
    ElMessage.error(`添加失败：${error.response?.data || error.message || "服务器内部错误"}`);
  } finally {
    formLoading.value = false;
  }
}

//重置表单
function resetForm() {
  // 重置表单数据
  Object.assign(courseForm, {
    courseId: "",
    courseName: "",
    credit: 0.0,
    courseHour: "",
    departmentId: "",
    teacherId: ""
  });
  // 清空校验状态
  if (courseFormRef.value) {
    nextTick(() => courseFormRef.value.clearValidate());
  }
}
</script>

<style scoped>
/* 基础样式优化 */
.el-card {
  border-radius: 8px;
  border: 1px solid #e1e5e9;
}

/* 必选字段星号 */
:deep(.el-form-item.is-required .el-form-item__label::before) {
  content: "*";
  color: #f56c6c;
  margin-right: 4px;
}

/* 按钮组样式 */
.flex {
  display: flex;
}
.justify-end {
  justify-content: flex-end;
}
.gap-3 {
  gap: 12px;
}
.mt-6 {
  margin-top: 24px;
}
.mx-auto {
  margin-left: auto;
  margin-right: auto;
}
.mt-8 {
  margin-top: 32px;
}
.max-w-\[600px\] {
  max-width: 600px;
}
.text-xl {
  font-size: 18px;
}
.font-bold {
  font-weight: 700;
}
.p-4 {
  padding: 16px;
}
</style>