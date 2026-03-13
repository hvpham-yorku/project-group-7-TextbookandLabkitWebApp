package com.example.demo.service;

import com.example.demo.domain.Listing;
import com.example.demo.domain.ListingStatus;
import com.example.demo.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ListingService {

    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public Listing addListing(String sellerEmail, String title, String description, BigDecimal price,
                          String courseCode, String semester, String materialType,
                          String condition, String exchangeType,
                          String isbn, BigDecimal bookstorePrice) {
        if (sellerEmail == null || sellerEmail.isBlank()) return null;
        if (title == null || title.isBlank()) return null;
        if (description == null || description.isBlank()) return null;
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) return null;

        if (bookstorePrice != null && bookstorePrice.compareTo(BigDecimal.ZERO) < 0) return null;

        return listingRepository.create(sellerEmail, title, description, price,
        courseCode, semester, materialType, condition, exchangeType,
        (isbn == null ? "" : isbn.trim()), bookstorePrice);
    }

    // Backward compatible overload (old code calls this)
public Listing addListing(String sellerEmail, String title, String description, BigDecimal price,
                          String courseCode, String semester, String materialType,
                          String condition, String exchangeType) {
    return addListing(sellerEmail, title, description, price,
            courseCode, semester, materialType, condition, exchangeType,
            "", null);
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
                                 BigDecimal price, ListingStatus status,
                                 String isbn, BigDecimal bookstorePrice) {
        if (sellerEmail == null || sellerEmail.isBlank()) return false;
        if (listingId <= 0) return false;
        if (title == null || title.isBlank()) return false;
        if (description == null || description.isBlank()) return false;
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) return false;
        if (status == null) return false;
        if (bookstorePrice != null && bookstorePrice.compareTo(BigDecimal.ZERO) < 0) return false;

        Listing listing = listingRepository.findById(listingId);
        if (listing == null) return false;
        if (!listing.getSellerEmail().equalsIgnoreCase(sellerEmail)) return false;

        listing.setTitle(title.trim());
        listing.setDescription(description.trim());
        listing.setPrice(price);
        listing.setStatus(status);
        listing.setIsbn(isbn == null ? "" : isbn.trim());
        listing.setBookstorePrice(bookstorePrice);
        listingRepository.save(listing);
        return true;
    }

// Backward compatible overload for updateListing (old code)
public boolean updateListing(long listingId, String sellerEmail,
                             String title, String description,
                             BigDecimal price, ListingStatus status) {
    return updateListing(listingId, sellerEmail, title, description, price, status, "", null);
}

    public void updateSellerEmail(String oldEmail, String newEmail) {
        if (oldEmail == null || newEmail == null) return;
        if (oldEmail.equalsIgnoreCase(newEmail)) return;
        listingRepository.updateSellerEmail(oldEmail, newEmail);
    }

    public List<Listing> getListingsForSeller(String sellerEmail) {
        return listingRepository.findBySellerEmail(sellerEmail);
    }

    // KAN-62
    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    /**
     * Browse page: keyword search + sorting.
     * - q matches title, description, course code, and ISBN
     * - sortBy controls ordering
     */
    public List<Listing> searchAndSort(String q, String sortBy) {
        List<Listing> listings = listingRepository.findAll();
        String needle = (q == null) ? "" : q.trim().toLowerCase(Locale.ROOT);

        return listings.stream()
                .filter(l -> needle.isEmpty() || matches(l, needle))
                .sorted(getComparator(sortBy))
                .collect(Collectors.toList());
    }

    private boolean matches(Listing l, String needle) {
        return safe(l.getTitle()).contains(needle)
                || safe(l.getDescription()).contains(needle)
                || safe(l.getCourseCode()).contains(needle)
                || safe(l.getIsbn()).contains(needle);
    }

    private String safe(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT);
    }

    private Comparator<Listing> getComparator(String sortBy) {
        String s = (sortBy == null) ? "" : sortBy;
        return switch (s) {
            case "priceAsc" -> Comparator.comparing(Listing::getPrice, Comparator.nullsLast(Comparator.naturalOrder()));
            case "priceDesc" -> Comparator.comparing(Listing::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed();
            case "courseAsc" -> Comparator.comparing(l -> safe(l.getCourseCode()));
            case "conditionAsc" -> Comparator.comparing(l -> safe(l.getCondition()));
            case "newest" -> Comparator.comparing(Listing::getDatePosted, Comparator.nullsLast(Comparator.naturalOrder())).reversed();
            default -> Comparator.comparing(Listing::getDatePosted, Comparator.nullsLast(Comparator.naturalOrder())).reversed();
        };
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
}