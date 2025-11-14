package com.example.stusyshomework.controller;

import com.example.stusyshomework.entity.Student;
import com.example.stusyshomework.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/student/list")
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "student/list";
    }

    @PostMapping("/student/save")
    public String saveStudent(HttpServletRequest request) {
        try {
            System.out.println("=== 开始处理学生保存请求 ===");

            // 获取所有参数
            String idStr = request.getParameter("id");
            String studentNo = request.getParameter("studentNo");
            String name = request.getParameter("name");
            String gender = request.getParameter("gender");
            String ageStr = request.getParameter("age");
            String birthDateStr = request.getParameter("birthDate");
            String phone = request.getParameter("phone");
            String className = request.getParameter("className");
            String department = request.getParameter("department");
            String status = request.getParameter("status");

            // 打印参数用于调试
            System.out.println("id: " + idStr);
            System.out.println("studentNo: " + studentNo);
            System.out.println("name: " + name);
            System.out.println("gender: " + gender);
            System.out.println("age: " + ageStr);
            System.out.println("birthDate: " + birthDateStr);
            System.out.println("phone: " + phone);
            System.out.println("className: " + className);
            System.out.println("department: " + department);
            System.out.println("status: " + status);

            Student student = new Student();

            // 处理ID
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    student.setId(Integer.parseInt(idStr));
                } catch (NumberFormatException e) {
                    System.out.println("ID格式错误，使用新ID");
                }
            }

            // 设置基本字段，提供默认值
            student.setStudentNo(studentNo != null ? studentNo.trim() : "未知学号");
            student.setName(name != null ? name.trim() : "未知姓名");
            student.setGender(gender != null ? gender.trim() : "男");

            // 处理年龄
            try {
                student.setAge(Integer.parseInt(ageStr));
            } catch (NumberFormatException e) {
                student.setAge(18); // 默认年龄
            }

            // 处理出生日期
            try {
                if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
                    Date birthDate = dateFormat.parse(birthDateStr);
                    student.setBirthDate(birthDate);
                } else {
                    student.setBirthDate(new Date()); // 默认当前日期
                }
            } catch (Exception e) {
                student.setBirthDate(new Date()); // 默认当前日期
            }

            student.setPhone(phone != null ? phone.trim() : "未知手机号");
            student.setClassName(className != null ? className.trim() : "未知班级");
            student.setDepartment(department != null ? department.trim() : "未知院系");
            student.setStatus(status != null ? status.trim() : "在读");

            Student savedStudent = studentService.saveStudent(student);
            System.out.println("保存成功，学生ID: " + savedStudent.getId());

            return "redirect:/student/list";
        } catch (Exception e) {
            System.err.println("保存学生信息失败:");
            e.printStackTrace();
            return "redirect:/student/list?error=true";
        }
    }

    @GetMapping("/student/delete")
    public String deleteStudent(@RequestParam Integer id) {
        try {
            studentService.deleteStudent(id);
            System.out.println("删除学生成功，ID: " + id);
        } catch (Exception e) {
            System.err.println("删除学生失败:");
            e.printStackTrace();
        }
        return "redirect:/student/list";
    }

    @GetMapping("/student/search")
    public String searchStudents(@RequestParam String keyword, Model model) {
        List<Student> students = studentService.searchStudents(keyword);
        model.addAttribute("students", students);
        model.addAttribute("searchKeyword", keyword);
        return "student/list";
    }

    @GetMapping("/student/statistics")
    public String statistics(Model model) {
        // Gender statistics
        model.addAttribute("genderStats", studentService.getGenderStatistics());

        // All students for relationship analysis
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);

        return "student/statistics";
    }
}