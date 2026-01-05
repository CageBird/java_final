package com.example.xinji2023student.controller;

import com.example.xinji2023student.entity.Department;
import com.example.xinji2023student.mapper.DepartmentMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentMapper departmentMapper;
    @GetMapping("/queryAll")
    public List queryAll() {
        List<Department> list = departmentMapper.selectList(null);
        return list;
    }

    @GetMapping("/findById/{departmentId}")
    public Department findById(@PathVariable("departmentId") Integer departmentId) {
        return departmentMapper.findById(departmentId);
    }

    @GetMapping("/countStudents")
    public List<Map<String, Object>> countStudentsInDepartments() {
        return departmentMapper.countStudentsByDepartment();
    }
}
