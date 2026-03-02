package com.example.demo.repository;

import com.example.demo.domain.Listing;
import com.example.demo.domain.ListingStatus;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class StubListingRepository implements ListingRepository {

    private final List<Listing> listings = new ArrayList<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    public StubListingRepository() {
        // Sample data (seller emails match the stub users)
        listings.add(new Listing(idSeq.getAndIncrement(), "abc123@my.yorku.ca",
                "EECS 2311 Textbook (Used)", "Good condition, some highlights.", new BigDecimal("40.00"), ListingStatus.AVAILABLE));

        listings.add(new Listing(idSeq.getAndIncrement(), "abc123@my.yorku.ca",
                "Lab Kit - Digital Multimeter", "Works perfectly, includes leads.", new BigDecimal("25.00"), ListingStatus.AVAILABLE));

        listings.add(new Listing(idSeq.getAndIncrement(), "student1@my.yorku.ca",
                "ENG 1101 Notes", "Printed notes + past tests.", new BigDecimal("10.00"), ListingStatus.UNAVAILABLE));
    }

    @Override
    public List<Listing> findBySellerEmail(String sellerEmail) {
        List<Listing> result = new ArrayList<>();
        for (Listing l : listings) {
            if (l.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                result.add(l);
            }
        }
        return result;
    }

    @Override
    public Listing findById(long id) {
        for (Listing l : listings) {
            if (l.getId() == id) return l;
        }
        return null;
    }

    @Override
    public Listing create(String sellerEmail, String title, String description, BigDecimal price) {
        Listing listing = new Listing(
                idSeq.getAndIncrement(),
                sellerEmail,
                title.trim(),
                description.trim(),
                price,
                ListingStatus.AVAILABLE
        );
        listings.add(listing);
        return listing;
    }

    @Override
    public boolean deleteById(long id) {
        Listing toRemove = findById(id);
        if (toRemove == null) return false;
        listings.remove(toRemove);
        return true;
    }

    @Override
    public void save(Listing listing) {
        // For stub: if exists, update; else add.
        Listing existing = findById(listing.getId());
        if (existing == null) {
            listings.add(listing);
            return;
        }
        existing.setTitle(listing.getTitle());
        existing.setDescription(listing.getDescription());
        existing.setPrice(listing.getPrice());
        existing.setStatus(listing.getStatus());
    }
}