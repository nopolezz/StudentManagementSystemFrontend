package com.teach.javafx.models;


public class Teacher extends Admin {
    private String degree;
    private String employmentTime;
    private String courseTeached;

    public Teacher() {}

    public Teacher(String title, String name) {
        super(title, name);
    }

    public String getDegree() {return degree;}

    public String getEmploymentTime() {return employmentTime;}

    public String getCourse() {return courseTeached;}

    public void setDegree(String degree) {this.degree = degree;}

    public void setEmploymentTime(String employmentTime) {this.employmentTime = employmentTime;}

    public void setCourse(String course) {this.courseTeached = course;}
}