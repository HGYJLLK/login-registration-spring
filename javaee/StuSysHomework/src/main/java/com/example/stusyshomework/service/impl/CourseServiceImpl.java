package com.example.stusyshomework.service.impl;

import com.example.stusyshomework.entity.Course;
import com.example.stusyshomework.entity.Student;
import com.example.stusyshomework.repository.CourseRepository;
import com.example.stusyshomework.repository.StudentRepository;
import com.example.stusyshomework.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Integer id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Integer id) {
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void assignStudentsToCourse(Integer courseId, List<Integer> studentIds) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Clear existing students
        course.getStudents().clear();

        // Add selected students
        if (studentIds != null && !studentIds.isEmpty()) {
            Set<Student> students = new HashSet<>();
            for (Integer studentId : studentIds) {
                Student student = studentRepository.findById(studentId).orElse(null);
                if (student != null) {
                    students.add(student);
                    student.getCourses().add(course);
                }
            }
            course.setStudents(students);
        }

        courseRepository.save(course);
    }
}
