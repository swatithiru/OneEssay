package com.example.oneessay;

import java.io.Serializable;

public class Student implements Serializable {

    private  String name;
    private  String email;
    private  String password;
    private  String studentid;
    private  String professor;

    public Student(String name, String email, String password, String studentid, String professor) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.studentid = studentid;
        this.professor = professor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", studentid='" + studentid + '\'' +
                ", professor='" + professor + '\'' +
                '}';
    }
}
