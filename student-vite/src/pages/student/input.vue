<template>
  <el-card shadow="never" class="student-input-card">
    <template #header>
      <h2>新生录入</h2>
    </template>

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

      <el-form-item>
        <el-button type="primary" @click="handleSubmit">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
        <el-button @click="$router.push('/student/list')">返回列表</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref, reactive, nextTick } from "vue";
import { ElMessage } from 'element-plus';
import dayjs from 'dayjs';
import { addStudent } from "../../api/student";



// 关键：初始化表单引用，避免为 null
const studentFormRef = ref(null);

// 表单状态
const formLoading = ref(false);

// 表单数据
const studentForm = reactive({
  studentId: "",
  studentName: "",
  gender: "1", // 默认男
  birthDate: "",
  enrollmentDate: dayjs().format('YYYY-MM-DD'), // 默认当前日期
  departmentId: ""
});

// 表单验证规则
const studentRules = reactive({
  studentId: [
    { required: true, message: '学号不能为空', trigger: 'blur' },
    { min: 1, max: 20, message: '学号长度在 1 到 20 个字符', trigger: 'blur' }
  ],
  studentName: [
    { required: true, message: '姓名不能为空', trigger: 'blur' },
    { min: 1, max: 50, message: '姓名长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  enrollmentDate: [
    { required: true, message: '请选择入学日期', trigger: 'change' }
  ],
  departmentId: [
    { required: true, message: '系部不能为空', trigger: 'blur' },
    { min: 1, max: 50, message: '系部长度在 1 到 50 个字符', trigger: 'blur' }
  ]
});

// 提交表单
const handleSubmit = async () => {
  try {
    if (!studentFormRef.value) {
      ElMessage.error("表单初始化失败，请刷新页面");
      return;
    }

    // 验证表单
    const valid = await studentFormRef.value.validate();
    if (!valid) return;

    formLoading.value = true;

    // 准备提交数据
    const formData = {
      ...studentForm,
      studentId: studentForm.studentId.trim(),
      studentName: studentForm.studentName.trim(),
      departmentId: studentForm.departmentId.trim()
    };

    // 调用添加接口
    const res = await addStudent(formData);
    
    if (res.data && res.data > 0) {
      ElMessage.success("添加成功");
      resetForm(); // 重置表单
    } else {
      ElMessage.error(res.data?.message || "添加失败，可能学号已存在");
    }
  } catch (error) {
    console.error("添加失败:", error);
    if (error.name === 'ValidationError') {
      return;
    }
    ElMessage.error(`添加失败: ${error.message || '网络错误'}`);
  } finally {
    formLoading.value = false;
  }
};

// 重置表单
const resetForm = () => {
  if (studentFormRef.value) {
    nextTick(() => {
      studentFormRef.value.resetFields();
    });
  }
  // 手动重置日期默认值
  studentForm.enrollmentDate = dayjs().format('YYYY-MM-DD');
  studentForm.gender = "1";
};
</script>

<style scoped>
.student-input-card {
  margin: 20px auto;
  padding: 20px;
  max-width: 800px;
}

/* 替换 ::v-deep 为 :deep() */
:deep(.el-form-item.is-required .el-form-item__label::before) {
  content: '*';
  color: #f56c6c;
  margin-right: 4px;
}
</style>