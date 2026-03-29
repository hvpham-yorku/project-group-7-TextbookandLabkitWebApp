package com.example.demo.repository;
 
import com.example.demo.domain.Message;
import java.util.List;
 
public interface MessageRepository {
 
    Message save(Message message);
 
    List<Message> findByListingId(Long listingId);
 
    List<Message> findBySenderAndReceiver(Long senderId, Long receiverId);
 
    void markAsRead(Long receiverId, Long senderId);
 
    long countUnread(Long receiverId);
}
 
