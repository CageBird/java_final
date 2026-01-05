package com.example.xinji2023student.mapper;

import com.example.xinji2023student.entity.Department;
import com.example.xinji2023student.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentManyMapper {
    @Select("select * from students")
    @Results({
            //column是表里的字段，property是类里面的属性
            @Result(column="student_id",property="studentId"),
            @Result(column="student_name",property="studentName"),
            @Result(column="gender",property="gender"),
            @Result(column="birth_date",property="birthDate"),
            @Result(column="department_id",property="departmentId"),
            @Result(column="enrollment_date",property="enrollmentDate"),
            @Result(column="phone",property="phone"),
            @Result(column="email",property="email"),
            @Result(column="address",property="address"),
            @Result(column="status",property="status"),
            @Result(column="department_id",property="department",javaType= Department.class,
                    one=@One(select="com.example.xinji2023student.mappper.DepartmentMapper.findById"))
    })//根据department_id查找department,因此在部门表对应的Department.findByid
    public List<Student> getStudentAndDepartment();

}
