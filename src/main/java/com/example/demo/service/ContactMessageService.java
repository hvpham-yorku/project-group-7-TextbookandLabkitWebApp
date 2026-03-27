package com.example.demo.service;

import com.example.demo.domain.ContactMessage;
import com.example.demo.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    /**
     * Validates and saves a new contact message.
     * Returns the saved message (with generated id), or null if inputs are invalid.
     */
    public ContactMessage sendMessage(long listingId,
                                      String senderEmail,
                                      String sellerEmail,
                                      String subject,
                                      String message) {

        if (senderEmail == null || senderEmail.isBlank()) return null;
        if (sellerEmail == null || sellerEmail.isBlank()) return null;
        if (subject == null || subject.isBlank()) return null;
        if (message == null || message.isBlank()) return null;
        if (senderEmail.equalsIgnoreCase(sellerEmail)) return null;

        ContactMessage cm = new ContactMessage();
        cm.setListingId(listingId);
        cm.setSenderEmail(senderEmail.trim());
        cm.setSellerEmail(sellerEmail.trim());
        cm.setSubject(subject.trim());
        cm.setMessage(message.trim());
        cm.setCreatedAt(LocalDateTime.now());

        return contactMessageRepository.save(cm);
    }

    /**
     * Returns all messages received by a seller, ordered newest first.
     * Used for the seller inbox (KAN-94).
     */
    public List<ContactMessage> getMessagesForSeller(String sellerEmail) {
        if (sellerEmail == null || sellerEmail.isBlank()) return List.of();
        return contactMessageRepository.findBySellerEmail(sellerEmail);
    }

    /**
     * Returns all messages sent by a buyer.
     * Available for a sent-messages view if needed later.
     */
    public List<ContactMessage> getMessagesBySender(String senderEmail) {
        if (senderEmail == null || senderEmail.isBlank()) return List.of();
        return contactMessageRepository.findBySenderEmail(senderEmail);
    }
}
