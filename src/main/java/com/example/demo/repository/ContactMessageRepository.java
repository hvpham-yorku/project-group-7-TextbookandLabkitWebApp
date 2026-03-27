package com.example.demo.repository;

import com.example.demo.domain.ContactMessage;

import java.util.List;

public interface ContactMessageRepository {

    // Persist a new contact message and return it with its generated id set.
    ContactMessage save(ContactMessage message);

    // Find all messages sent to a seller (used for inbox).
    List<ContactMessage> findBySellerEmail(String sellerEmail);

    // Find all messages sent by a specific buyer (used for sent view).
    List<ContactMessage> findBySenderEmail(String senderEmail);
}
