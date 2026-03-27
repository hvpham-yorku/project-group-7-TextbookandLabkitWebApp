package com.example.demo.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.domain.User;
import com.example.demo.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

/**
 * KAN-100 / KAN-104 — verifies that a successful contact-seller action
 * creates a notification for the seller (end-to-end through the full web stack).
 *
 * Uses the stub profile (active by default via application.properties) so no
 * real database is needed.
 *
 * Stub seed data relied on here:
 *   Listing ID 1  → seller: abc123@my.yorku.ca
 *   User saif0@my.yorku.ca  → valid buyer (different from the seller above)
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ContactSellerNotificationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationService notificationService;

    @Test
    void contactSeller_validMessage_createsUnreadNotificationForSeller() throws Exception {
        String sellerEmail = "abc123@my.yorku.ca";

        // Capture baseline before the action (context may be shared across tests)
        int countBefore = notificationService.countUnreadNotifications(sellerEmail);

        User buyer = new User("saif0@my.yorku.ca", "1234", "Saif");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", buyer);

        mockMvc.perform(post("/listings/1/contact")
                        .session(session)
                        .param("subject", "Interested in your textbook")
                        .param("message", "Is this still available?"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listings/1?contacted=true"));

        // Seller must have exactly one more unread notification than before
        assertEquals(countBefore + 1, notificationService.countUnreadNotifications(sellerEmail));
    }

    @Test
    void contactSeller_blankMessage_doesNotCreateNotification() throws Exception {
        String sellerEmail = "abc123@my.yorku.ca";
        int countBefore = notificationService.countUnreadNotifications(sellerEmail);

        User buyer = new User("saif0@my.yorku.ca", "1234", "Saif");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", buyer);

        // Blank message — controller returns form with errors, no service call
        mockMvc.perform(post("/listings/1/contact")
                        .session(session)
                        .param("subject", "Hello")
                        .param("message", ""))
                .andExpect(status().isOk());  // re-renders form, not a redirect

        assertEquals(countBefore, notificationService.countUnreadNotifications(sellerEmail));
    }
}
