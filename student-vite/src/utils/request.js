// src/utils/request.js
import axios from 'axios';
import { ElMessage } from 'element-plus';
// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8088', 
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  },
  withCredentials: true
});

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  (response) => {
    // 只返回数据部分
    return response.data;
  },
  (error) => {
    // 统一错误提示
    ElMessage.error(error.response?.data?.msg || '请求失败');
    return Promise.reject(error);
  }
);

export default request;