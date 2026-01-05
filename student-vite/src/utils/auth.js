// 存储token的键名
const TOKEN_KEY = 'course_system_token'

// 设置token（改为sessionStorage，仅当前会话有效）
export function setToken(token) {
  if (token) {
    sessionStorage.setItem(TOKEN_KEY, token) 
  }
}

// 获取token
export function getToken() {
  return sessionStorage.getItem(TOKEN_KEY) 
}

// 移除token
export function removeToken() {
  sessionStorage.removeItem(TOKEN_KEY) 
}

// 判断是否已登录
export function isLogin() {
  return !!getToken()
}