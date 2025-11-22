package com.example.stusyshomework.mapper;

import com.example.stusyshomework.entity.CourseMB;
import com.example.stusyshomework.entity.StudentCourseMB;
import com.example.stusyshomework.entity.StudentMB;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StudentCourseMapper {

    List<String> findCourseIdsByStudentId(String studentId);

    List<CourseMB> findCoursesByStudentId(String studentId);

    List<String> findStudentIdsByCourseId(String courseId);

    List<StudentMB> findStudentsByCourseId(String courseId);

    void insert(StudentCourseMB studentCourse);

    void delete(@Param("studentId") String studentId, @Param("courseId") String courseId);

    void deleteByStudentId(String studentId);

    void deleteByCourseId(String courseId);

    StudentCourseMB findByStudentAndCourse(@Param("studentId") String studentId, @Param("courseId") String courseId);
}
