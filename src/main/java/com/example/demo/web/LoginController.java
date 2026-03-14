package com.example.demo.web;

import com.example.demo.domain.Message;
import com.example.demo.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        return service.sendMessage(message);
    }

    @GetMapping("/listing/{listingId}")
    public List<Message> getConversation(@PathVariable Long listingId) {
        return service.getConversation(listingId);
    }
}
