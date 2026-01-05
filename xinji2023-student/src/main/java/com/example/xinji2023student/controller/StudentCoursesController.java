package com.example.xinji2023student.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xinji2023student.entity.Student;
import com.example.xinji2023student.entity.StudentCourses;
import com.example.xinji2023student.mapper.StudentCoursesMapper;
import com.example.xinji2023student.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studentCourses")  // 增加这一行，为整个控制器添加前缀路径
public class StudentCoursesController {
    @Autowired
    private StudentCoursesMapper studentCoursesMapper;
    //--------------------------查询------------------------------------
    //全查询，并返回Json数据
    @GetMapping("/queryAll")
    public List<StudentCourses> queryAll(){
        List<StudentCourses> list=studentCoursesMapper.selectList(null);
        System.out.println(list);
        return list;
    }
    //通过Id查询(此代码可用于Vue中的数据更新中).
    // http://localhost:8088/student/selectById/202011010101
    @GetMapping("/selectById/{id}")
    public StudentCourses selectById(@PathVariable("id") String id){
        return studentCoursesMapper.selectById(id);
    }

    //    //--------------------------插入------------------------------------
    //    下面代码用来在vue中数据插入
    @PostMapping("/insert")
    public int insert(@RequestBody StudentCourses studentCourses){
        int result=studentCoursesMapper.insert(studentCourses);
        return result;
    }

    // StudentCoursesController.java 补充
    @PutMapping("/updateScore")
    public int updateScore(@RequestBody StudentCourses studentCourses) {
        // 仅更新成绩字段
        return studentCoursesMapper.update(
                studentCourses,
                new QueryWrapper<StudentCourses>()
                        .eq("student_id", studentCourses.getStudentId())
                        .eq("course_id", studentCourses.getCourseId())
        );
    }

    @GetMapping("/findByStudentId/{studentId}")
    public List<StudentCourses> findByStudentId(@PathVariable String studentId) {
        return studentCoursesMapper.selectList(
                new QueryWrapper<StudentCourses>().eq("student_id", studentId)
        );
    }
//    //--------------------------删除------------------------------------
//    // 根据id删除,并返回对应的行号，该代码在Vue中用来删除数据
//    @DeleteMapping("/deleteByid/{studentId}")
//    public int delete(@PathVariable("studentId") String studentId){
//        int row=studentMapper.deleteById(studentId);
//        return row;
//    }
//    @GetMapping("delete2")
//    public void delete2(String studentId){
//        int row = studentMapper.deleteById(studentId);
//        System.out.println(row);
//    }
//    //--------------------------更新------------------------------------
//    //在vue中更新，需要先定位id，利用之前的selectById,然后再做更新
//    @PutMapping("/update")
//    public int update(@RequestBody Student student){
//        int result= studentMapper.updateById(student);
//        return  result;
//      }
//    @PutMapping("/update2/{studentId}")
//    public int update2(@PathVariable String studentId,@RequestBody Student student){
//        student.setStudentId(studentId);//使用路径中的studentId
//        return studentMapper.updateById(student);
//    }
//    //--------------------------分页查询------------------------------------
//    @GetMapping("/findByPage/{page}/{size}")
//    public IPage findByPage(@PathVariable("page") Integer page,
//                            @PathVariable("size") Integer size) {
//        //设置起始值及每页数
//        Page<Student> pages = new Page<>(page, size);
//        IPage iPage = studentMapper.selectPage(pages, null);
//        System.out.println(iPage);
//        return iPage;
//    }
}
