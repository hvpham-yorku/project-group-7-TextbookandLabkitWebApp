package service;

import domain.Listing;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ListingServiceTest {

    @Test
    void addListing_shouldIncreaseList() {
        ListingService service = new ListingService();
        Listing listing = new Listing("1", "Java Book", "EECS2311", 50, "abc@yorku.ca");

        service.addListing(listing);

        assertEquals(1, service.getAllListings().size());
    }

    @Test
    void removeListing_shouldDeleteListing() {
        ListingService service = new ListingService();
        Listing listing = new Listing("1", "Java Book", "EECS2311", 50, "abc@yorku.ca");

        service.addListing(listing);
        boolean removed = service.removeListing("1");

        assertTrue(removed);
        assertEquals(0, service.getAllListings().size());
    }

    @Test
    void editListing_shouldUpdateFields() {
        ListingService service = new ListingService();
        Listing listing = new Listing("1", "Old Title", "EECS2311", 50, "abc@yorku.ca");

        service.addListing(listing);
        boolean edited = service.editListing("1", "New Title", "EECS2030", 60);

        assertTrue(edited);
        assertEquals("New Title", listing.getTitle());
        assertEquals("EECS2030", listing.getCourseCode());
        assertEquals(60, listing.getPrice());
    }
}
