<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2 class="login-title">系统登录</h2>
      </template>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        label-width="70px"
        v-loading="loginLoading"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            autocomplete="off"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            type="password"
            show-password
          />
        </el-form-item>

        <el-form-item class="login-actions">
          <el-button
            type="primary"
            class="w-full"
            @click="handleLogin"
            :loading="loginLoading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import { login } from "../api/user";
import { setToken } from "../utils/auth"; 

const router = useRouter();
const loginFormRef = ref(null);
const loginLoading = ref(false);

const loginForm = reactive({
  username: "",
  password: "",
});

const loginRules = reactive({
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, message: "密码长度不能少于6位", trigger: "blur" },
  ],
});

async function handleLogin() {
  try {
    const valid = await loginFormRef.value.validate();
    if (!valid) return;

    loginLoading.value = true;

    //模拟登录
    setTimeout(() => {
      if (loginForm.username === "admin" && loginForm.password === "123456") {
        // 存储token
        setToken("fake-token-123456");
        ElMessage.success("登录成功");
        router.push("/"); // 跳转到首页
      } else {
        ElMessage.error("账号或密码错误（模拟）");
      }
      loginLoading.value = false; 
    }, 500);

  } catch (error) {
    console.error("登录失败:", error);
    ElMessage.error("登录失败，请稍后重试");
    loginLoading.value = false;
    if (loginFormRef.value) {
      loginFormRef.value.clearValidate();
    }
  }
}
</script>


<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  font-size: 20px;
  font-weight: 700;
}

.login-actions {
  margin-top: 20px;
}
</style>