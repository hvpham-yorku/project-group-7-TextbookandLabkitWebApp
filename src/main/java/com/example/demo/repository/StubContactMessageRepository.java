package com.example.demo.repository;

import com.example.demo.domain.ContactMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("stub")
public class StubContactMessageRepository implements ContactMessageRepository {

    private final List<ContactMessage> messages = new ArrayList<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    @Override
    public ContactMessage save(ContactMessage message) {
        message.setId(idSeq.getAndIncrement());
        messages.add(message);
        return message;
    }

    @Override
    public List<ContactMessage> findBySellerEmail(String sellerEmail) {
        List<ContactMessage> result = new ArrayList<>();
        for (ContactMessage m : messages) {
            if (m.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                result.add(m);
            }
        }
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
        return result;
    }
}
