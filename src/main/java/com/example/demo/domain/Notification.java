package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain object representing a notification delivered to a user.
 * KAN-97: initial model creation.
 */
public class Notification {

    private long id;
    private String recipientEmail;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;

    public Notification() {
        this.isRead    = false;
        this.createdAt = LocalDateTime.now();
    }

    public Notification(long id,
                        String recipientEmail,
                        String message,
                        NotificationType type) {
        this.id             = id;
        this.recipientEmail = recipientEmail;
        this.message        = message;
        this.type           = type;
        this.isRead         = false;
        this.createdAt      = LocalDateTime.now();
    }

    // --- Getters and Setters ---

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { this.isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
