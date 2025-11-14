package com.example.stusyshomework.controller;

import com.example.stusyshomework.entity.Course;
import com.example.stusyshomework.entity.Student;
import com.example.stusyshomework.service.CourseService;
import com.example.stusyshomework.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public String listCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "course/list";
    }

    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course) {
        try {
            courseService.saveCourse(course);
            return "redirect:/course/list";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/course/list?error=true";
        }
    }

    @GetMapping("/delete")
    public String deleteCourse(@RequestParam Integer id) {
        try {
            courseService.deleteCourse(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/course/list";
    }

    @GetMapping("/bind/{courseId}")
    public String bindStudents(@PathVariable Integer courseId, Model model) {
        Course course = courseService.getCourseById(courseId);
        List<Student> allStudents = studentService.getAllStudents();

        model.addAttribute("course", course);
        model.addAttribute("allStudents", allStudents);
        return "course/bind";
    }

    @PostMapping("/bind/save")
    public String saveBindStudents(@RequestParam Integer courseId,
                                   @RequestParam(required = false) List<Integer> studentIds) {
        try {
            courseService.assignStudentsToCourse(courseId, studentIds);
            return "redirect:/course/list";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/course/list?error=true";
        }
    }
}
