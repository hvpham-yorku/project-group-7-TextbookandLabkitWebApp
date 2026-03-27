package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.Notification;
import com.example.demo.domain.NotificationType;
import com.example.demo.repository.StubNotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NotificationServiceTest {

    private StubNotificationRepository repo;
    private NotificationService service;

    @BeforeEach
    void setUp() {
        repo    = new StubNotificationRepository();
        service = new NotificationService(repo);
    }

    // -------------------------------------------------------------------------
    // createNotification
    // -------------------------------------------------------------------------

    @Test
    void createNotification_validInputs_returnsNotificationWithCorrectFields() {
        Notification n = service.createNotification(
                "alice@my.yorku.ca", "You received a new message", NotificationType.MESSAGE);

        assertNotNull(n);
        assertEquals("alice@my.yorku.ca",          n.getRecipientEmail());
        assertEquals("You received a new message",  n.getMessage());
        assertEquals(NotificationType.MESSAGE,       n.getType());
    }

    @Test
    void createNotification_newNotificationIsUnread() {
        Notification n = service.createNotification(
                "alice@my.yorku.ca", "msg", NotificationType.MESSAGE);

        assertNotNull(n);
        assertFalse(n.isRead());
    }

    @Test
    void createNotification_timestampIsSet() {
        Notification n = service.createNotification(
                "alice@my.yorku.ca", "msg", NotificationType.MESSAGE);

        assertNotNull(n);
        assertNotNull(n.getCreatedAt());
    }

    @Test
    void createNotification_idIsAssigned() {
        Notification n = service.createNotification(
                "alice@my.yorku.ca", "msg", NotificationType.SYSTEM);

        assertNotNull(n);
        assertTrue(n.getId() > 0);
    }

    @Test
    void createNotification_emailIsTrimmed() {
        Notification n = service.createNotification(
                "  alice@my.yorku.ca  ", "msg", NotificationType.MESSAGE);

        assertNotNull(n);
        assertEquals("alice@my.yorku.ca", n.getRecipientEmail());
    }

    @Test
    void createNotification_messageIsTrimmed() {
        Notification n = service.createNotification(
                "alice@my.yorku.ca", "  hello  ", NotificationType.MESSAGE);

        assertNotNull(n);
        assertEquals("hello", n.getMessage());
    }

    @Test
    void createNotification_nullEmail_returnsNull() {
        assertNull(service.createNotification(null, "msg", NotificationType.MESSAGE));
    }

    @Test
    void createNotification_blankEmail_returnsNull() {
        assertNull(service.createNotification("   ", "msg", NotificationType.MESSAGE));
    }

    @Test
    void createNotification_nullMessage_returnsNull() {
        assertNull(service.createNotification("alice@my.yorku.ca", null, NotificationType.MESSAGE));
    }

    @Test
    void createNotification_blankMessage_returnsNull() {
        assertNull(service.createNotification("alice@my.yorku.ca", "", NotificationType.MESSAGE));
    }

    @Test
    void createNotification_nullType_returnsNull() {
        assertNull(service.createNotification("alice@my.yorku.ca", "msg", null));
    }

    // -------------------------------------------------------------------------
    // getNotificationsForUser
    // -------------------------------------------------------------------------

    @Test
    void getNotificationsForUser_returnsOnlyTheirNotifications() {
        service.createNotification("alice@my.yorku.ca", "for alice", NotificationType.MESSAGE);
        service.createNotification("bob@my.yorku.ca",   "for bob",   NotificationType.MESSAGE);

        List<Notification> result = service.getNotificationsForUser("alice@my.yorku.ca");

        assertEquals(1, result.size());
        assertEquals("alice@my.yorku.ca", result.get(0).getRecipientEmail());
    }

    @Test
    void getNotificationsForUser_emailLookupIsCaseInsensitive() {
        service.createNotification("alice@my.yorku.ca", "msg", NotificationType.MESSAGE);

        List<Notification> result = service.getNotificationsForUser("ALICE@MY.YORKU.CA");

        assertEquals(1, result.size());
    }

    @Test
    void getNotificationsForUser_noNotifications_returnsEmptyList() {
        List<Notification> result = service.getNotificationsForUser("nobody@my.yorku.ca");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getNotificationsForUser_nullEmail_returnsEmptyList() {
        List<Notification> result = service.getNotificationsForUser(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getNotificationsForUser_blankEmail_returnsEmptyList() {
        List<Notification> result = service.getNotificationsForUser("   ");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // countUnreadNotifications
    // -------------------------------------------------------------------------

    @Test
    void countUnreadNotifications_allUnread_returnsCorrectCount() {
        service.createNotification("alice@my.yorku.ca", "msg1", NotificationType.MESSAGE);
        service.createNotification("alice@my.yorku.ca", "msg2", NotificationType.MESSAGE);

        assertEquals(2, service.countUnreadNotifications("alice@my.yorku.ca"));
    }

    @Test
    void countUnreadNotifications_onlyCountsCurrentUserNotifications() {
        service.createNotification("alice@my.yorku.ca", "for alice", NotificationType.MESSAGE);
        service.createNotification("bob@my.yorku.ca",   "for bob",   NotificationType.MESSAGE);

        assertEquals(1, service.countUnreadNotifications("alice@my.yorku.ca"));
    }

    @Test
    void countUnreadNotifications_noNotifications_returnsZero() {
        assertEquals(0, service.countUnreadNotifications("nobody@my.yorku.ca"));
    }

    @Test
    void countUnreadNotifications_nullEmail_returnsZero() {
        assertEquals(0, service.countUnreadNotifications(null));
    }

    @Test
    void countUnreadNotifications_afterMarkAsRead_countDecreases() {
        Notification n1 = service.createNotification("alice@my.yorku.ca", "msg1", NotificationType.MESSAGE);
                          service.createNotification("alice@my.yorku.ca", "msg2", NotificationType.MESSAGE);

        assertEquals(2, service.countUnreadNotifications("alice@my.yorku.ca"));

        service.markAsRead(n1.getId());

        assertEquals(1, service.countUnreadNotifications("alice@my.yorku.ca"));
    }

    // -------------------------------------------------------------------------
    // markAsRead
    // -------------------------------------------------------------------------

    @Test
    void markAsRead_setsIsReadToTrue() {
        Notification n = service.createNotification("alice@my.yorku.ca", "msg", NotificationType.MESSAGE);
        assertFalse(n.isRead());

        service.markAsRead(n.getId());

        assertTrue(repo.findById(n.getId()).isRead());
    }

    @Test
    void markAsRead_doesNotAffectOtherNotifications() {
        Notification n1 = service.createNotification("alice@my.yorku.ca", "msg1", NotificationType.MESSAGE);
        Notification n2 = service.createNotification("alice@my.yorku.ca", "msg2", NotificationType.MESSAGE);

        service.markAsRead(n1.getId());

        assertFalse(repo.findById(n2.getId()).isRead());
    }

    @Test
    void markAsRead_nonExistentId_doesNotThrow() {
        assertDoesNotThrow(() -> service.markAsRead(999L));
    }

    // -------------------------------------------------------------------------
    // getUnreadNotificationsForUser
    // -------------------------------------------------------------------------

    @Test
    void getUnreadNotificationsForUser_excludesReadNotifications() {
        Notification n1 = service.createNotification("alice@my.yorku.ca", "unread",    NotificationType.MESSAGE);
        Notification n2 = service.createNotification("alice@my.yorku.ca", "will read", NotificationType.MESSAGE);

        service.markAsRead(n2.getId());

        List<Notification> unread = service.getUnreadNotificationsForUser("alice@my.yorku.ca");

        assertEquals(1, unread.size());
        assertEquals(n1.getId(), unread.get(0).getId());
    }

    @Test
    void getUnreadNotificationsForUser_allRead_returnsEmptyList() {
        Notification n = service.createNotification("alice@my.yorku.ca", "msg", NotificationType.MESSAGE);
        service.markAsRead(n.getId());

        List<Notification> unread = service.getUnreadNotificationsForUser("alice@my.yorku.ca");

        assertNotNull(unread);
        assertTrue(unread.isEmpty());
    }

    @Test
    void getUnreadNotificationsForUser_nullEmail_returnsEmptyList() {
        List<Notification> result = service.getUnreadNotificationsForUser(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
