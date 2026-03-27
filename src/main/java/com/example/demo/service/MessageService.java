package com.example.demo.service;

import com.example.demo.repository.MessageRepository; // your existing repo
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository; // your existing repo
    private final BlockService blockService;

    // ✅ Inject BlockService via constructor — Spring handles this automatically
    public MessageService(MessageRepository messageRepository, BlockService blockService) {
        this.messageRepository = messageRepository;
        this.blockService = blockService;
    }

    public void sendMessage(String senderEmail, String receiverEmail, String content) {

        // ✅ Block check — prevents blocked users from messaging each other
        if (blockService.isBlocked(senderEmail, receiverEmail)) {
            throw new IllegalStateException("Cannot send message. This user is blocked.");
        }

        // ... rest of your existing send message logic below
        // e.g. messageRepository.save(new Message(senderEmail, receiverEmail, content));
    }
}
