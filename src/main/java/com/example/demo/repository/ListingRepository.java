package com.example.demo.service;

import com.example.demo.domain.Listing;
import com.example.demo.domain.ListingStatus;
import com.example.demo.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ListingService {

    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public Listing addListing(String sellerEmail, String title, String description, BigDecimal price) {
        if (sellerEmail == null || sellerEmail.isBlank()) return null;
        if (title == null || title.isBlank()) return null;
        if (description == null || description.isBlank()) return null;
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) return null;

        return listingRepository.create(sellerEmail, title, description, price);
    }

    public boolean deleteListing(long listingId, String sellerEmail) {
        if (sellerEmail == null || sellerEmail.isBlank()) return false;
        if (listingId <= 0) return false;

        Listing listing = listingRepository.findById(listingId);
        if (listing == null) return false;

        if (!listing.getSellerEmail().equalsIgnoreCase(sellerEmail)) return false;

        return listingRepository.deleteById(listingId);
    }

    public Listing findById(long listingId) {
        return listingRepository.findById(listingId);
    }

    public boolean updateListing(long listingId, String sellerEmail,
                                 String title, String description,
                                 BigDecimal price, ListingStatus status) {

        if (sellerEmail == null || sellerEmail.isBlank()) return false;
        if (listingId <= 0) return false;
        if (title == null || title.isBlank()) return false;
        if (description == null || description.isBlank()) return false;
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) return false;
        if (status == null) return false;

        Listing listing = listingRepository.findById(listingId);
        if (listing == null) return false;

        if (!listing.getSellerEmail().equalsIgnoreCase(sellerEmail)) return false;

        listing.setTitle(title.trim());
        listing.setDescription(description.trim());
        listing.setPrice(price);
        listing.setStatus(status);

        listingRepository.save(listing);
        return true;
    }

    public void updateSellerEmail(String oldEmail, String newEmail) {
        if (oldEmail == null || newEmail == null) return;
        if (oldEmail.equalsIgnoreCase(newEmail)) return;

        listingRepository.updateSellerEmail(oldEmail, newEmail);
    }

    public List<Listing> getListingsForSeller(String sellerEmail) {
        return listingRepository.findBySellerEmail(sellerEmail);
    }

    public boolean updateStatus(long listingId, String sellerEmail, ListingStatus newStatus) {

        if (newStatus == null) return false;

        Listing listing = listingRepository.findById(listingId);
        if (listing == null) return false;

        if (!listing.getSellerEmail().equalsIgnoreCase(sellerEmail)) return false;

        listing.setStatus(newStatus);
        listingRepository.save(listing);

        return true;
    }

    /* =========================
       KAN-15 Filter Listings
       ========================= */

    public List<Listing> filterListingsBySeller(String sellerEmail) {

        if (sellerEmail == null || sellerEmail.isBlank()) {
            return List.of();
        }

        return listingRepository.findBySellerEmail(sellerEmail);
    }


    /* =========================
       KAN-17 Sort Listings
       ========================= */

    public List<Listing> sortListingsByPrice(String sellerEmail) {

        List<Listing> listings = listingRepository.findBySellerEmail(sellerEmail);

        listings.sort((a, b) -> a.getPrice().compareTo(b.getPrice()));

        return listings;
    }


    public List<Listing> sortListingsByTitle(String sellerEmail) {

        List<Listing> listings = listingRepository.findBySellerEmail(sellerEmail);

        listings.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));

        return listings;
    }

}
