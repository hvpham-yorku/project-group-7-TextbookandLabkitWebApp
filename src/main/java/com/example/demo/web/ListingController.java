package com.example.demo.web;

import com.example.demo.domain.Listing;
import com.example.demo.domain.ListingStatus;
import com.example.demo.domain.User;
import com.example.demo.service.ListingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class ListingController {

private final ListingService listingService;

public ListingController(ListingService listingService) {
    this.listingService = listingService;
}

@GetMapping("/my-listings")
public String myListings(HttpSession session, Model model) {
    Object u = session.getAttribute("user");
    if (u == null) return "redirect:/login";
    User user = (User) u;
    model.addAttribute("user", user);
    model.addAttribute("listings", listingService.getListingsForSeller(user.getEmail()));
    return "my-listings";
}

@GetMapping("/listings/{id}")
public String listingDetail(@PathVariable("id") long id,
                            HttpSession session,
                            Model model) {

    Object u = session.getAttribute("user");
    if (u == null) return "redirect:/login";

    Listing listing = listingService.findById(id);
    if (listing == null) return "redirect:/browse";

    User user = (User) u;
    boolean isSeller = user.getEmail().equalsIgnoreCase(listing.getSellerEmail());

    model.addAttribute("listing", listing);
    model.addAttribute("isSeller", isSeller);
    return "listing-detail";
}

@GetMapping("/listings/{id}/contact")
public String contactSellerPlaceholder(@PathVariable("id") long id,
                                       HttpSession session,
                                       Model model) {

    Object u = session.getAttribute("user");
    if (u == null) return "redirect:/login";

    Listing listing = listingService.findById(id);
    if (listing == null) return "redirect:/browse";

    // Sellers cannot contact themselves
    User user = (User) u;
    if (user.getEmail().equalsIgnoreCase(listing.getSellerEmail())) {
        return "redirect:/listings/" + id;
    }

    model.addAttribute("listing", listing);
    return "contact-seller-placeholder";
}

@GetMapping("/browse")
public String browseListings(@RequestParam(value = "q", required = false) String q,
                             @RequestParam(value = "sortBy", required = false, defaultValue = "newest") String sortBy,
                             HttpSession session,
                             Model model) {

    Object u = session.getAttribute("user");
    if (u == null) return "redirect:/login";

    model.addAttribute("q", q == null ? "" : q);
    model.addAttribute("sortBy", sortBy);
    model.addAttribute("listings", listingService.searchAndSort(q, sortBy));

    return "browse-listings";
}

@PostMapping("/listings")
public String createListing(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("price") BigDecimal price,
                            @RequestParam(value = "courseCode", defaultValue = "") String courseCode,
                            @RequestParam(value = "semester", defaultValue = "") String semester,
                            @RequestParam(value = "materialType", defaultValue = "") String materialType,
                            @RequestParam(value = "condition", defaultValue = "") String condition,
                            @RequestParam(value = "exchangeType", defaultValue = "") String exchangeType,
                            @RequestParam(value = "isbn", defaultValue = "") String isbn,
                            @RequestParam(value = "bookstorePrice", required = false) BigDecimal bookstorePrice,
                            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                            HttpSession session,
                            Model model) {

    Object u = session.getAttribute("user");
    if (u == null) return "redirect:/login";

    User user = (User) u;

    Listing result = listingService.addListing(
            user.getEmail(),
            title,
            description,
            price,
            courseCode,
            semester,
            materialType,
            condition,
            exchangeType,
            isbn,
            bookstorePrice
    );

    if (result == null) {
        model.addAttribute("user", user);
        model.addAttribute("listings", listingService.getListingsForSeller(user.getEmail()));
        model.addAttribute("error", "Could not create listing. Please check your inputs.");
        return "my-listings";
    }

    // Save uploaded image if provided
    if (imageFile != null && !imageFile.isEmpty()) {
        String savedPath = saveUploadedImage(imageFile);
        if (savedPath != null) {
            listingService.updateImagePath(result.getId(), savedPath);
        }
    }

    return "redirect:/my-listings";
}

/**
 * Saves the uploaded image to the local uploads/ directory.
 * Returns the web-accessible path (e.g. "/uploads/abc123.jpg"), or null on failure.
 */
private String saveUploadedImage(MultipartFile file) {
    String original = file.getOriginalFilename();
    if (original == null || original.isBlank()) return null;

    String ext = "";
    int dot = original.lastIndexOf('.');
    if (dot >= 0) ext = original.substring(dot).toLowerCase();

    // Only allow common image types
    if (!List.of(".jpg", ".jpeg", ".png", ".webp").contains(ext)) return null;

    try {
        Path uploadDir = Paths.get("uploads");
        Files.createDirectories(uploadDir);

        String filename = UUID.randomUUID() + ext;
        Path dest = uploadDir.resolve(filename).toAbsolutePath();
        file.transferTo(dest.toFile());

        return "/uploads/" + filename;
    } catch (IOException e) {
        // Non-fatal: listing is created without image
        return null;
    }
}

@GetMapping("/listings/{id}/edit")
public String editListingPage(@PathVariable("id") long id,
                              HttpSession session,
                              Model model) {

    Object u = session.getAttribute("user");
    if (u == null) return "redirect:/login";

    User user = (User) u;

    Listing listing = listingService.findById(id);

    if (listing == null) return "redirect:/my-listings";
    if (!listing.getSellerEmail().equalsIgnoreCase(user.getEmail())) return "redirect:/my-listings";

    model.addAttribute("listing", listing);
    model.addAttribute("statuses", ListingStatus.values());

    return "edit-listing";
}

@PostMapping("/listings/{id}/edit")
public String editListingSubmit(@PathVariable("id") long id,
                                @RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("price") BigDecimal price,
                                @RequestParam("status") ListingStatus status,
                                @RequestParam(value = "isbn", defaultValue = "") String isbn,
                                @RequestParam(value = "bookstorePrice", required = false) BigDecimal bookstorePrice,
                                HttpSession session,
                                Model model) {

    Object u = session.getAttribute("user");
    if (u == null) return "redirect:/login";

    User user = (User) u;

    boolean ok = listingService.updateListing(
            id,
            user.getEmail(),
            title,
            description,
            price,
            status,
            isbn,
            bookstorePrice
    );

    if (!ok) {

        Listing listing = listingService.findById(id);

        model.addAttribute("listing", listing);
        model.addAttribute("statuses", ListingStatus.values());
        model.addAttribute("error", "Could not save changes. Please check your inputs.");

        return "edit-listing";
    }

    return "redirect:/my-listings";
}

@PostMapping("/listings/{id}/delete")
public String deleteListing(@PathVariable("id") long id,
                            HttpSession session,
                            Model model) {

    Object u = session.getAttribute("user");
    if (u == null) return "redirect:/login";

    User user = (User) u;

    boolean ok = listingService.deleteListing(id, user.getEmail());

    if (!ok) {

        model.addAttribute("user", user);
        model.addAttribute("listings", listingService.getListingsForSeller(user.getEmail()));
        model.addAttribute("error", "Could not delete listing.");

        return "my-listings";
    }

    return "redirect:/my-listings";
}

@GetMapping("/listings/filter")
public String filterListings(@RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "status", required = false) ListingStatus status,
                             @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
                             @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
                             HttpSession session,
                             Model model) {

    if (session.getAttribute("user") == null) return "redirect:/login";

    List<Listing> listings = listingService.filterListings(keyword, status, minPrice, maxPrice);

    model.addAttribute("listings", listings);
    model.addAttribute("keyword", keyword);
    model.addAttribute("status", status);
    model.addAttribute("minPrice", minPrice);
    model.addAttribute("maxPrice", maxPrice);

    return "listings";
}

@GetMapping("/listings/sort/price")
public String sortByPriceAsc(HttpSession session, Model model) {

    if (session.getAttribute("user") == null) return "redirect:/login";

    model.addAttribute("listings", listingService.getAllListingsSortedByPrice(true));
    model.addAttribute("sortBy", "price-asc");

    return "listings";
}

@GetMapping("/listings/sort/price-desc")
public String sortByPriceDesc(HttpSession session, Model model) {

    if (session.getAttribute("user") == null) return "redirect:/login";

    model.addAttribute("listings", listingService.getAllListingsSortedByPrice(false));
    model.addAttribute("sortBy", "price-desc");

    return "listings";
}

@GetMapping("/listings/sort/title")
public String sortByTitleAsc(HttpSession session, Model model) {

    if (session.getAttribute("user") == null) return "redirect:/login";

    model.addAttribute("listings", listingService.getAllListingsSortedByTitle(true));
    model.addAttribute("sortBy", "title-asc");

    return "listings";
}

@GetMapping("/listings/sort/title-desc")
public String sortByTitleDesc(HttpSession session, Model model) {

    if (session.getAttribute("user") == null) return "redirect:/login";

    model.addAttribute("listings", listingService.getAllListingsSortedByTitle(false));
    model.addAttribute("sortBy", "title-desc");

    return "listings";
}

@GetMapping("/listings")
public String allListings(HttpSession session, Model model) {

    if (session.getAttribute("user") == null) return "redirect:/login";

    model.addAttribute("listings", listingService.getAllListings());

    return "listings";
}


}
