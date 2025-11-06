package com.example.stusyshomework.repository;

import com.example.stusyshomework.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByStudentNoContainingOrNameContaining(String studentNo, String name);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.gender = ?1")
    Long countByGender(String gender);
}