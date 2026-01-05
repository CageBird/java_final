package com.example.xinji2023student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xinji2023student.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 院系Mapper接口
 * 继承MyBatis-Plus的BaseMapper，自带CRUD基础方法
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {


    @Select("SELECT * FROM departments WHERE department_id = #{departmentId}")
    Department findById(@Param("departmentId") Integer departmentId);

    @Select("SELECT " +
            "d.department_id as departmentId, " +  // 别名与前端接收字段一致
            "d.department_name as departmentName, " +
            "IFNULL(COUNT(s.student_id), 0) as studentCount " +  // 别名统一为驼峰
            "FROM departments d " +
            "LEFT JOIN students s ON d.department_id = s.department_id " +
            "GROUP BY d.department_id, d.department_name")
    List<Map<String, Object>> countStudentsByDepartment();
}