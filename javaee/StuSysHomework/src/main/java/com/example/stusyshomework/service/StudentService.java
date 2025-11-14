package com.example.stusyshomework.service;

import com.example.stusyshomework.entity.Student;
import java.util.List;
import java.util.Map;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(Integer id);
    Student saveStudent(Student student);
    void deleteStudent(Integer id);
    List<Student> searchStudents(String keyword);
    Map<String, Object> getGenderStatistics();

    // Card association methods
    List<Student> getStudentsWithoutCard();
    boolean hasCard(Integer studentId);
}