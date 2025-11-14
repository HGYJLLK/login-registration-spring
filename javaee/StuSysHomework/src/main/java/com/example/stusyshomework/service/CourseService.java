package com.example.stusyshomework.service;

import com.example.stusyshomework.entity.Course;
import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    Course getCourseById(Integer id);
    Course saveCourse(Course course);
    void deleteCourse(Integer id);

    // Batch assign students to a course
    void assignStudentsToCourse(Integer courseId, List<Integer> studentIds);
}
