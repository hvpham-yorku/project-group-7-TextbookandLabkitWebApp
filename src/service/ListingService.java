package service;

import domain.Listing;
import java.util.ArrayList;
import java.util.List;

public class ListingService {

    private final List<Listing> listings = new ArrayList<>();

    public void addListing(Listing listing) {
        listings.add(listing);
    }

    public boolean removeListing(String id) {
        return listings.removeIf(l -> l.getId().equals(id));
    }

    public boolean editListing(String id, String newTitle, String newCourseCode, int newPrice) {
        for (Listing l : listings) {
            if (l.getId().equals(id)) {
                l.setTitle(newTitle);
                l.setCourseCode(newCourseCode);
                l.setPrice(newPrice);
                return true;
            }
        }
        return false;
    }

    public List<Listing> getAllListings() {
        return listings;
    }
}
