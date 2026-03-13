package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domain.Listing;
import com.example.demo.domain.ListingStatus;
import com.example.demo.repository.StubListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class ListingServiceTest {

    // Pre-seeded by StubListingRepository constructor:
    //   ID 1 → abc123@my.yorku.ca, "EECS 2311 Textbook (Used)",    AVAILABLE,   $40.00
    //   ID 2 → abc123@my.yorku.ca, "Lab Kit - Digital Multimeter", AVAILABLE,   $25.00
    //   ID 3 → student1@my.yorku.ca, "ENG 1101 Notes",             UNAVAILABLE, $10.00

    private static final String SELLER_ABC  = "abc123@my.yorku.ca";
    private static final String SELLER_STU1 = "student1@my.yorku.ca";
    private static final String SELLER_NEW  = "newuser@my.yorku.ca";

    private StubListingRepository repo;
    private ListingService service;

    @BeforeEach
    void setUp() {
        repo    = new StubListingRepository();
        service = new ListingService(repo);
    }

    // -------------------------------------------------------------------------
    // addListing
    // -------------------------------------------------------------------------

    @Test
    void addListing_validInputs_returnsListingWithCorrectFields() {
        Listing result = service.addListing(SELLER_NEW, "Calculus Textbook", "Like new", new BigDecimal("35.00"),
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell");

        assertNotNull(result);
        assertEquals(SELLER_NEW,           result.getSellerEmail());
        assertEquals("Calculus Textbook",  result.getTitle());
        assertEquals("Like new",           result.getDescription());
        assertEquals(new BigDecimal("35.00"), result.getPrice());
        assertEquals(ListingStatus.AVAILABLE, result.getStatus());
    }

    @Test
    void addListing_newListingAppearsInSellerList() {
        service.addListing(SELLER_NEW, "Physics Notes", "Complete notes", new BigDecimal("15.00"),
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell");

        List<Listing> listings = service.getListingsForSeller(SELLER_NEW);
        assertEquals(1, listings.size());
        assertEquals("Physics Notes", listings.get(0).getTitle());
    }

    @Test
    void addListing_zeroPriceIsValid_returnsListing() {
        Listing result = service.addListing(SELLER_NEW, "Free Notes", "Giving away", BigDecimal.ZERO,
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell");

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getPrice());
    }

    @Test
    void addListing_nullEmail_returnsNull() {
        Listing result = service.addListing(null, "Title", "Desc", new BigDecimal("10.00"),
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell");
        assertNull(result);
    }

    @Test
    void addListing_blankEmail_returnsNull() {
        Listing result = service.addListing("   ", "Title", "Desc", new BigDecimal("10.00"),
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell");
        assertNull(result);
    }

    @Test
    void addListing_nullTitle_returnsNull() {
        Listing result = service.addListing(SELLER_NEW, null, "Desc", new BigDecimal("10.00"),
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell");
        assertNull(result);
    }

    @Test
    void addListing_blankTitle_returnsNull() {
        Listing result = service.addListing(SELLER_NEW, "   ", "Desc", new BigDecimal("10.00"),
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell");
        assertNull(result);
    }

    @Test
    void addListing_nullDescription_returnsNull() {
        Listing result = service.addListing(SELLER_NEW, "Title", null, new BigDecimal("10.00"),
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell");
        assertNull(result);
    }

    @Test
    void addListing_blankDescription_returnsNull() {
        Listing result = service.addListing(SELLER_NEW, "Title", "", new BigDecimal("10.00"),
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell");
        assertNull(result);
    }

    @Test
    void addListing_nullPrice_returnsNull() {
        Listing result = service.addListing(SELLER_NEW, "Title", "Desc", null,
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell");
        assertNull(result);
    }

    @Test
    void addListing_negativePrice_returnsNull() {
        Listing result = service.addListing(SELLER_NEW, "Title", "Desc", new BigDecimal("-0.01"),
                "EECS 2311", "Winter 2025", "Textbook", "Good", "Sell");
        assertNull(result);
    }

    // -------------------------------------------------------------------------
    // getListingsForSeller
    // -------------------------------------------------------------------------

    @Test
    void getListingsForSeller_knownSeller_returnsOnlyTheirListings() {
        List<Listing> listings = service.getListingsForSeller(SELLER_ABC);

        assertEquals(2, listings.size());
        for (Listing l : listings) {
            assertEquals(SELLER_ABC, l.getSellerEmail());
        }
    }

    @Test
    void getListingsForSeller_emailCaseInsensitive_returnsListings() {
        List<Listing> listings = service.getListingsForSeller("ABC123@MY.YORKU.CA");
        assertEquals(2, listings.size());
    }

    @Test
    void getListingsForSeller_sellerWithNoListings_returnsEmptyList() {
        List<Listing> listings = service.getListingsForSeller(SELLER_NEW);
        assertNotNull(listings);
        assertTrue(listings.isEmpty());
    }

    // -------------------------------------------------------------------------
    // deleteListing
    // -------------------------------------------------------------------------

    @Test
    void deleteListing_ownerDeletesOwnListing_returnsTrue() {
        boolean result = service.deleteListing(1L, SELLER_ABC);
        assertTrue(result);
    }

    @Test
    void deleteListing_listingIsRemovedAfterDelete() {
        service.deleteListing(1L, SELLER_ABC);

        assertNull(service.findById(1L));
        assertEquals(1, service.getListingsForSeller(SELLER_ABC).size());
    }

    @Test
    void deleteListing_wrongOwner_returnsFalse() {
        // ID 1 belongs to SELLER_ABC; SELLER_STU1 should not be able to delete it
        boolean result = service.deleteListing(1L, SELLER_STU1);
        assertFalse(result);
    }

    @Test
    void deleteListing_wrongOwner_listingStillExists() {
        service.deleteListing(1L, SELLER_STU1);
        assertNotNull(service.findById(1L));
    }

    @Test
    void deleteListing_nonExistentId_returnsFalse() {
        boolean result = service.deleteListing(999L, SELLER_ABC);
        assertFalse(result);
    }

    @Test
    void deleteListing_idZero_returnsFalse() {
        boolean result = service.deleteListing(0L, SELLER_ABC);
        assertFalse(result);
    }

    @Test
    void deleteListing_negativeId_returnsFalse() {
        boolean result = service.deleteListing(-5L, SELLER_ABC);
        assertFalse(result);
    }

    @Test
    void deleteListing_blankEmail_returnsFalse() {
        boolean result = service.deleteListing(1L, "  ");
        assertFalse(result);
    }

    @Test
    void deleteListing_nullEmail_returnsFalse() {
        boolean result = service.deleteListing(1L, null);
        assertFalse(result);
    }

    // -------------------------------------------------------------------------
    // updateStatus
    // -------------------------------------------------------------------------

    @Test
    void updateStatus_ownerChangesStatus_returnsTrueAndStatusUpdated() {
        // ID 1 is AVAILABLE; change to SOLD
        boolean result = service.updateStatus(1L, SELLER_ABC, ListingStatus.SOLD);

        assertTrue(result);
        assertEquals(ListingStatus.SOLD, service.findById(1L).getStatus());
    }

    @Test
    void updateStatus_ownerChangesToUnavailable_statusUpdated() {
        boolean result = service.updateStatus(1L, SELLER_ABC, ListingStatus.UNAVAILABLE);

        assertTrue(result);
        assertEquals(ListingStatus.UNAVAILABLE, service.findById(1L).getStatus());
    }

    @Test
    void updateStatus_wrongOwner_returnsFalse() {
        // ID 1 belongs to SELLER_ABC; SELLER_STU1 should be rejected
        boolean result = service.updateStatus(1L, SELLER_STU1, ListingStatus.SOLD);
        assertFalse(result);
    }

    @Test
    void updateStatus_wrongOwner_statusUnchanged() {
        service.updateStatus(1L, SELLER_STU1, ListingStatus.SOLD);
        assertEquals(ListingStatus.AVAILABLE, service.findById(1L).getStatus());
    }

    @Test
    void updateStatus_nonExistentId_returnsFalse() {
        boolean result = service.updateStatus(999L, SELLER_ABC, ListingStatus.SOLD);
        assertFalse(result);
    }

    @Test
    void updateStatus_nullStatus_returnsFalse() {
        boolean result = service.updateStatus(1L, SELLER_ABC, null);
        assertFalse(result);
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    void findById_existingId_returnsCorrectListing() {
        Listing listing = service.findById(1L);

        assertNotNull(listing);
        assertEquals(1L, listing.getId());
        assertEquals(SELLER_ABC, listing.getSellerEmail());
    }

    @Test
    void findById_anotherExistingId_returnsCorrectListing() {
        Listing listing = service.findById(3L);

        assertNotNull(listing);
        assertEquals(3L, listing.getId());
        assertEquals(SELLER_STU1, listing.getSellerEmail());
    }

    @Test
    void findById_nonExistentId_returnsNull() {
        Listing listing = service.findById(999L);
        assertNull(listing);
    }

    // -------------------------------------------------------------------------
    // updateListing
    // -------------------------------------------------------------------------

    @Test
    void updateListing_validInputs_returnsTrueAndAllFieldsUpdated() {
        boolean result = service.updateListing(
                1L, SELLER_ABC,
                "Updated Textbook", "New description",
                new BigDecimal("55.00"), ListingStatus.UNAVAILABLE);

        assertTrue(result);

        Listing updated = service.findById(1L);
        assertEquals("Updated Textbook",       updated.getTitle());
        assertEquals("New description",         updated.getDescription());
        assertEquals(new BigDecimal("55.00"),   updated.getPrice());
        assertEquals(ListingStatus.UNAVAILABLE, updated.getStatus());
    }

    @Test
    void updateListing_titleIsTrimmed() {
        service.updateListing(1L, SELLER_ABC, "  Trimmed Title  ", "Desc", new BigDecimal("10.00"), ListingStatus.AVAILABLE);
        assertEquals("Trimmed Title", service.findById(1L).getTitle());
    }

    @Test
    void updateListing_wrongOwner_returnsFalse() {
        boolean result = service.updateListing(
                1L, SELLER_STU1,
                "Hacked Title", "Hacked Desc",
                new BigDecimal("1.00"), ListingStatus.SOLD);

        assertFalse(result);
    }

    @Test
    void updateListing_wrongOwner_listingUnchanged() {
        service.updateListing(1L, SELLER_STU1, "X", "Y", new BigDecimal("1.00"), ListingStatus.SOLD);

        Listing unchanged = service.findById(1L);
        assertEquals("EECS 2311 Textbook (Used)", unchanged.getTitle());
        assertEquals(ListingStatus.AVAILABLE,     unchanged.getStatus());
    }

    @Test
    void updateListing_nonExistentId_returnsFalse() {
        boolean result = service.updateListing(
                999L, SELLER_ABC,
                "Title", "Desc",
                new BigDecimal("10.00"), ListingStatus.AVAILABLE);

        assertFalse(result);
    }

    @Test
    void updateListing_blankEmail_returnsFalse() {
        boolean result = service.updateListing(
                1L, "  ",
                "Title", "Desc",
                new BigDecimal("10.00"), ListingStatus.AVAILABLE);

        assertFalse(result);
    }

    @Test
    void updateListing_nullEmail_returnsFalse() {
        boolean result = service.updateListing(
                1L, null,
                "Title", "Desc",
                new BigDecimal("10.00"), ListingStatus.AVAILABLE);

        assertFalse(result);
    }

    @Test
    void updateListing_blankTitle_returnsFalse() {
        boolean result = service.updateListing(
                1L, SELLER_ABC,
                "   ", "Desc",
                new BigDecimal("10.00"), ListingStatus.AVAILABLE);

        assertFalse(result);
    }

    @Test
    void updateListing_nullTitle_returnsFalse() {
        boolean result = service.updateListing(
                1L, SELLER_ABC,
                null, "Desc",
                new BigDecimal("10.00"), ListingStatus.AVAILABLE);

        assertFalse(result);
    }

    @Test
    void updateListing_blankDescription_returnsFalse() {
        boolean result = service.updateListing(
                1L, SELLER_ABC,
                "Title", "",
                new BigDecimal("10.00"), ListingStatus.AVAILABLE);

        assertFalse(result);
    }

    @Test
    void updateListing_nullDescription_returnsFalse() {
        boolean result = service.updateListing(
                1L, SELLER_ABC,
                "Title", null,
                new BigDecimal("10.00"), ListingStatus.AVAILABLE);

        assertFalse(result);
    }

    @Test
    void updateListing_negativePrice_returnsFalse() {
        boolean result = service.updateListing(
                1L, SELLER_ABC,
                "Title", "Desc",
                new BigDecimal("-1.00"), ListingStatus.AVAILABLE);

        assertFalse(result);
    }

    @Test
    void updateListing_nullPrice_returnsFalse() {
        boolean result = service.updateListing(
                1L, SELLER_ABC,
                "Title", "Desc",
                null, ListingStatus.AVAILABLE);

        assertFalse(result);
    }

    @Test
    void updateListing_nullStatus_returnsFalse() {
        boolean result = service.updateListing(
                1L, SELLER_ABC,
                "Title", "Desc",
                new BigDecimal("10.00"), null);

        assertFalse(result);
    }

    @Test
    void updateListing_zeroPriceIsValid_returnsTrue() {
        boolean result = service.updateListing(
                1L, SELLER_ABC,
                "Title", "Desc",
                BigDecimal.ZERO, ListingStatus.AVAILABLE);

        assertTrue(result);
        assertEquals(BigDecimal.ZERO, service.findById(1L).getPrice());
    }

    // -------------------------------------------------------------------------
    // updateSellerEmail
    // -------------------------------------------------------------------------

    @Test
    void updateSellerEmail_oldEmailHasNoListingsAfterMigration() {
        service.updateSellerEmail(SELLER_ABC, SELLER_NEW);

        List<Listing> oldSellerListings = service.getListingsForSeller(SELLER_ABC);
        assertTrue(oldSellerListings.isEmpty());
    }

    @Test
    void updateSellerEmail_newEmailHasMigratedListings() {
        service.updateSellerEmail(SELLER_ABC, SELLER_NEW);

        List<Listing> newSellerListings = service.getListingsForSeller(SELLER_NEW);
        assertEquals(2, newSellerListings.size());
    }

    @Test
    void updateSellerEmail_migratedListingsRetainOriginalData() {
        service.updateSellerEmail(SELLER_ABC, SELLER_NEW);

        List<Listing> newSellerListings = service.getListingsForSeller(SELLER_NEW);
        boolean foundTextbook = false;
        boolean foundKit      = false;
        for (Listing l : newSellerListings) {
            if ("EECS 2311 Textbook (Used)".equals(l.getTitle()))    foundTextbook = true;
            if ("Lab Kit - Digital Multimeter".equals(l.getTitle())) foundKit      = true;
        }
        assertTrue(foundTextbook);
        assertTrue(foundKit);
    }

    @Test
    void updateSellerEmail_otherSellersUnaffected() {
        service.updateSellerEmail(SELLER_ABC, SELLER_NEW);

        List<Listing> student1Listings = service.getListingsForSeller(SELLER_STU1);
        assertEquals(1, student1Listings.size());
        assertEquals("ENG 1101 Notes", student1Listings.get(0).getTitle());
    }

    @Test
    void updateSellerEmail_sameEmail_listingsUnchanged() {
        service.updateSellerEmail(SELLER_ABC, SELLER_ABC);

        assertEquals(2, service.getListingsForSeller(SELLER_ABC).size());
    }

    @Test
    void updateSellerEmail_nullOldEmail_noExceptionAndListingsUnchanged() {
        service.updateSellerEmail(null, SELLER_NEW);

        // Original data should be untouched
        assertEquals(2, service.getListingsForSeller(SELLER_ABC).size());
    }

    @Test
    void updateSellerEmail_nullNewEmail_noExceptionAndListingsUnchanged() {
        service.updateSellerEmail(SELLER_ABC, null);

        assertEquals(2, service.getListingsForSeller(SELLER_ABC).size());
    }
}
