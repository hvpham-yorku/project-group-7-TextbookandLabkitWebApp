package com.example.demo.repository;

import com.example.demo.domain.Listing;

import java.util.List;

public interface ListingRepository {

    List<Listing> findBySellerEmail(String sellerEmail);

    Listing findById(long id);

    void save(Listing listing);
}