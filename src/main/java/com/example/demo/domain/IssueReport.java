package com.example.demo.domain;

import java.time.LocalDateTime;

public class IssueReport {

    private long id;
    private long transactionId;
    private String reporterEmail;
    private IssueCategory category;
    private IssueSeverity severity;
    private String description;
    private LocalDateTime createdAt;

    public IssueReport() {
        this.createdAt = LocalDateTime.now();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getTransactionId() { return transactionId; }
    public void setTransactionId(long transactionId) { this.transactionId = transactionId; }

    public String getReporterEmail() { return reporterEmail; }
    public void setReporterEmail(String reporterEmail) { this.reporterEmail = reporterEmail; }

    public IssueCategory getCategory() { return category; }
    public void setCategory(IssueCategory category) { this.category = category; }

    public IssueSeverity getSeverity() { return severity; }
    public void setSeverity(IssueSeverity severity) { this.severity = severity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCategoryLabel() {
        if (category == null) return "Issue";
        return switch (category) {
            case NO_SHOW -> "No-show";
            case DAMAGED_ITEM -> "Damaged item";
            case MISLEADING_DESCRIPTION -> "Misleading description";
            case PAYMENT_ISSUE -> "Payment issue";
            case OTHER -> "Other";
        };
    }
}
