package com.example.demo.repository;

import com.example.demo.domain.ContactMessage;

import java.util.List;

public interface ContactMessageRepository {

    ContactMessage save(ContactMessage message);
    ContactMessage findById(long id);
    List<ContactMessage> findBySellerEmail(String sellerEmail);
    List<ContactMessage> findBySenderEmail(String senderEmail);
}
