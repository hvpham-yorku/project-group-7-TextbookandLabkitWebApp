package com.example.demo.domain;

import java.time.LocalDateTime;

public class Transaction {

    private long id;
    private long listingId;
    private String listingTitle;
    private String courseCode;
    private long sourceMessageId;
    private String buyerEmail;
    private String sellerEmail;
    private TransactionStatus status;
    private boolean buyerConfirmed;
    private boolean sellerConfirmed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Transaction() {
        this.status = TransactionStatus.MEETUP_PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getListingId() { return listingId; }
    public void setListingId(long listingId) { this.listingId = listingId; }

    public String getListingTitle() { return listingTitle; }
    public void setListingTitle(String listingTitle) { this.listingTitle = listingTitle; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public long getSourceMessageId() { return sourceMessageId; }
    public void setSourceMessageId(long sourceMessageId) { this.sourceMessageId = sourceMessageId; }

    public String getBuyerEmail() { return buyerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }

    public String getSellerEmail() { return sellerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public boolean isBuyerConfirmed() { return buyerConfirmed; }
    public void setBuyerConfirmed(boolean buyerConfirmed) { this.buyerConfirmed = buyerConfirmed; }

    public boolean isSellerConfirmed() { return sellerConfirmed; }
    public void setSellerConfirmed(boolean sellerConfirmed) { this.sellerConfirmed = sellerConfirmed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isParty(String email) {
        return email != null && (email.equalsIgnoreCase(buyerEmail) || email.equalsIgnoreCase(sellerEmail));
    }

    public boolean isSeller(String email) {
        return email != null && email.equalsIgnoreCase(sellerEmail);
    }

    public boolean isBuyer(String email) {
        return email != null && email.equalsIgnoreCase(buyerEmail);
    }

    public boolean isTerminal() {
        return status == TransactionStatus.COMPLETED
                || status == TransactionStatus.CANCELLED
                || status == TransactionStatus.ISSUE_OPEN;
    }

    public String getStatusLabel() {
        return switch (status) {
            case MEETUP_PENDING -> "Meetup pending";
            case AWAITING_BUYER_CONFIRMATION -> "Awaiting buyer confirmation";
            case AWAITING_SELLER_CONFIRMATION -> "Awaiting seller confirmation";
            case COMPLETED -> "Completed";
            case CANCELLED -> "Cancelled";
            case ISSUE_OPEN -> "Issue open";
        };
    }

    public int getProgressPercent() {
        return switch (status) {
            case MEETUP_PENDING -> 25;
            case AWAITING_BUYER_CONFIRMATION, AWAITING_SELLER_CONFIRMATION -> 65;
            case COMPLETED -> 100;
            case CANCELLED -> 0;
            case ISSUE_OPEN -> 45;
        };
    }

    public String getNextStepText() {
        return switch (status) {
            case MEETUP_PENDING -> "Next step: meet and confirm the exchange from both sides.";
            case AWAITING_BUYER_CONFIRMATION -> "Next step: buyer must confirm receipt to complete the exchange.";
            case AWAITING_SELLER_CONFIRMATION -> "Next step: seller must confirm handoff to complete the exchange.";
            case COMPLETED -> "Exchange complete. The listing has been marked as sold.";
            case CANCELLED -> "This exchange was cancelled before completion.";
            case ISSUE_OPEN -> "An issue was reported. Completion is paused until the issue is reviewed.";
        };
    }
}
