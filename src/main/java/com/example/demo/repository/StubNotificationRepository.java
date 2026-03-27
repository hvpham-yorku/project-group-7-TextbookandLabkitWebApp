package com.example.demo.repository;

import com.example.demo.domain.Notification;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("stub")
public class StubNotificationRepository implements NotificationRepository {

    private final List<Notification> notifications = new ArrayList<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    @Override
    public Notification save(Notification notification) {
        notification.setId(idSeq.getAndIncrement());
        notifications.add(notification);
        return notification;
    }

    @Override
    public Notification findById(long id) {
        for (Notification n : notifications) {
            if (n.getId() == id) return n;
        }
        return null;
    }

    @Override
    public List<Notification> findByRecipientEmail(String recipientEmail) {
        List<Notification> result = new ArrayList<>();
        for (Notification n : notifications) {
            if (n.getRecipientEmail().equalsIgnoreCase(recipientEmail)) {
                result.add(n);
            }
        }
        result.sort(Comparator.comparing(Notification::getCreatedAt).reversed());
        return result;
    }

    @Override
    public List<Notification> findUnreadByRecipientEmail(String recipientEmail) {
        List<Notification> result = new ArrayList<>();
        for (Notification n : notifications) {
            if (n.getRecipientEmail().equalsIgnoreCase(recipientEmail) && !n.isRead()) {
                result.add(n);
            }
        }
        result.sort(Comparator.comparing(Notification::getCreatedAt).reversed());
        return result;
    }

    @Override
    public int countUnreadByRecipientEmail(String recipientEmail) {
        return findUnreadByRecipientEmail(recipientEmail).size();
    }

    @Override
    public void markAsRead(long id) {
        for (Notification n : notifications) {
            if (n.getId() == id) {
                n.setRead(true);
                return;
            }
        }
    }
}
