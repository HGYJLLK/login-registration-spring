package com.example.stusyshomework.service;

import com.example.stusyshomework.entity.CourseMB;
import com.example.stusyshomework.entity.StudentMB;
import java.util.List;

public interface StudentCourseMBService {

    List<String> getCourseIdsByStudentId(String studentId);

    List<CourseMB> getCoursesByStudentId(String studentId);

    List<String> getStudentIdsByCourseId(String courseId);

    List<StudentMB> getStudentsByCourseId(String courseId);

    void enrollCourse(String studentId, String courseId);

    void enrollCourses(String studentId, List<String> courseIds);

    void batchEnrollStudents(String courseId, List<String> studentIds);

    void removeEnrollment(String studentId, String courseId);

    void removeAllEnrollmentsByStudent(String studentId);

    void removeAllEnrollmentsByCourse(String courseId);

    boolean isEnrolled(String studentId, String courseId);
}
