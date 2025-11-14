package com.example.stusyshomework.service.impl;

import com.example.stusyshomework.entity.Student;
import com.example.stusyshomework.repository.CardRepository;
import com.example.stusyshomework.repository.StudentRepository;
import com.example.stusyshomework.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Integer id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> searchStudents(String keyword) {
        return studentRepository.findByStudentNoContainingOrNameContaining(keyword, keyword);
    }

    @Override
    public Map<String, Object> getGenderStatistics() {
        Long maleCount = studentRepository.countByGender("男");
        Long femaleCount = studentRepository.countByGender("女");
        Long total = studentRepository.count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("maleCount", maleCount);
        stats.put("femaleCount", femaleCount);
        stats.put("malePercentage", total > 0 ? String.format("%.1f%%", (maleCount * 100.0 / total)) : "0%");
        stats.put("femalePercentage", total > 0 ? String.format("%.1f%%", (femaleCount * 100.0 / total)) : "0%");

        return stats;
    }

    @Override
    public List<Student> getStudentsWithoutCard() {
        List<Student> allStudents = studentRepository.findAll();
        return allStudents.stream()
                .filter(student -> cardRepository.findByStudent_Id(student.getId()).isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasCard(Integer studentId) {
        return cardRepository.findByStudent_Id(studentId).isPresent();
    }
}