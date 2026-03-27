package com.example.demo.repository;

import com.example.demo.domain.Notification;

import java.util.List;

public interface NotificationRepository {
    Notification save(Notification notification);
    Notification findById(long id);
    List<Notification> findByRecipientEmail(String recipientEmail);
    List<Notification> findUnreadByRecipientEmail(String recipientEmail);
    int countUnreadByRecipientEmail(String recipientEmail);
    void markAsRead(long id);
}
