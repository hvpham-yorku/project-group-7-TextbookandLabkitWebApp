package com.example.demo.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Domain object representing a YorkU course-material listing.
 * KAN-60: updated to support academic fields (courseCode, semester, etc.)
 */
public class Listing {

    private long id;
    private String sellerEmail;

    private String title;
    private String description;
    private BigDecimal price;

    // Academic / course-specific fields
    private String courseCode;    // e.g. "EECS 2311"
    private String semester;      // e.g. "Winter 2025"
    private String materialType;  // e.g. "Textbook", "Lab Kit", "Notes"
    private String condition;     // e.g. "New", "Good", "Fair", "Poor"
    private String exchangeType;  // e.g. "Sell", "Trade", "Free"

    private ListingStatus status;

    // No-arg constructor (required for Thymeleaf form binding)
    public Listing() {
        this.status = ListingStatus.AVAILABLE;
    }

    // Full constructor
    public Listing(long id,
                   String sellerEmail,
                   String title,
                   String description,
                   BigDecimal price,
                   String courseCode,
                   String semester,
                   String materialType,
                   String condition,
                   String exchangeType,
                   ListingStatus status) {

        this.id = id;
        this.sellerEmail = sellerEmail;
        this.title = title;
        this.description = description;
        this.price = price;
        this.courseCode = courseCode;
        this.semester = semester;
        this.materialType = materialType;
        this.condition = condition;
        this.exchangeType = exchangeType;
        this.status = status == null ? ListingStatus.AVAILABLE : status;
    }

    // --- Getters and Setters ---

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getSellerEmail() { return sellerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getMaterialType() { return materialType; }
    public void setMaterialType(String materialType) { this.materialType = materialType; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getExchangeType() { return exchangeType; }
    public void setExchangeType(String exchangeType) { this.exchangeType = exchangeType; }

    public ListingStatus getStatus() { return status; }
    public void setStatus(ListingStatus status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Listing listing)) return false;
        return id == listing.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}