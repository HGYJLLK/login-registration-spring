package com.example.stusyshomework.service.impl;

import com.example.stusyshomework.entity.CourseMB;
import com.example.stusyshomework.mapper.CourseMapper;
import com.example.stusyshomework.service.CourseMBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CourseMBServiceImpl implements CourseMBService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<CourseMB> getAllCourses() {
        return courseMapper.findAll();
    }

    @Override
    public CourseMB getCourseById(String courseId) {
        return courseMapper.findById(courseId);
    }

    @Override
    @Transactional
    public void saveCourse(CourseMB course) {
        courseMapper.insert(course);
    }

    @Override
    @Transactional
    public void updateCourse(CourseMB course) {
        courseMapper.update(course);
    }

    @Override
    @Transactional
    public void deleteCourse(String courseId) {
        courseMapper.deleteById(courseId);
    }
}
