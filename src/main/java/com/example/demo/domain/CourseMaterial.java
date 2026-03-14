package com.example.demo.domain;

import java.math.BigDecimal;

public class CourseMaterial {

    private String courseCode;
    private String title;
    private String isbn;
    private boolean required;
    private BigDecimal bookstorePrice;

    public CourseMaterial() {}

    public CourseMaterial(String courseCode, String title, String isbn, boolean required, BigDecimal bookstorePrice) {
        this.courseCode = courseCode;
        this.title = title;
        this.isbn = isbn;
        this.required = required;
        this.bookstorePrice = bookstorePrice;
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public BigDecimal getBookstorePrice() { return bookstorePrice; }
    public void setBookstorePrice(BigDecimal bookstorePrice) { this.bookstorePrice = bookstorePrice; }
}