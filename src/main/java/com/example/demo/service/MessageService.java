package com.example.demo.service;

import com.example.demo.domain.Message;
import com.example.demo.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public Message sendMessage(Message message) {
        return repository.save(message);
    }

    public List<Message> getConversation(Long listingId) {
        return repository.findByListingId(listingId);
    }
}
