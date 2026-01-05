package com.example.xinji2023student.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xinji2023student.entity.Student;
import com.example.xinji2023student.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentService extends ServiceImpl<StudentMapper, Student> {

    // 分页查询
    public IPage<Student> findByPage(Integer page, Integer size) {
        Page<Student> pageParam = new Page<>(page, size);
        return page(pageParam);
    }

    // 分页条件查询
    public IPage<Student> findByPageAndQuery(Integer currentPage, Integer pageSize, String studentName) {
        Page<Student> pageInfo = new Page<>(currentPage, pageSize);
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        if (studentName != null && !studentName.trim().isEmpty()) {
            queryWrapper.like("student_name", studentName.trim());
        }

        return page(pageInfo, queryWrapper);
    }

    // 批量删除
    @Transactional
    public boolean deleteBatch(List<String> studentIds) {
        return removeByIds(studentIds);
    }

    // 更新学生信息
    @Transactional
    public boolean updateStudent(Student student) {
        return updateById(student);
    }

    // 添加学生
    @Transactional
    public boolean addStudent(Student student) {
        return save(student);
    }
}