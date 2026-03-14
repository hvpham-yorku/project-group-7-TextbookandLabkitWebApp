package com.example.demo.repository;

import com.example.demo.domain.Listing;

import java.math.BigDecimal;
import java.util.List;

public interface ListingRepository {

    Listing create(String sellerEmail, String title, String description, BigDecimal price);

    Listing findById(long id);

    boolean deleteById(long id);

    void save(Listing listing);

    List<Listing> findBySellerEmail(String sellerEmail);

    void updateSellerEmail(String oldEmail, String newEmail);

    // Browse all listings from all sellers
    List<Listing> findAll();
}
