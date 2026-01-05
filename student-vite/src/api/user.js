import request from '@/utils/request';
// 用户登录
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    }
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

// 用户退出登录
export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}