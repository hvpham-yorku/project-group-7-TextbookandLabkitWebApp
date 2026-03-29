package com.example.demo.repository;

import com.example.demo.domain.Message;
import java.util.List;

public interface MessageRepository {

    // Persist a new message and return it with its generated id.
    Message save(Message message);

    // Return all messages exchanged between two users, ordered oldest-first.
    List<Message> findConversation(String emailA, String emailB);

    // Return the distinct list of emails that the given user has exchanged messages with.
    List<String> findConversationPartners(String email);

    // Mark all unread messages sent by senderEmail to receiverEmail as read.
    void markAsRead(String receiverEmail, String senderEmail);

    // Count unread messages where receiverEmail is the recipient.
    long countUnread(String receiverEmail);

    // Return all general chat room messages ordered oldest-first.
    // General chat messages are stored with receiver_email = 'GENERAL_CHAT'.
    List<Message> findAllGeneralChatMessages();
}
