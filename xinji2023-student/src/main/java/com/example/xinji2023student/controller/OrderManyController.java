package com.example.xinji2023student.controller;

import com.example.xinji2023student.entity.Student;
import com.example.xinji2023student.mapper.StudentJoinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderMany")
public class OrderManyController {
    //订单查询
    @Autowired
    StudentJoinMapper studentJoinMapper;
    @GetMapping("queryStudentAndOrders")
    public List queryAllAndOrder() {
        List<Student> list = studentJoinMapper.getStudentAndOrders();
        return list;
    }
}
