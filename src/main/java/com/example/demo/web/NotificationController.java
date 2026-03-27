package com.example.demo.web;

import com.example.demo.domain.User;
import com.example.demo.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public String notifications(HttpSession session, Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;
        model.addAttribute("notifications", notificationService.getNotificationsForUser(user.getEmail()));
        model.addAttribute("unreadCount", notificationService.countUnreadNotifications(user.getEmail()));
        return "notifications";
    }

    @PostMapping("/notifications/{id}/read")
    public String markAsRead(@PathVariable("id") long id, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        notificationService.markAsRead(id);
        return "redirect:/notifications";
    }
}
