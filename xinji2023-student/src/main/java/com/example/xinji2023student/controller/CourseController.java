package com.example.xinji2023student.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xinji2023student.entity.Course;
import com.example.xinji2023student.entity.Student;
import com.example.xinji2023student.entity.StudentCourses;
import com.example.xinji2023student.mapper.CourseMapper;
import com.example.xinji2023student.mapper.StudentCoursesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")  // 增加这一行，为整个控制器添加前缀路径
public class CourseController {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private StudentCoursesMapper studentCoursesMapper;
    //--------------------------查询------------------------------------
    //全查询，并返回Json数据
    @GetMapping("/queryAll")
    public List<Course> queryAll(){
        List<Course> list=courseMapper.selectList(null);
        System.out.println(list);
        return list;
    }
    @GetMapping("/selectById/{courseId}")
    public Course selectById(@PathVariable("courseId") String courseId){
        return courseMapper.findById(courseId);
    }


    // CourseController.java 新增接口修复
    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@RequestBody Course course) {
        try {
            // 1. 基础校验
            if (course.getCourseId() == null || course.getCourseId().trim().length() != 5) {
                return ResponseEntity.status(400).body("课程编号必须为5位字符");
            }
            if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
                return ResponseEntity.status(400).body("课程名称不能为空");
            }
            if (course.getCredit() == null || course.getCredit() < 0.5 || course.getCredit() > 9.9) {
                return ResponseEntity.status(400).body("学分必须在0.5-9.9之间");
            }
            // 关键：校验hours非空（数据库NOT NULL）
            if (course.getHours() == null || course.getHours() < 0) {
                return ResponseEntity.status(400).body("课时不能为空且不能小于0");
            }
            if (course.getDepartmentId() == null) {
                return ResponseEntity.status(400).body("院系ID不能为空");
            }
            if (course.getTeacherId() == null) {
                return ResponseEntity.status(400).body("教师ID不能为空");
            }

            // 2. 校验课程ID是否已存在
            Course existing = courseMapper.selectOne(new QueryWrapper<Course>()
                    .eq("course_id", course.getCourseId().trim()));
            if (existing != null) {
                return ResponseEntity.status(409).body("课程ID已存在");
            }

            // 3. 校验外键是否存在（可选，推荐）
            // 需注入DepartmentMapper/TeacherMapper
            // if (departmentMapper.selectById(course.getDepartmentId()) == null) {
            //     return ResponseEntity.status(400).body("院系ID不存在");
            // }
            // if (teacherMapper.selectById(course.getTeacherId()) == null) {
            //     return ResponseEntity.status(400).body("教师ID不存在");
            // }

            // 4. 执行插入（trim courseId，避免空格）
            course.setCourseId(course.getCourseId().trim());
            int insertCount = courseMapper.insert(course);

            return ResponseEntity.ok(insertCount > 0 ? 1 : 0);
        } catch (Exception e) {
            e.printStackTrace(); // 打印完整堆栈，定位问题
            // 精准捕获异常类型
            if (e.getMessage().contains("foreign key constraint")) {
                return ResponseEntity.status(400).body("外键约束失败：院系ID/教师ID不存在");
            } else if (e.getMessage().contains("duplicate entry")) {
                return ResponseEntity.status(409).body("课程ID已存在");
            } else if (e.getMessage().contains("Field 'hours' doesn't have a default value")) {
                return ResponseEntity.status(400).body("课时不能为空");
            } else {
                return ResponseEntity.status(500).body("添加失败：" + e.getMessage());
            }
        }
    }

    // CourseController.java 补充
//    @PostMapping("/insert")
//    public ResponseEntity<?> addCourse(@RequestBody Course course) {
//        try {
//            // 验证课程ID是否已存在
//            Course existing = courseMapper.findById(course.getCourseId());
//            if (existing != null) {
//                return ResponseEntity.status(HttpStatus.CONFLICT)
//                        .body(Collections.singletonMap("message", "课程ID已存在"));
//            }
//            int result = courseMapper.insert(course);
//            return ResponseEntity.ok(result > 0 ? "添加成功" : "添加失败");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Collections.singletonMap("message", "添加失败：" + e.getMessage()));
//        }
//    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCourse(@RequestBody Course course) {
        try {
            // 新增：基础校验
            if (course.getCourseId() == null || course.getCourseId().trim().length() != 5) {
                return ResponseEntity.status(400).body("课程编号必须为5位字符");
            }
            if (course.getHours() == null || course.getHours() < 0) {
                return ResponseEntity.status(400).body("课时不能为空且不能小于0");
            }

            // 先检查课程是否存在
            Course existing = courseMapper.selectById(course.getCourseId().trim());
            if (existing == null) {
                return ResponseEntity.status(404).body("课程不存在");
            }

            // 执行更新（trim courseId）
            course.setCourseId(course.getCourseId().trim());
            int result = courseMapper.updateById(course);

            // 修复：返回数字1（匹配前端判断逻辑）
            return ResponseEntity.ok(result > 0 ? 1 : 0);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) {
        try {
            int result = courseMapper.deleteById(courseId);
            // 问题点：返回"删除成功"/"课程不存在"字符串，而非数字1/0
            return ResponseEntity.ok(result > 0 ? "删除成功" : "课程不存在");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "删除失败：" + e.getMessage()));
        }
    }


    // 批量删除接口修复
    @DeleteMapping("/delete/batch")
    public ResponseEntity<?> deleteBatchCourse(@RequestBody List<String> courseIds) {
        try {
            // 1. 校验参数非空
            if (courseIds == null || courseIds.isEmpty()) {
                return ResponseEntity.status(400).body("课程ID列表不能为空");
            }

            // 2. 过滤有效ID（JDK 8兼容写法）
            List<String> validCourseIds = courseIds.stream()
                    .filter(id -> id != null && id.trim().length() == 5) // 保留5位有效ID
                    .map(String::trim) // 去除空格
                    .distinct() // 去重
                    .collect(Collectors.toList()); // 替换toList()为collect

            if (validCourseIds.isEmpty()) {
                return ResponseEntity.status(400).body("无有效课程ID（必须为5位字符）");
            }

            // 3. 执行批量删除
            int deleteCount = courseMapper.deleteBatchIds(validCourseIds);

            // 4. 返回删除结果
            if (deleteCount == 0) {
                return ResponseEntity.status(404).body("课程不存在");
            } else if (deleteCount < validCourseIds.size()) {
                return ResponseEntity.ok("部分课程删除成功（共删除" + deleteCount + "条，" + (validCourseIds.size() - deleteCount) + "条不存在）");
            } else {
                return ResponseEntity.ok(deleteCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("批量删除失败：" + e.getMessage());
        }
    }


    // CourseController.java 补充
    @GetMapping("/findByPage/{page}/{size}")
    public ResponseEntity<?> findByPage(
            @PathVariable Integer page,
            @PathVariable Integer size) {
        try {
            Page<Course> pageParam = new Page<>(page, size);
            IPage<Course> iPage = courseMapper.selectPage(pageParam, null);

            Map<String, Object> result = new HashMap<>();
            result.put("records", iPage.getRecords());
            result.put("total", iPage.getTotal());
            result.put("size", iPage.getSize());
            result.put("current", iPage.getCurrent()); // 必须添加！
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "分页查询失败：" + e.getMessage()));
        }
    }
    @GetMapping("/findByPageAndQuery/{page}/{size}")
    public ResponseEntity<?> findByPageAndQuery(
            @PathVariable Integer page,
            @PathVariable Integer size,
            @RequestParam String courseName) {
        try {
            Page<Course> pageParam = new Page<>(page, size);
            QueryWrapper<Course> wrapper = new QueryWrapper<>();
            wrapper.like("course_name", courseName);

            IPage<Course> iPage = courseMapper.selectPage(pageParam, wrapper);

            Map<String, Object> result = new HashMap<>();
            result.put("records", iPage.getRecords());
            result.put("total", iPage.getTotal());
            result.put("size", iPage.getSize());
            result.put("current", iPage.getCurrent()); // 必须添加！
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "条件查询失败：" + e.getMessage()));
        }
    }

    @GetMapping("/findByDepartment/{deptId}")
    public List<Course> findByDepartment(@PathVariable Integer deptId) {
        // 需在CourseMapper中添加对应方法
        return courseMapper.selectList(new QueryWrapper<Course>().eq("department_id", deptId));
    }

    // CourseController.java 补充
    @GetMapping("/withStudents/{courseId}")
    public List<StudentCourses> getCourseWithStudents(@PathVariable String courseId) {
        // 需在StudentCoursesMapper中添加按课程ID查询的方法
        return studentCoursesMapper.selectList(
                new QueryWrapper<StudentCourses>().eq("course_id", courseId)
        );
    }
}
