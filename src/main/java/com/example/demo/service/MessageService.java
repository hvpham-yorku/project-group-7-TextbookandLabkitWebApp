package com.example.demo.service;

import com.example.demo.domain.Message;
import com.example.demo.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;
    private final BlockService blockService;

    public MessageService(MessageRepository repository, BlockService blockService) {
        this.repository = repository;
        this.blockService = blockService;
    }

    /**
     * Send a message. Validates content and checks block status.
     */
    public Message sendMessage(Message message) {

        if (message.getSenderId() == null || message.getReceiverId() == null) {
            throw new IllegalArgumentException("Sender and receiver are required.");
        }

        if (message.getSenderId().equals(message.getReceiverId())) {
            throw new IllegalArgumentException("You cannot send a message to yourself.");
        }

        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty.");
        }

        if (message.getContent().trim().length() > 1000) {
            throw new IllegalArgumentException("Message cannot exceed 1000 characters.");
        }

        // Block check — uses sender/receiver IDs converted to string for compatibility
        String senderKey = String.valueOf(message.getSenderId());
        String receiverKey = String.valueOf(message.getReceiverId());

        if (blockService.isBlocked(senderKey, receiverKey)) {
            throw new IllegalStateException("Cannot send message. This user is blocked.");
        }

        message.setContent(message.getContent().trim());
        return repository.save(message);
    }

    /**
     * Get all messages for a listing (original functionality kept).
     */
    public List<Message> getConversation(Long listingId) {
        return repository.findByListingId(listingId);
    }

    /**
     * Get direct conversation between two users.
     */
    public List<Message> getDirectConversation(Long senderId, Long receiverId) {
        repository.markAsRead(receiverId, senderId);
        return repository.findBySenderAndReceiver(senderId, receiverId);
    }

    /**
     * Get unread message count for a user (for badge/notification display).
     */
    public long getUnreadCount(Long receiverId) {
        return repository.countUnread(receiverId);
    }
}
