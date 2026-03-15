package com.example.demo.repository;

import com.example.demo.domain.Listing;

import java.math.BigDecimal;
import java.util.List;

public interface ListingRepository {

    // Create listing (simple version)
    Listing create(String sellerEmail, String title, String description, BigDecimal price);

    // Create listing (full version used in main)
    Listing create(String sellerEmail, String title, String description, BigDecimal price,
                   String courseCode, String semester, String materialType,
                   String condition, String exchangeType,
                   String isbn, BigDecimal bookstorePrice);

    // Save updates to an existing listing
    void save(Listing listing);

    // Find listing by ID
    Listing findById(long id);

    // Delete listing
    boolean deleteById(long id);

    // Find listings for a specific seller
    List<Listing> findBySellerEmail(String sellerEmail);

    // Update seller email for all listings
    void updateSellerEmail(String oldEmail, String newEmail);

    // Browse all listings
    List<Listing> findAll();
}