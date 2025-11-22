package com.example.stusyshomework.service;

import com.example.stusyshomework.entity.CourseMB;
import java.util.List;

public interface CourseMBService {

    List<CourseMB> getAllCourses();

    CourseMB getCourseById(String courseId);

    void saveCourse(CourseMB course);

    void updateCourse(CourseMB course);

    void deleteCourse(String courseId);
}
