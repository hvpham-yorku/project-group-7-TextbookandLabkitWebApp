package com.example.demo.domain;

public class User {

    // Immutable identity fields
    private final String email;
    private final String password;

    // Mutable profile fields
    private String name;
    private String studentId;
    private String phoneNumber;
    private String aboutMe;
    private String program;
    private String campus;

    // Existing 3-arg constructor — kept so all existing code and tests compile unchanged
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // --- Getters ---

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getProgram() {
        return program;
    }

    public String getCampus() {
        return campus;
    }

    // --- Setters for mutable fields ---

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }
}
