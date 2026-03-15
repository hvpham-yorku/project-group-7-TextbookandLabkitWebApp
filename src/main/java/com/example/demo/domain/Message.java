package com.example.demo.domain;

import java.time.LocalDateTime;

public class Message {

    private Long id;
    private Long listingId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime timestamp;

    public Message() {
    }

    public Message(Long id, Long listingId, Long senderId, Long receiverId, String content) {
        this.id = id;
        this.listingId = listingId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getListingId() {
        return listingId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
