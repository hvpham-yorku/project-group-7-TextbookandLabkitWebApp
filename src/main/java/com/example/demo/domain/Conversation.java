package com.example.demo.domain;

import java.util.List;

public class Conversation {

    private final String participantA;
    private final String participantB;
    private final List<Message> messages;

    public Conversation(String participantA, String participantB, List<Message> messages) {
        this.participantA = participantA;
        this.participantB = participantB;
        this.messages = messages;
    }

    public List<Message> getMessages() { return messages; }

    // Returns the email of the other participant (not the current user).
    // Called directly from Thymeleaf as convo.getOtherParticipant(currentUserEmail).
    public String getOtherParticipant(String currentUserEmail) {
        if (currentUserEmail.equalsIgnoreCase(participantA)) return participantB;
        return participantA;
    }

    // Returns the most recent message, or null if the conversation is empty.
    // Accessed in Thymeleaf as convo.lastMessage.
    public Message getLastMessage() {
        if (messages == null || messages.isEmpty()) return null;
        return messages.get(messages.size() - 1);
    }

    // Returns the number of unread messages for the given receiver.
    // Called from Thymeleaf as convo.getUnreadCount(currentUserEmail).
    public long getUnreadCount(String receiverEmail) {
        if (messages == null) return 0;
        return messages.stream()
                .filter(m -> m.getReceiverEmail() != null
                        && m.getReceiverEmail().equalsIgnoreCase(receiverEmail)
                        && !m.isRead())
                .count();
    }
}
