package com.example.demo.repository;

import com.example.demo.domain.Message;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("stub")
public class MessageRepositoryStub implements MessageRepository {

    private final Map<Long, Message> messages = new HashMap<>();
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
    public List<Message> findByListingId(Long listingId) {
        return messages.values().stream()
                .filter(m -> m.getListingId() != null && m.getListingId().equals(listingId))
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findBySenderAndReceiver(Long senderId, Long receiverId) {
        return messages.values().stream()
                .filter(m ->
                    (m.getSenderId().equals(senderId) && m.getReceiverId().equals(receiverId)) ||
                    (m.getSenderId().equals(receiverId) && m.getReceiverId().equals(senderId))
                )
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long receiverId, Long senderId) {
        messages.values().stream()
                .filter(m -> m.getReceiverId().equals(receiverId)
                        && m.getSenderId().equals(senderId)
                        && !m.isRead())
                .forEach(Message::markAsRead);
    }

    @Override
    public long countUnread(Long receiverId) {
        return messages.values().stream()
                .filter(m -> m.getReceiverId().equals(receiverId) && !m.isRead())
                .count();
    }
}
