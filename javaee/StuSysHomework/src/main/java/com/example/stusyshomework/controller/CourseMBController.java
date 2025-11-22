package com.example.stusyshomework.controller;

import com.example.stusyshomework.entity.CourseMB;
import com.example.stusyshomework.entity.StudentMB;
import com.example.stusyshomework.mapper.StudentMapper;
import com.example.stusyshomework.service.CourseMBService;
import com.example.stusyshomework.service.StudentCourseMBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseMBController {

    @Autowired
    private CourseMBService courseService;

    @Autowired
    private StudentCourseMBService studentCourseService;

    @Autowired
    private StudentMapper studentMapper;

    @GetMapping("/list")
    public String list(Model model) {
        List<CourseMB> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        model.addAttribute("size", 10);
        return "course/list";
    }

    @GetMapping("/form")
    public String form(@RequestParam(required = false) String courseId, Model model) {
        if (courseId != null) {
            CourseMB course = courseService.getCourseById(courseId);
            model.addAttribute("course", course);
        }
        return "course/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String courseId,
                       @RequestParam String courseName,
                       @RequestParam String term,
                       @RequestParam Integer credit,
                       @RequestParam String description) {
        CourseMB existingCourse = courseService.getCourseById(courseId);

        if (existingCourse != null) {
            existingCourse.setCourseName(courseName);
            existingCourse.setTerm(term);
            existingCourse.setCredit(credit);
            existingCourse.setDescription(description);
            courseService.updateCourse(existingCourse);
        } else {
            CourseMB course = new CourseMB(courseId, courseName, term, description, credit);
            courseService.saveCourse(course);
        }

        return "redirect:/course/list";
    }

    @GetMapping("/delete/{courseId}")
    public String delete(@PathVariable String courseId) {
        studentCourseService.removeAllEnrollmentsByCourse(courseId);
        courseService.deleteCourse(courseId);
        return "redirect:/course/list";
    }

    @GetMapping("/chooseForStudent/{studentId}")
    public String chooseForStudent(@PathVariable String studentId, Model model) {
        StudentMB student = studentMapper.findByStudentId(studentId);
        List<CourseMB> allCourses = courseService.getAllCourses();
        List<String> selectedCourseIds = studentCourseService.getCourseIdsByStudentId(studentId);

        model.addAttribute("student", student);
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("selectedCourseIds", selectedCourseIds);

        return "course/chooseForStudent";
    }

    @PostMapping("/studentChoose")
    public String studentChoose(@RequestParam String studentId,
                                @RequestParam(required = false) List<String> courseIds) {
        studentCourseService.enrollCourses(studentId, courseIds);
        return "redirect:/student/list";
    }

    @GetMapping("/studentCourses/{studentId}")
    public String studentCourses(@PathVariable String studentId, Model model) {
        StudentMB student = studentMapper.findByStudentId(studentId);
        List<CourseMB> courses = studentCourseService.getCoursesByStudentId(studentId);

        model.addAttribute("student", student);
        model.addAttribute("courses", courses);

        return "course/studentCourses";
    }

    @GetMapping("/removeFromStudent/{studentId}/{courseId}")
    public String removeFromStudent(@PathVariable String studentId,
                                    @PathVariable String courseId) {
        studentCourseService.removeEnrollment(studentId, courseId);
        return "redirect:/course/studentCourses/" + studentId;
    }

    @GetMapping("/selectStudents/{courseId}")
    public String selectStudents(@PathVariable String courseId, Model model) {
        CourseMB course = courseService.getCourseById(courseId);
        List<StudentMB> students = studentMapper.findAll();
        List<String> selectedStudentIds = studentCourseService.getStudentIdsByCourseId(courseId);

        model.addAttribute("course", course);
        model.addAttribute("students", students);
        model.addAttribute("selectedStudentIds", selectedStudentIds);

        return "course/selectStudents";
    }

    @PostMapping("/batchSelect")
    public String batchSelect(@RequestParam String courseId,
                              @RequestParam(required = false) List<String> studentIds) {
        studentCourseService.batchEnrollStudents(courseId, studentIds);
        return "redirect:/course/list";
    }
}
