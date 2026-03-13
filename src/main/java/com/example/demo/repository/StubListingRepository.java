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
        // Sample data — seller emails match the stub users
        listings.add(new Listing(idSeq.getAndIncrement(), "abc123@my.yorku.ca",
                "EECS 2311 Textbook (Used)",
                "Good condition, a few highlights in chapter 3. No missing pages.",
                new BigDecimal("40.00"),
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell",
                ListingStatus.AVAILABLE));

        listings.add(new Listing(idSeq.getAndIncrement(), "abc123@my.yorku.ca",
                "EECS 1021 Lab Kit - Breadboard & Components",
                "Full kit, everything included. Used for one semester only.",
                new BigDecimal("20.00"),
                "EECS 1021", "Fall 2024", "Lab Kit", "Good", "Sell",
                ListingStatus.AVAILABLE));

        listings.add(new Listing(idSeq.getAndIncrement(), "student1@my.yorku.ca",
                "MATH 1013 Printed Notes + Past Midterms",
                "Full set of handwritten notes and 3 past midterms with solutions.",
                new BigDecimal("10.00"),
                "MATH 1013", "Winter 2025", "Notes", "Good", "Sell",
                ListingStatus.AVAILABLE));

        listings.add(new Listing(idSeq.getAndIncrement(), "student1@my.yorku.ca",
                "EECS 2030 Object-Oriented Programming Textbook",
                "Lightly used. Will trade for EECS 3311 textbook.",
                new BigDecimal("0.00"),
                "EECS 2030", "Fall 2024", "Textbook", "Fair", "Trade",
                ListingStatus.AVAILABLE));

        listings.add(new Listing(idSeq.getAndIncrement(), "student2@my.yorku.ca",
                "PHYS 1010 Lab Manual (Latest Edition)",
                "Brand new, never written in. Bought the wrong section.",
                new BigDecimal("15.00"),
                "PHYS 1010", "Winter 2025", "Lab Kit", "New", "Sell",
                ListingStatus.AVAILABLE));

        listings.add(new Listing(idSeq.getAndIncrement(), "student2@my.yorku.ca",
                "NATS 1840 Science and Technology Textbook",
                "Free to a good home. Some underlining but fully readable.",
                new BigDecimal("0.00"),
                "NATS 1840", "Fall 2024", "Textbook", "Fair", "Giveaway",
                ListingStatus.AVAILABLE));
    }

    @Override
    public List<Listing> findAll() {
        return new ArrayList<>(listings);
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
    public Listing create(String sellerEmail, String title, String description, BigDecimal price,
                          String courseCode, String semester, String materialType,
                          String condition, String exchangeType) {
        Listing listing = new Listing(
                idSeq.getAndIncrement(),
                sellerEmail,
                title.trim(),
                description.trim(),
                price,
                courseCode,
                semester,
                materialType,
                condition,
                exchangeType,
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
    public void updateSellerEmail(String oldEmail, String newEmail) {
        List<Listing> updated = new ArrayList<>();
        for (Listing l : listings) {
            if (l.getSellerEmail().equalsIgnoreCase(oldEmail)) {
                updated.add(new Listing(l.getId(), newEmail, l.getTitle(), l.getDescription(), l.getPrice(),
                        l.getCourseCode(), l.getSemester(), l.getMaterialType(),
                        l.getCondition(), l.getExchangeType(), l.getStatus()));
            } else {
                updated.add(l);
            }
        }
        listings.clear();
        listings.addAll(updated);
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
        existing.setCourseCode(listing.getCourseCode());
        existing.setSemester(listing.getSemester());
        existing.setMaterialType(listing.getMaterialType());
        existing.setCondition(listing.getCondition());
        existing.setExchangeType(listing.getExchangeType());
        existing.setStatus(listing.getStatus());
    }
}