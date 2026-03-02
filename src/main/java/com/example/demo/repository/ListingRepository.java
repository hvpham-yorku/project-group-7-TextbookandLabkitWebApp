package com.example.demo.repository;

import com.example.demo.domain.Listing;

import java.math.BigDecimal;
import java.util.List;

public interface ListingRepository {

    List<Listing> findBySellerEmail(String sellerEmail);

    Listing findById(long id);

    void save(Listing listing);

    Listing create(String sellerEmail, String title, String description, BigDecimal price);

    boolean deleteById(long id);
}