package com.teach.javafx.models;


public class Student extends Admin {
    private String major;
    private String className;
    public Student(){}
    public Student(String title, String name){
        super(title , name);
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
