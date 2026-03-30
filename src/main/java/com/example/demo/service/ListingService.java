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

    /**
     * Reusable validation helper to check if a string field is present and non-blank.
     * Extracted to eliminate repeated null/blank checks across service methods.
     */
    private boolean isValidField(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * Looks up a listing and verifies the given seller owns it.
     * Returns null if any check fails (invalid args, not found, wrong owner).
     * Used by deleteListing, updateListing, and updateStatus to avoid duplication.
     */
    private Listing findOwnedListing(long listingId, String sellerEmail) {
        if (!isValidField(sellerEmail)) return null;
        if (listingId <= 0) return null;

        Listing listing = listingRepository.findById(listingId);
        if (listing == null) return null;

        if (!listing.getSellerEmail().equalsIgnoreCase(sellerEmail)) return null;

        return listing;
    }

    public Listing addListing(String sellerEmail, String title, String description, BigDecimal price,
                              String courseCode, String semester, String materialType,
                              String condition, String exchangeType,
                              String isbn, BigDecimal bookstorePrice) {

        if (!isValidField(sellerEmail)) return null;
        if (!isValidField(title)) return null;
        if (!isValidField(description)) return null;
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) return null;
        if (bookstorePrice != null && bookstorePrice.compareTo(BigDecimal.ZERO) < 0) return null;

        return listingRepository.create(
                sellerEmail, title, description, price,
                courseCode, semester, materialType,
                condition, exchangeType,
                (isbn == null ? "" : isbn.trim()),
                bookstorePrice
        );
    }

    // Backward compatible overload
    public Listing addListing(String sellerEmail, String title, String description, BigDecimal price,
                              String courseCode, String semester, String materialType,
                              String condition, String exchangeType) {

        return addListing(
                sellerEmail, title, description, price,
                courseCode, semester, materialType,
                condition, exchangeType,
                "", null
        );
    }

    public boolean deleteListing(long listingId, String sellerEmail) {
        Listing listing = findOwnedListing(listingId, sellerEmail);
        if (listing == null) return false;

        return listingRepository.deleteById(listingId);
    }

    public Listing findById(long listingId) {
        return listingRepository.findById(listingId);
    }

    public boolean updateListing(long listingId, String sellerEmail,
                                 String title, String description,
                                 BigDecimal price, ListingStatus status,
                                 String isbn, BigDecimal bookstorePrice) {

        if (!isValidField(title)) return false;
        if (!isValidField(description)) return false;
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) return false;
        if (status == null) return false;
        if (bookstorePrice != null && bookstorePrice.compareTo(BigDecimal.ZERO) < 0) return false;

        Listing listing = findOwnedListing(listingId, sellerEmail);
        if (listing == null) return false;

        listing.setTitle(title.trim());
        listing.setDescription(description.trim());
        listing.setPrice(price);
        listing.setStatus(status);
        listing.setIsbn(isbn == null ? "" : isbn.trim());
        listing.setBookstorePrice(bookstorePrice);

        listingRepository.save(listing);
        return true;
    }

    // Backward compatible overload
    public boolean updateListing(long listingId, String sellerEmail,
                                 String title, String description,
                                 BigDecimal price, ListingStatus status) {

        return updateListing(
                listingId, sellerEmail,
                title, description,
                price, status,
                "", null
        );
    }

    public void updateImagePath(long listingId, String imagePath) {
        Listing listing = listingRepository.findById(listingId);
        if (listing == null) return;
        listing.setImagePath(imagePath);
        listingRepository.save(listing);
    }

    public void updateSellerEmail(String oldEmail, String newEmail) {

        if (oldEmail == null || newEmail == null) return;
        if (oldEmail.equalsIgnoreCase(newEmail)) return;

        listingRepository.updateSellerEmail(oldEmail, newEmail);
    }

    public List<Listing> getListingsForSeller(String sellerEmail) {
        return listingRepository.findBySellerEmail(sellerEmail);
    }

    // ===============================
    // Browse Listings (Search + Sort)
    // ===============================

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

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

            case "priceAsc" ->
                    Comparator.comparing(Listing::getPrice, Comparator.nullsLast(Comparator.naturalOrder()));

            case "priceDesc" ->
                    Comparator.comparing(Listing::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed();

            case "courseAsc" ->
                    Comparator.comparing(l -> safe(l.getCourseCode()));

            case "conditionAsc" ->
                    Comparator.comparing(l -> safe(l.getCondition()));

            case "newest" ->
                    Comparator.comparing(Listing::getDatePosted,
                            Comparator.nullsLast(Comparator.naturalOrder())).reversed();

            default ->
                    Comparator.comparing(Listing::getDatePosted,
                            Comparator.nullsLast(Comparator.naturalOrder())).reversed();
        };
    }

    public boolean updateStatus(long listingId, String sellerEmail, ListingStatus newStatus) {

        if (newStatus == null) return false;

        Listing listing = findOwnedListing(listingId, sellerEmail);
        if (listing == null) return false;

        listing.setStatus(newStatus);
        listingRepository.save(listing);

        return true;
    }

    // ===============================
    // KAN-15 Filter Listings
    // ===============================

    public List<Listing> filterListingsBySeller(String sellerEmail) {

        if (!isValidField(sellerEmail)) return List.of();

        return listingRepository.findBySellerEmail(sellerEmail);
    }

    public List<Listing> filterListings(String keyword, ListingStatus status,
                                        BigDecimal minPrice, BigDecimal maxPrice) {

        List<Listing> all = listingRepository.findAll();

        return all.stream()
                .filter(l -> status == null || l.getStatus() == status)
                .filter(l -> minPrice == null || l.getPrice().compareTo(minPrice) >= 0)
                .filter(l -> maxPrice == null || l.getPrice().compareTo(maxPrice) <= 0)
                .filter(l -> matchesKeyword(l, keyword))
                .collect(Collectors.toList());
    }

    private boolean matchesKeyword(Listing listing, String keyword) {
        if (keyword == null || keyword.isBlank()) return true;

        String needle = keyword.trim().toLowerCase(Locale.ROOT);
        return safe(listing.getTitle()).contains(needle)
                || safe(listing.getDescription()).contains(needle);
    }

    // ===============================
    // Sorting helpers
    // ===============================

    private static final Comparator<Listing> PRICE_ASC  = Comparator.comparing(Listing::getPrice);
    private static final Comparator<Listing> PRICE_DESC = PRICE_ASC.reversed();
    private static final Comparator<Listing> TITLE_ASC  = (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle());
    private static final Comparator<Listing> TITLE_DESC = TITLE_ASC.reversed();

    private List<Listing> sortedBy(List<Listing> listings, Comparator<Listing> comparator) {
        listings.sort(comparator);
        return listings;
    }

    // ===============================
    // KAN-17 Sort Listings
    // ===============================

    public List<Listing> sortListingsByPrice(String sellerEmail) {
        return sortedBy(listingRepository.findBySellerEmail(sellerEmail), PRICE_ASC);
    }

    public List<Listing> sortListingsByPriceDesc(String sellerEmail) {
        return sortedBy(listingRepository.findBySellerEmail(sellerEmail), PRICE_DESC);
    }

    public List<Listing> sortListingsByTitle(String sellerEmail) {
        return sortedBy(listingRepository.findBySellerEmail(sellerEmail), TITLE_ASC);
    }

    public List<Listing> sortListingsByTitleDesc(String sellerEmail) {
        return sortedBy(listingRepository.findBySellerEmail(sellerEmail), TITLE_DESC);
    }

    // ===============================
    // Browse All Listings
    // ===============================

    public List<Listing> getAllListingsSortedByPrice(boolean ascending) {
        return sortedBy(listingRepository.findAll(), ascending ? PRICE_ASC : PRICE_DESC);
    }

    public List<Listing> getAllListingsSortedByTitle(boolean ascending) {
        return sortedBy(listingRepository.findAll(), ascending ? TITLE_ASC : TITLE_DESC);
    }
}
