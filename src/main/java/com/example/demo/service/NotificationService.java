package com.example.demo.service;

import com.example.demo.domain.Notification;
import com.example.demo.domain.NotificationType;
import com.example.demo.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Creates and saves a new notification for the given recipient.
     * Returns the saved notification, or null if inputs are invalid.
     * isRead defaults to false and createdAt defaults to now() via the no-arg constructor.
     */
    public Notification createNotification(String recipientEmail, String message, NotificationType type) {
        if (recipientEmail == null || recipientEmail.isBlank()) return null;
        if (message == null || message.isBlank()) return null;
        if (type == null) return null;

        Notification notification = new Notification();
        notification.setRecipientEmail(recipientEmail.trim());
        notification.setMessage(message.trim());
        notification.setType(type);

        return notificationRepository.save(notification);
    }

    /**
     * Returns all notifications for a user, newest first.
     */
    public List<Notification> getNotificationsForUser(String recipientEmail) {
        if (recipientEmail == null || recipientEmail.isBlank()) return List.of();
        return notificationRepository.findByRecipientEmail(recipientEmail);
    }

    /**
     * Returns only unread notifications for a user, newest first.
     */
    public List<Notification> getUnreadNotificationsForUser(String recipientEmail) {
        if (recipientEmail == null || recipientEmail.isBlank()) return List.of();
        return notificationRepository.findUnreadByRecipientEmail(recipientEmail);
    }

    /**
     * Returns the count of unread notifications for a user.
     */
    public int countUnreadNotifications(String recipientEmail) {
        if (recipientEmail == null || recipientEmail.isBlank()) return 0;
        return notificationRepository.countUnreadByRecipientEmail(recipientEmail);
    }

    /**
     * Marks a single notification as read.
     */
    public void markAsRead(long id) {
        notificationRepository.markAsRead(id);
    }
}
