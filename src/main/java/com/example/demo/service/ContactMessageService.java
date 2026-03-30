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

        if (!isValidField(senderEmail)) return null;
        if (!isValidField(sellerEmail)) return null;
        if (!isValidField(subject)) return null;
        if (!isValidField(message)) return null;
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
        if (!isValidField(sellerEmail)) return List.of();
        return contactMessageRepository.findBySellerEmail(sellerEmail);
    }

    public ContactMessage findById(long id) {
        if (id <= 0) return null;
        return contactMessageRepository.findById(id);
    }

    /**
     * Returns all messages sent by a buyer.
     * Available for a sent-messages view if needed later.
     */
    public List<ContactMessage> getMessagesBySender(String senderEmail) {
        if (!isValidField(senderEmail)) return List.of();
        return contactMessageRepository.findBySenderEmail(senderEmail);
    }

    /**
     * Reusable validation helper to check if a string field is present and non-blank.
     * Extracted to eliminate repeated null/blank checks across service methods.
     */
    private boolean isValidField(String value) {
        return value != null && !value.isBlank();
    }
}
