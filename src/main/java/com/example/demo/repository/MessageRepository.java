package com.example.demo.repository;

import com.example.demo.domain.Message;
import java.util.List;

public interface MessageRepository {

    Message save(Message message);

    List<Message> findByListingId(Long listingId);

}
