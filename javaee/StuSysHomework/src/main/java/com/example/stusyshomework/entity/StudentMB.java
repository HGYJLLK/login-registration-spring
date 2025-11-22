package com.example.stusyshomework.entity;

public class StudentMB {
    private String studentId;
    private String name;
    private String major;
    private String grade;

    public StudentMB() {
    }

    public StudentMB(String studentId, String name, String major, String grade) {
        this.studentId = studentId;
        this.name = name;
        this.major = major;
        this.grade = grade;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "StudentMB{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", major='" + major + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
