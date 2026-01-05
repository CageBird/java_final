package com.example.xinji2023student.controller;


import org.springframework.web.bind.annotation.*;

@RestController//这是一个注解，说明下面的类是一个控制类
@RequestMapping("/api")//第一层路径
public class HelloController {
    @GetMapping("/hello") //访问的路径
    public String hello() {
        System.out.println("--------------gpnu--------------"); //控制台输出
        return "Hello SpringBoot!";
    }
    //路径变量
    @GetMapping("/helloPathVariable/{name}")
    public String helloPV(@PathVariable String name) {
        System.out.println("接受到一个名字："+name);
        return "Hello SpringBoot!"+name;
    }

    @GetMapping("/helloRequest")
    public String helloRP(@RequestParam String name,@RequestParam String height) {
        System.out.println("接受到一个名字：" + name+"身高"+height);
        return "Hello SpringBoot!" + name+"身高"+height;
    }
}
