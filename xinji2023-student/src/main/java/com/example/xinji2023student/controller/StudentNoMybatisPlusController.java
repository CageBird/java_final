package com.example.xinji2023student.controller;

import com.example.xinji2023student.entity.Student;
import com.example.xinji2023student.mapper.StudentNoMybatisPlusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studentNoMPlus")
public class StudentNoMybatisPlusController {
    @Autowired
    StudentNoMybatisPlusMapper studentNoMybatisPlusMapper;
    @GetMapping("/findAll")
    public List findAll() {
        List<Student> list=studentNoMybatisPlusMapper.getAll();
        System.out.println(list);
        return list;
    }

    @GetMapping("findById")
    public Student findById(String studentId) {
        Student student=studentNoMybatisPlusMapper.findById(studentId);
        return student;
    }

    @PostMapping("/add")
    public String insert(@RequestBody Student student) {
        int i = studentNoMybatisPlusMapper.add(student);
        if(i>0) {
            return "插入成功";
        }else {
            return "插入失败";
        }
    }

    @PutMapping("/update")
    public int updateStudent(@RequestBody Student student) {
        return studentNoMybatisPlusMapper.update(student);
    }

    @DeleteMapping("/delete/{studentId}")
    public int deleteStudent(@PathVariable String studentId) {
        return studentNoMybatisPlusMapper.delete(studentId);
    }
}
