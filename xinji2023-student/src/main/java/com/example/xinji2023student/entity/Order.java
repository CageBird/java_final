package com.example.xinji2023student.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("orders")
public class Order {
    @TableId(value = "order_id",type= IdType.AUTO)
    private Long orderId;

    private String studentId;
    private Data orderData;
    private BigDecimal amount;
    private String status;
    private String description;

    //多对一关系，多个订单属于一个学生
    @TableField(exist = false)
    private Student student;
}
