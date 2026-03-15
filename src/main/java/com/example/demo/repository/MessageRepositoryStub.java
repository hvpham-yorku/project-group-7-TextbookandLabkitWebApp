package com.example.demo.repository;

import com.example.demo.domain.Message;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MessageRepositoryStub implements MessageRepository {

    private Map<Long, Message> messages = new HashMap<>();
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

        List<Message> result = new ArrayList<>();

        for (Message m : messages.values()) {
            if (m.getListingId().equals(listingId)) {
                result.add(m);
            }
        }

        return result;
    }
}
