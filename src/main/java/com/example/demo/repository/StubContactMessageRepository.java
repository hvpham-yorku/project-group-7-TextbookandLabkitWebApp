package com.example.demo.repository;

import com.example.demo.domain.ContactMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("stub")
public class StubContactMessageRepository implements ContactMessageRepository {

    private final List<ContactMessage> messages = new ArrayList<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    public StubContactMessageRepository() {
        seed(1L, "student1@my.yorku.ca", "abc123@my.yorku.ca",
                "Interested in your EECS 2311 book",
                "Hey, is this still available? I can meet on campus this week.",
                LocalDateTime.now().minusHours(6));
        seed(2L, "saif0@my.yorku.ca", "abc123@my.yorku.ca",
                "Question about the EECS 1021 lab kit",
                "Does the kit include all components needed for labs?",
                LocalDateTime.now().minusHours(3));
    }

    private void seed(long listingId, String senderEmail, String sellerEmail,
                      String subject, String message, LocalDateTime createdAt) {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setId(idSeq.getAndIncrement());
        contactMessage.setListingId(listingId);
        contactMessage.setSenderEmail(senderEmail);
        contactMessage.setSellerEmail(sellerEmail);
        contactMessage.setSubject(subject);
        contactMessage.setMessage(message);
        contactMessage.setCreatedAt(createdAt);
        messages.add(contactMessage);
    }

    @Override
    public ContactMessage save(ContactMessage message) {
        message.setId(idSeq.getAndIncrement());
        messages.add(message);
        return message;
    }

    @Override
    public ContactMessage findById(long id) {
        for (ContactMessage message : messages) {
            if (message.getId() == id) return message;
        }
        return null;
    }

    @Override
    public List<ContactMessage> findBySellerEmail(String sellerEmail) {
        List<ContactMessage> result = new ArrayList<>();
        for (ContactMessage m : messages) {
            if (m.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                result.add(m);
            }
        }
        result.sort(Comparator.comparing(ContactMessage::getCreatedAt).reversed());
        return result;
    }

    @Override
    public List<ContactMessage> findBySenderEmail(String senderEmail) {
        List<ContactMessage> result = new ArrayList<>();
        for (ContactMessage m : messages) {
            if (m.getSenderEmail().equalsIgnoreCase(senderEmail)) {
                result.add(m);
            }
        }
        result.sort(Comparator.comparing(ContactMessage::getCreatedAt).reversed());
        return result;
    }
}
