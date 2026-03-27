package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain object representing a contact message sent from a buyer to a seller
 * about a specific listing.
 * KAN-93: initial model creation.
 */
public class ContactMessage {

    private long id;
    private long listingId;
    private String senderEmail;
    private String sellerEmail;
    private String subject;
    private String message;
    private LocalDateTime createdAt;

    public ContactMessage() {
        this.createdAt = LocalDateTime.now();
    }

    public ContactMessage(long id,
                          long listingId,
                          String senderEmail,
                          String sellerEmail,
                          String subject,
                          String message) {
        this.id          = id;
        this.listingId   = listingId;
        this.senderEmail = senderEmail;
        this.sellerEmail = sellerEmail;
        this.subject     = subject;
        this.message     = message;
        this.createdAt   = LocalDateTime.now();
    }

    // --- Getters and Setters ---

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getListingId() { return listingId; }
    public void setListingId(long listingId) { this.listingId = listingId; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public String getSellerEmail() { return sellerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactMessage that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
