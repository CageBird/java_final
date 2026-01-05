package com.example.xinji2023student.controller;

import com.example.xinji2023student.entity.Order;
import com.example.xinji2023student.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;
    @GetMapping("/findAll/{studentId}")
    public List<Order> findByStudentId(@PathVariable String studentId){
        List<Order> list = orderMapper.findByStudentId(studentId);
        return list;
    }

    // OrderController.java 补充
    @PostMapping("/add")
    public int addOrder(@RequestBody Order order) {
        return orderMapper.insert(order);
    }

    @PutMapping("/update")
    public int updateOrder(@RequestBody Order order) {
        return orderMapper.updateById(order);
    }

    @DeleteMapping("/delete/{orderId}")
    public int deleteOrder(@PathVariable Long orderId) {
        return orderMapper.deleteById(orderId);
    }

    @GetMapping("/findById/{orderId}")
    public Order findOrderById(@PathVariable Long orderId) {
        return orderMapper.selectById(orderId);
    }
}
