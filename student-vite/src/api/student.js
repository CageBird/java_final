import axios from 'axios'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',  // 使用代理
  timeout: 10000
})

// 获取分页学生列表
export function getStudentListByPage(page, size) {
  return request({
    url: `/student/findByPage/${page}/${size}`,
    method: 'get'
  })
}

// 分页条件查询
export function getStudentListByPageAndQuery(currentPage, pageSize, params) {
  return request({
    url: `/student/findByPageAndQuery/${currentPage}/${pageSize}`,
    method: 'get',
    params: params
  })
}

// 获取所有学生
export function getAllStudents() {
  return request({
    url: '/student/queryAll',
    method: 'get'
  })
}

// 根据ID获取学生
export function getStudentById(studentId) {
  return request({
    url: `/student/selectById/${studentId}`,
    method: 'get'
  })
}

// 添加学生
export function addStudent(data) {
  return request({
    url: '/student/insert',
    method: 'post',
    data: data
  })
}

// 更新学生
export function updateStudent(data) {
  return request({
    url: '/student/update',
    method: 'put',
    data: data
  })
}

// 删除学生
export function deleteStudent(studentId) {
  return request({
    url: `/student/deleteById/${studentId}`,
    method: 'delete'
  })
}

// 批量删除
export function deleteBatchStudent(studentIds) {
  return request({
    url: '/student/deleteBatch',
    method: 'delete',
    data: studentIds
  })
}

// 获取学生数量
export function getStudentCount() {
  return request({
    url: '/student/count',
    method: 'get'
  })
}