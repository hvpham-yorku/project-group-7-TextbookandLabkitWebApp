
package com.example.demo.service;

import com.example.demo.domain.Listing;
import com.example.demo.domain.ListingStatus;
import com.example.demo.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        if (sellerEmail == null || sellerEmail.isBlank()) return List.of();
        return listingRepository.findBySellerEmail(sellerEmail);
    }

    public List<Listing> filterListings(String keyword, ListingStatus status,
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        List<Listing> all = listingRepository.findAll();

        return all.stream()
                .filter(l -> status == null || l.getStatus() == status)
                .filter(l -> minPrice == null || l.getPrice().compareTo(minPrice) >= 0)
                .filter(l -> maxPrice == null || l.getPrice().compareTo(maxPrice) <= 0)
                .filter(l -> keyword == null || keyword.isBlank()
                        || l.getTitle().toLowerCase().contains(keyword.toLowerCase())
                        || l.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    /* =========================
       KAN-17 Sort Listings
       ========================= */

    public List<Listing> sortListingsByPrice(String sellerEmail) {
        List<Listing> listings = listingRepository.findBySellerEmail(sellerEmail);
        listings.sort(Comparator.comparing(Listing::getPrice));
        return listings;
    }

    public List<Listing> sortListingsByPriceDesc(String sellerEmail) {
        List<Listing> listings = listingRepository.findBySellerEmail(sellerEmail);
        listings.sort(Comparator.comparing(Listing::getPrice).reversed());
        return listings;
    }

    public List<Listing> sortListingsByTitle(String sellerEmail) {
        List<Listing> listings = listingRepository.findBySellerEmail(sellerEmail);
        listings.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        return listings;
    }

    public List<Listing> sortListingsByTitleDesc(String sellerEmail) {
        List<Listing> listings = listingRepository.findBySellerEmail(sellerEmail);
        listings.sort((a, b) -> b.getTitle().compareToIgnoreCase(a.getTitle()));
        return listings;
    }

    /* =========================
       Browse All Listings
       ========================= */

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public List<Listing> getAllListingsSortedByPrice(boolean ascending) {
        List<Listing> all = listingRepository.findAll();
        if (ascending) {
            all.sort(Comparator.comparing(Listing::getPrice));
        } else {
            all.sort(Comparator.comparing(Listing::getPrice).reversed());
        }
        return all;
    }

    public List<Listing> getAllListingsSortedByTitle(boolean ascending) {
        List<Listing> all = listingRepository.findAll();
        if (ascending) {
            all.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        } else {
            all.sort((a, b) -> b.getTitle().compareToIgnoreCase(a.getTitle()));
        }
        return all;
    }
}
