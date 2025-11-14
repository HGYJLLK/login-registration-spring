package com.example.stusyshomework.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "course_no", unique = true, nullable = false, length = 50)
    private String courseNo;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String term;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column
    private Integer credit;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    // Constructors
    public Course() {
    }

    public Course(String courseNo, String name, String term, String introduction, Integer credit) {
        this.courseNo = courseNo;
        this.name = name;
        this.term = term;
        this.introduction = introduction;
        this.credit = credit;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    // toString without relationship field to avoid circular reference
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseNo='" + courseNo + '\'' +
                ", name='" + name + '\'' +
                ", term='" + term + '\'' +
                ", credit=" + credit +
                '}';
    }
}
