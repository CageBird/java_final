package com.example.xinji2023student.mapper;

import com.example.xinji2023student.entity.Course;
import com.example.xinji2023student.entity.Department;
import com.example.xinji2023student.entity.Student;
import com.example.xinji2023student.entity.StudentCourses;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentCoursesManyMapper {
    public List<StudentCourses> getStudentCourseAndStudent();
    @Select("select * from student_courses")
    @Results({
            //column是表里的字段，property是类里面的属性
            @Result(column="id",property="id"),
            @Result(column="student_id",property="studentId"),
            @Result(column="course_id",property="courseId"),
            @Result(column="enrollment_date",property="enrollmentDate"),
            @Result(column="score",property="score"),
            @Result(column="status",property="status"),
            @Result(column="student_id",property="student",javaType= Student.class,
                    one=@One(select="com.example.xinji2023student.mapper.StudentNoMybatisPlusMapper.findById")),
            @Result(column="course_id",property="course",javaType= Course.class,
                    one=@One(select="com.example.xinji2023student.mapper.CourseMapper.findById"))
    })//根据student_id查找student
    public List<StudentCourses> getStudentCourseAndStudentAndCourses();
}
