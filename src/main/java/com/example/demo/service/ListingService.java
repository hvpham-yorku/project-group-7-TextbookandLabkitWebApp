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

```
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

    Listing listing = listingRepository.findById(listingId);
    if (listing == null) return false;

    if (!listing.getSellerEmail().equalsIgnoreCase(sellerEmail)) return false;

    listing.setStatus(newStatus);
    listingRepository.save(listing);

    return true;
}

// ===============================
// KAN-15 Filter Listings
// ===============================

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

// ===============================
// KAN-17 Sort Listings
// ===============================

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

// ===============================
// Browse All Listings
// ===============================

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
