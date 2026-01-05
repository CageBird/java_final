package com.example.xinji2023student.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;

@TableName("students")  // 修改表名为 students
public class Student {
    @TableId("student_id")
    private String studentId;

    @TableField("student_name")
    private String studentName;

    @TableField("gender")
    private String gender;

    @TableField("birth_date")
    private LocalDate birthDate;

    @TableField("enrollment_date")
    private LocalDate enrollmentDate;

    @TableField("department_id")
    private String departmentId;

    // 构造方法
    public Student() {}

    public Student(String studentId, String studentName, String gender, LocalDate birthDate, LocalDate enrollmentDate, String departmentId) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.enrollmentDate = enrollmentDate;
        this.departmentId = departmentId;
    }

    // Getter 和 Setter 方法
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate=" + birthDate +
                ", enrollmentDate=" + enrollmentDate +
                ", departmentId='" + departmentId + '\'' +
                '}';
    }
}