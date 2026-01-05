import axios from 'axios'

// 创建axios实例
const request = axios.create({
  baseURL: '/api', 
  timeout: 10000,
  withCredentials: true
});

// 获取分页课程列表
export function getCourseListByPage(page, size) {
  return request({
    url: `/course/findByPage/${page}/${size}`,
    method: 'get'
  });
}

// 分页条件查询
export function getCourseListByPageAndQuery(page, size, params) {
  return request({
    url: `/course/findByPageAndQuery/${page}/${size}`,
    method: 'get',
    params: params 
  });
}

// 获取所有课程
export function getAllCourses() {
  return request({
    url: '/course/queryAll',
    method: 'get'
  })
}

// 根据ID获取课程
export function getCourseById(courseId) {
  return request({
    url: `/course/selectById/${courseId}`,
    method: 'get'
  })
}

export function addCourse(data) {
  return request({
    url: '/course/add', 
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    }
  });
}

export function updateCourse(data) {
  return request({
    url: '/course/update',
    method: 'put',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    }
  });
}

// 删除课程
export function deleteCourse(courseId) {
  return request({
    url: `/course/delete/${courseId}`,
    method: 'delete'
  });
}

// 批量删除
export function deleteBatchCourse(courseIds) {
  return request({
    url: '/course/delete/batch',
    method: 'delete',
    data: courseIds
  });
}


// 获取课程数量
export function getCourseCount() {
  return request({
    url: '/course/count',
    method: 'get'
  })
}



// 获取学生列表（用于下拉选择）
export function getStudentList() {
  return request({
    url: '/student/queryAll',
    method: 'get'
  })
}

// 获取课程列表（用于下拉选择）
export function getCourseList() {
  return request({
    url: '/course/queryAll',
    method: 'get'
  })
}

export default {

getCourseListByPage,getCourseListByPageAndQuery,getAllCourses,
addCourse,updateCourse,deleteCourse,deleteBatchCourse,getCourseById,
getCourseCount,getStudentList,getCourseList
}