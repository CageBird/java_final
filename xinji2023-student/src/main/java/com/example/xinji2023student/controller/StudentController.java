package com.example.xinji2023student.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.xinji2023student.entity.Student;
import com.example.xinji2023student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"}, allowCredentials = "true")

public class StudentController {

    @Autowired
    private StudentService studentService;

    //----------------------查询------------------------------
    @GetMapping("/queryAll")
    public ResponseEntity<?> queryAll() {
        try {
            List<Student> list = studentService.list();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "查询失败", e);
        }
    }

    @GetMapping("/selectById/{studentId}")
    public ResponseEntity<?> selectById(@PathVariable String studentId) {
        try {
            Student student = studentService.getById(studentId);
            if (student == null) {
                return errorResponse(HttpStatus.NOT_FOUND, "学生不存在", null);
            }
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "查询失败", e);
        }
    }

    @GetMapping("/findByPage/{page}/{size}")
    public ResponseEntity<?> findByPage(
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size) {
        try {
            IPage<Student> iPage = studentService.findByPage(page, size);

            Map<String, Object> result = new HashMap<>();
            result.put("records", iPage.getRecords());
            result.put("total", iPage.getTotal());
            result.put("size", iPage.getSize());
            result.put("current", iPage.getCurrent());
            result.put("pages", iPage.getPages());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "分页查询失败", e);
        }
    }

    @GetMapping("/findByPageAndQuery/{currentPage}/{pageSize}")
    public ResponseEntity<?> findByPageAndQuery(
            @PathVariable("currentPage") Integer currentPage,
            @PathVariable("pageSize") Integer pageSize,
            @RequestParam(required = false) String studentName) {
        try {
            IPage<Student> iPage = studentService.findByPageAndQuery(currentPage, pageSize, studentName);

            Map<String, Object> result = new HashMap<>();
            result.put("records", iPage.getRecords());
            result.put("total", iPage.getTotal());
            result.put("size", iPage.getSize());
            result.put("current", iPage.getCurrent());
            result.put("pages", iPage.getPages());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "条件查询失败", e);
        }
    }

    //---------------------插入-------------------------
    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody Student student) {
        try {
            // 验证学号是否已存在
            Student existing = studentService.getById(student.getStudentId());
            if (existing != null) {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("message", "学号已存在");
                errorMap.put("studentId", student.getStudentId());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMap);
            }

            // 验证必填字段
            if (student.getEnrollmentDate() == null) {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("message", "入学日期不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
            }

            boolean result = studentService.addStudent(student);
            return ResponseEntity.ok(result ? 1 : 0);
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "添加失败", e);
        }
    }

    //----------------------删除------------------------
    @DeleteMapping("/deleteById/{studentId}")
    public ResponseEntity<?> deleteById(@PathVariable("studentId") String studentId) {
        try {
            boolean result = studentService.removeById(studentId);
            return ResponseEntity.ok(result ? 1 : 0);
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "删除失败", e);
        }
    }

    // 批量删除接口
    @DeleteMapping("/deleteBatch")
    public ResponseEntity<?> deleteBatch(@RequestBody List<String> studentIds) {
        try {
            boolean result = studentService.deleteBatch(studentIds);
            return ResponseEntity.ok(result ? studentIds.size() : 0);
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "批量删除失败", e);
        }
    }

    //-------------------更新----------------------
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Student student) {
        try {
            // 验证必填字段
            if (student.getEnrollmentDate() == null) {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("message", "入学日期不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
            }

            boolean result = studentService.updateStudent(student);
            return ResponseEntity.ok(result ? 1 : 0);
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "更新失败", e);
        }
    }

    // 统计学生数量
    @GetMapping("/count")
    public ResponseEntity<?> count() {
        try {
            long count = studentService.count();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "统计失败", e);
        }
    }

    // 错误响应工具方法
    private ResponseEntity<?> errorResponse(HttpStatus status, String message, Exception e) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", message);
        if (e != null) {
            errorMap.put("error", e.getMessage());
        }
        return ResponseEntity.status(status).body(errorMap);
    }
}