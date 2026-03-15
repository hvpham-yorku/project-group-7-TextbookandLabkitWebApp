package com.example.demo.domain;

public class SavedListing {

    private String userEmail;
    private long listingId;

    public SavedListing(String userEmail, long listingId) {
        this.userEmail = userEmail;
        this.listingId = listingId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public long getListingId() {
        return listingId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setListingId(long listingId) {
        this.listingId = listingId;
    }
}
