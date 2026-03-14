package com.example.demo.repository;

import com.example.demo.domain.Listing;

import java.math.BigDecimal;
import java.util.List;

public interface ListingRepository {

    List<Listing> findAll();

    List<Listing> findBySellerEmail(String sellerEmail);

    Listing findById(long id);

    void save(Listing listing);

    Listing create(String sellerEmail, String title, String description, BigDecimal price,
               String courseCode, String semester, String materialType,
               String condition, String exchangeType,
               String isbn, BigDecimal bookstorePrice);

    boolean deleteById(long id);

    void updateSellerEmail(String oldEmail, String newEmail);
}