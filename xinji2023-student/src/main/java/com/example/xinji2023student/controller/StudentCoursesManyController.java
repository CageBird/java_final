package com.example.xinji2023student.controller;

import com.example.xinji2023student.entity.StudentCourses;
import com.example.xinji2023student.mapper.StudentCoursesManyMapper;
import com.example.xinji2023student.mapper.StudentCoursesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/studentCoursesMany")
public class StudentCoursesManyController {
    @Autowired
    StudentCoursesManyMapper studentCoursesManyMapper;
    @GetMapping("/findAll")
    public List<StudentCourses> findAll() {
        List<StudentCourses> list=studentCoursesManyMapper.getStudentCourseAndStudent();
        return list;
    }
    @GetMapping("/findAll3")
    public List<StudentCourses> findAll3() {
        List<StudentCourses> list=studentCoursesManyMapper.getStudentCourseAndStudentAndCourses();
        return list;
    }
}
