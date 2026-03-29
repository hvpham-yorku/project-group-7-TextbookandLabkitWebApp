package com.example.demo.service;

import com.example.demo.domain.Conversation;
import com.example.demo.domain.Message;
import com.example.demo.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * Send a direct message from senderEmail to receiverEmail.
     * Validates content and checks block status using emails.
     */
    public Message sendMessage(String senderEmail, String receiverEmail, String content) {
        if (senderEmail == null || receiverEmail == null) {
            throw new IllegalArgumentException("Sender and receiver are required.");
        }
        if (senderEmail.equalsIgnoreCase(receiverEmail)) {
            throw new IllegalArgumentException("You cannot send a message to yourself.");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty.");
        }
        if (content.trim().length() > 1000) {
            throw new IllegalArgumentException("Message cannot exceed 1000 characters.");
        }
        if (blockService.isBlocked(senderEmail, receiverEmail)) {
            throw new IllegalStateException("Cannot send message. This user is blocked.");
        }

        Message message = new Message();
        message.setSenderEmail(senderEmail);
        message.setReceiverEmail(receiverEmail);
        message.setContent(content.trim());
        return repository.save(message);
    }

    /**
     * Return all conversations for the given user, one per unique partner.
     */
    public List<Conversation> getConversations(String email) {
        List<String> partners = repository.findConversationPartners(email);
        List<Conversation> conversations = new ArrayList<>();
        for (String partner : partners) {
            List<Message> msgs = repository.findConversation(email, partner);
            conversations.add(new Conversation(email, partner, msgs));
        }
        return conversations;
    }

    /**
     * Return the conversation between currentEmail and otherEmail,
     * marking all incoming messages as read.
     */
    public Conversation getConversationWith(String currentEmail, String otherEmail) {
        repository.markAsRead(currentEmail, otherEmail);
        List<Message> msgs = repository.findConversation(currentEmail, otherEmail);
        return new Conversation(currentEmail, otherEmail, msgs);
    }

    /**
     * Count unread messages for the given user (used for the header badge).
     */
    public long countUnread(String email) {
        return repository.countUnread(email);
    }

    /**
     * Return all general chat room messages ordered by sent time ascending.
     */
    public List<Message> getGeneralChatMessages() {
        return repository.findAllGeneralChatMessages();
    }

    /**
     * Post a message into the general chat room from the given sender.
     * Uses the sentinel receiver value 'GENERAL_CHAT' to distinguish
     * general chat messages from direct messages in the same table.
     */
    public Message sendGeneralChatMessage(String senderEmail, String content) {
        if (senderEmail == null) {
            throw new IllegalArgumentException("Sender is required.");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty.");
        }
        if (content.trim().length() > 1000) {
            throw new IllegalArgumentException("Message cannot exceed 1000 characters.");
        }
        Message message = new Message();
        message.setSenderEmail(senderEmail);
        message.setReceiverEmail("GENERAL_CHAT");
        message.setContent(content.trim());
        return repository.save(message);
    }
}
