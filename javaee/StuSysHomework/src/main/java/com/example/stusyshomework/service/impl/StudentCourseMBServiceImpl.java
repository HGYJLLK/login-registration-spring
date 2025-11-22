package com.example.stusyshomework.service.impl;

import com.example.stusyshomework.entity.CourseMB;
import com.example.stusyshomework.entity.StudentCourseMB;
import com.example.stusyshomework.entity.StudentMB;
import com.example.stusyshomework.mapper.StudentCourseMapper;
import com.example.stusyshomework.service.StudentCourseMBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StudentCourseMBServiceImpl implements StudentCourseMBService {

    @Autowired
    private StudentCourseMapper studentCourseMapper;

    @Override
    public List<String> getCourseIdsByStudentId(String studentId) {
        return studentCourseMapper.findCourseIdsByStudentId(studentId);
    }

    @Override
    public List<CourseMB> getCoursesByStudentId(String studentId) {
        return studentCourseMapper.findCoursesByStudentId(studentId);
    }

    @Override
    public List<String> getStudentIdsByCourseId(String courseId) {
        return studentCourseMapper.findStudentIdsByCourseId(courseId);
    }

    @Override
    public List<StudentMB> getStudentsByCourseId(String courseId) {
        return studentCourseMapper.findStudentsByCourseId(courseId);
    }

    @Override
    @Transactional
    public void enrollCourse(String studentId, String courseId) {
        if (!isEnrolled(studentId, courseId)) {
            StudentCourseMB studentCourse = new StudentCourseMB(studentId, courseId);
            studentCourseMapper.insert(studentCourse);
        }
    }

    @Override
    @Transactional
    public void enrollCourses(String studentId, List<String> courseIds) {
        studentCourseMapper.deleteByStudentId(studentId);

        if (courseIds != null && !courseIds.isEmpty()) {
            for (String courseId : courseIds) {
                StudentCourseMB studentCourse = new StudentCourseMB(studentId, courseId);
                studentCourseMapper.insert(studentCourse);
            }
        }
    }

    @Override
    @Transactional
    public void batchEnrollStudents(String courseId, List<String> studentIds) {
        studentCourseMapper.deleteByCourseId(courseId);

        if (studentIds != null && !studentIds.isEmpty()) {
            for (String studentId : studentIds) {
                StudentCourseMB studentCourse = new StudentCourseMB(studentId, courseId);
                studentCourseMapper.insert(studentCourse);
            }
        }
    }

    @Override
    @Transactional
    public void removeEnrollment(String studentId, String courseId) {
        studentCourseMapper.delete(studentId, courseId);
    }

    @Override
    @Transactional
    public void removeAllEnrollmentsByStudent(String studentId) {
        studentCourseMapper.deleteByStudentId(studentId);
    }

    @Override
    @Transactional
    public void removeAllEnrollmentsByCourse(String courseId) {
        studentCourseMapper.deleteByCourseId(courseId);
    }

    @Override
    public boolean isEnrolled(String studentId, String courseId) {
        StudentCourseMB enrollment = studentCourseMapper.findByStudentAndCourse(studentId, courseId);
        return enrollment != null;
    }
}
