package com.example.demo.domain;

import java.time.LocalDateTime;

public class MaterialRequest {

    private long id;
    private String requesterEmail;
    private String courseCode;
    private String materialTitle;
    private String materialType;
    private String note;
    private MaterialRequestStatus status;
    private LocalDateTime createdAt;

    public MaterialRequest() {
        this.status = MaterialRequestStatus.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    public MaterialRequest(long id, String requesterEmail, String courseCode,
                           String materialTitle, String materialType, String note,
                           MaterialRequestStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.requesterEmail = requesterEmail;
        this.courseCode = courseCode;
        this.materialTitle = materialTitle;
        this.materialType = materialType;
        this.note = note;
        this.status = status;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getRequesterEmail() { return requesterEmail; }
    public void setRequesterEmail(String requesterEmail) { this.requesterEmail = requesterEmail; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getMaterialTitle() { return materialTitle; }
    public void setMaterialTitle(String materialTitle) { this.materialTitle = materialTitle; }

    public String getMaterialType() { return materialType; }
    public void setMaterialType(String materialType) { this.materialType = materialType; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public MaterialRequestStatus getStatus() { return status; }
    public void setStatus(MaterialRequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public String getDisplayTitle() {
        return materialTitle == null || materialTitle.isBlank() ? "Requested material" : materialTitle;
    }
}
