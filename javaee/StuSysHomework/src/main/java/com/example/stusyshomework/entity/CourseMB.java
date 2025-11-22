package com.example.stusyshomework.entity;

public class CourseMB {
    private String courseId;
    private String courseName;
    private String term;
    private String description;
    private Integer credit;

    public CourseMB() {
    }

    public CourseMB(String courseId, String courseName, String term, String description, Integer credit) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.term = term;
        this.description = description;
        this.credit = credit;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "CourseMB{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", term='" + term + '\'' +
                ", description='" + description + '\'' +
                ", credit=" + credit +
                '}';
    }
}
