package com.example.xinji2023student.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("departments")
public class Department {
    @TableId
    private Integer departmentId;
    private String departmentName;
    private String officeLocation;
    private String phone;
    private String headName;
    private LocalDateTime createTime; // 新增字段（对应ALTER TABLE添加的create_time）
}
