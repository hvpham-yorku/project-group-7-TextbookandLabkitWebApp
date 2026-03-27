package com.example.demo.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.domain.Notification;
import com.example.demo.domain.NotificationType;
import com.example.demo.domain.User;
import com.example.demo.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Satisfies both NotificationController and GlobalModelAdvice
    @MockBean
    private NotificationService notificationService;

    // -------------------------------------------------------------------------
    // GET /notifications
    // -------------------------------------------------------------------------

    @Test
    void getNotifications_withoutSession_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/notifications"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void getNotifications_withSession_returnsNotificationsView() throws Exception {
        User user = new User("alice@my.yorku.ca", "pass", "Alice");

        when(notificationService.getNotificationsForUser("alice@my.yorku.ca"))
                .thenReturn(List.of());
        when(notificationService.countUnreadNotifications("alice@my.yorku.ca"))
                .thenReturn(0);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(get("/notifications").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("notifications"))
                .andExpect(model().attributeExists("notifications"))
                .andExpect(model().attributeExists("unreadCount"));
    }

    @Test
    void getNotifications_withSession_notificationsListIsInModel() throws Exception {
        User user = new User("alice@my.yorku.ca", "pass", "Alice");
        Notification n = new Notification(1L, "alice@my.yorku.ca", "test message", NotificationType.MESSAGE);

        when(notificationService.getNotificationsForUser("alice@my.yorku.ca"))
                .thenReturn(List.of(n));
        when(notificationService.countUnreadNotifications("alice@my.yorku.ca"))
                .thenReturn(1);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(get("/notifications").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("notifications", List.of(n)))
                .andExpect(model().attribute("unreadCount", 1));
    }

    // -------------------------------------------------------------------------
    // POST /notifications/{id}/read
    // -------------------------------------------------------------------------

    @Test
    void postMarkAsRead_withoutSession_redirectsToLogin() throws Exception {
        mockMvc.perform(post("/notifications/1/read"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void postMarkAsRead_withSession_callsMarkAsReadAndRedirects() throws Exception {
        User user = new User("alice@my.yorku.ca", "pass", "Alice");

        when(notificationService.countUnreadNotifications(anyString())).thenReturn(0);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(post("/notifications/42/read").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications"));

        verify(notificationService).markAsRead(42L);
    }
}
