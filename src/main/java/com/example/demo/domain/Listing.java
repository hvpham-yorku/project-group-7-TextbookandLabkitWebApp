package com.example.demo.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A simple domain object for Iteration 0/1 (stub persistence).
 * In a real app, this would likely be a JPA @Entity.
 */
public class Listing {

    private final long id;
    private final String sellerEmail;

    private String title;
    private String description;
    private BigDecimal price;
    private ListingStatus status;

    public Listing(long id,
                   String sellerEmail,
                   String title,
                   String description,
                   BigDecimal price,
                   ListingStatus status) {

        this.id = id;
        this.sellerEmail = sellerEmail;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status == null ? ListingStatus.AVAILABLE : status;
    }

    public long getId() {
        return id;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ListingStatus getStatus() {
        return status;
    }

    public void setStatus(ListingStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Listing listing)) return false;
        return id == listing.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}