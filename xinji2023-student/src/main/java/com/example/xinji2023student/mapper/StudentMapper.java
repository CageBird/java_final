package com.example.xinji2023student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xinji2023student.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    // 自定义查询方法（如果需要）
    @Select("SELECT * FROM student WHERE department_id = #{departmentId}")
    List<Student> findByDepartmentId(String departmentId);

    @Select("SELECT COUNT(*) FROM student WHERE gender = #{gender}")
    Long countByGender(String gender);
}