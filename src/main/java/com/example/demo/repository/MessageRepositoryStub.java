package com.example.demo.repository;

import com.example.demo.domain.Message;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("stub")
public class MessageRepositoryStub implements MessageRepository {

    private final Map<Long, Message> messages = new LinkedHashMap<>();
    private long idCounter = 1;

    @Override
    public Message save(Message message) {
        if (message.getId() == null) {
            message.setId(idCounter++);
        }
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public List<Message> findConversation(String emailA, String emailB) {
        return messages.values().stream()
                .filter(m ->
                        (m.getSenderEmail().equalsIgnoreCase(emailA) && m.getReceiverEmail().equalsIgnoreCase(emailB)) ||
                        (m.getSenderEmail().equalsIgnoreCase(emailB) && m.getReceiverEmail().equalsIgnoreCase(emailA))
                )
                .sorted(Comparator.comparing(Message::getSentAt))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findConversationPartners(String email) {
        Set<String> partners = new LinkedHashSet<>();
        for (Message m : messages.values()) {
            if (m.getSenderEmail().equalsIgnoreCase(email)) {
                partners.add(m.getReceiverEmail());
            } else if (m.getReceiverEmail().equalsIgnoreCase(email)) {
                partners.add(m.getSenderEmail());
            }
        }
        return new ArrayList<>(partners);
    }

    @Override
    public void markAsRead(String receiverEmail, String senderEmail) {
        messages.values().stream()
                .filter(m -> m.getReceiverEmail().equalsIgnoreCase(receiverEmail)
                        && m.getSenderEmail().equalsIgnoreCase(senderEmail)
                        && !m.isRead())
                .forEach(Message::markAsRead);
    }

    @Override
    public long countUnread(String receiverEmail) {
        return messages.values().stream()
                .filter(m -> m.getReceiverEmail().equalsIgnoreCase(receiverEmail) && !m.isRead())
                .count();
    }

    @Override
    public List<Message> findAllGeneralChatMessages() {
        return messages.values().stream()
                .filter(m -> "GENERAL_CHAT".equals(m.getReceiverEmail()))
                .sorted(Comparator.comparing(Message::getSentAt))
                .collect(Collectors.toList());
    }
}
