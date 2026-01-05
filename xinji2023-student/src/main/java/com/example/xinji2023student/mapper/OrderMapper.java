package com.example.xinji2023student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xinji2023student.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT * FROM orders WHERE student_id=#{studentId}")
    List<Order> findByStudentId(String studentId);
}
