package service;

import domain.Listing;
import org.junit.jupiter.api.Test;
import repository.StubListingRepository;

import static org.junit.jupiter.api.Assertions.*;

public class ListingServiceTest {

    @Test
    void editOwnListing() {
        StubListingRepository repo = new StubListingRepository();
        ListingService service = new ListingService(repo);

        Listing l = new Listing("1", "Book", "EECS2311", 50, "abc123@yorku.ca");
        repo.save(l);

        service.edit("abc123@yorku.ca", "1", "Updated", "EECS2311", 60);

        assertNotNull(repo.findById("1"));
    }
}
