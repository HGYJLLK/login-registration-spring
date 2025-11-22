package com.example.stusyshomework.mapper;

import com.example.stusyshomework.entity.StudentMB;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface StudentMapper {

    List<StudentMB> findAll();

    StudentMB findByStudentId(String studentId);
}
