package com.example.demo.service;

import com.example.demo.domain.Listing;
import com.example.demo.domain.ListingStatus;
import com.example.demo.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingService {

    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<Listing> getListingsForSeller(String sellerEmail) {
        return listingRepository.findBySellerEmail(sellerEmail);
    }

    /**
     * User story: As a student(seller) I want to mark my listing as sold or unavailable
     * once it has been purchased or exchanged to a buyer.
     */
    public boolean updateStatus(long listingId, String sellerEmail, ListingStatus newStatus) {
        if (newStatus == null) return false;

        Listing listing = listingRepository.findById(listingId);
        if (listing == null) return false;

        // Seller can only update their own listing
        if (!listing.getSellerEmail().equalsIgnoreCase(sellerEmail)) return false;

        listing.setStatus(newStatus);
        listingRepository.save(listing);
        return true;
    }
}