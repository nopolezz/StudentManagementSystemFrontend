package com.teach.javafx.models;


public class Admin{
    protected Integer personId;          //学号/工号
    protected String title;              //头衔
    protected String name;               //姓名
    protected String type;               //类型（学生或老师）
    protected String dept;               //学院
    protected String card;               //身份证号
    protected String gender;             //性别
    protected String genderName;         //性别名称
    protected String birthday;           //出生日期
    protected String email;              //邮箱
    protected String phone;              //电话
    protected String address;            //地址
    protected String introduce;          //简介

    /**
     * 构造方法
     */
    public Admin(){}
    public Admin(String title, String name){
        this.title = title;
        this.name = name;
    }

    /**
     * get set 方法
     */
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String num) {
        this.title = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String toString(){
        return title+"-" + name;
    }
}
