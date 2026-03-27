package com.example.demo.web;

import com.example.demo.domain.User;
import com.example.demo.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Adds shared model attributes to every controller response.
 * KAN-103: supplies unreadCount so the navbar badge works on all pages.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    private final NotificationService notificationService;

    public GlobalModelAdvice(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ModelAttribute("unreadCount")
    public int unreadCount(HttpSession session) {
        Object u = session.getAttribute("user");
        if (u == null) return 0;
        return notificationService.countUnreadNotifications(((User) u).getEmail());
    }
}
