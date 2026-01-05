package com.example.xinji2023student.controller;

import com.example.xinji2023student.entity.Student;
import com.example.xinji2023student.mapper.StudentManyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/studentMany")
public class StudentManyController {
    @Autowired
    StudentManyMapper studentManyMapper;
    @GetMapping("/findAll")
    public List<Student> findAll(){
        List<Student> list=studentManyMapper.getStudentAndDepartment();
        return list;
    }
}
