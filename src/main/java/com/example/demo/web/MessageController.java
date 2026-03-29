package com.example.demo.web;

import com.example.demo.domain.Message;
import com.example.demo.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    /**
     * Send a message.
     * POST /messages
     */
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        try {
            Message saved = service.sendMessage(message);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get all messages for a listing.
     * GET /messages/listing/{listingId}
     */
    @GetMapping("/listing/{listingId}")
    public ResponseEntity<List<Message>> getConversation(@PathVariable Long listingId) {
        return ResponseEntity.ok(service.getConversation(listingId));
    }

    /**
     * Get direct conversation between two users.
     * GET /messages/direct?senderId=1&receiverId=2
     */
    @GetMapping("/direct")
    public ResponseEntity<List<Message>> getDirectConversation(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        return ResponseEntity.ok(service.getDirectConversation(senderId, receiverId));
    }

    /**
     * Get unread message count for a user.
     * GET /messages/unread/{receiverId}
     */
    @GetMapping("/unread/{receiverId}")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable Long receiverId) {
        return ResponseEntity.ok(Map.of("unreadCount", service.getUnreadCount(receiverId)));
    }
}
