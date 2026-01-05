package com.example.xinji2023student.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("courses")
public class Course {
    // 核心：手动指定主键，关闭自增（courseId是手动输入的8位字符）
    @TableId(value = "course_id", type = com.baomidou.mybatisplus.annotation.IdType.INPUT)
    private String courseId; // 对应数据库 course_id (char(8))

    @TableField("course_name")
    private String courseName; // 对应 course_name (varchar(100))

    @TableField("credit")
    private Double credit; // 对应 credit (decimal(3,1))

    @TableField("hours")
    private Integer hours; // 对应 hours (int, NOT NULL)

    @TableField("department_id")
    private Integer departmentId; // 对应 department_id (int, 外键)

    @TableField("teacher_id")
    private Integer teacherId; // 对应 teacher_id (int, 外键)

    // 非数据库字段
    @TableField(exist = false)
    private String teacherName;
    @TableField(exist = false)
    private String departmentName;
}