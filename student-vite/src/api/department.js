import request from '@/utils/request';

// 获取所有院系
export function getDepartmentList() {
  return request({
    url: '/department/queryAll',
    method: 'get'
  });
}

// 按ID查询院系
export function getDepartmentById(departmentId) {
  return request({
    url: `/department/findById/${departmentId}`,
    method: 'get',
    params:params
  });
}

// 统计各院系学生数量
export function countStudentsByDepartment() {
  return request({
    url: '/department/countStudents',
    method: 'get'
  });
}