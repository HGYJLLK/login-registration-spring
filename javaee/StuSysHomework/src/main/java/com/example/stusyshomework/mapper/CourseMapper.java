package com.example.stusyshomework.mapper;

import com.example.stusyshomework.entity.CourseMB;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CourseMapper {

    List<CourseMB> findAll();

    CourseMB findById(String courseId);

    void insert(CourseMB course);

    void update(CourseMB course);

    void deleteById(String courseId);
}
