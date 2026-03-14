}
package com.example.demo.web;

import com.example.demo.domain.Listing;
import com.example.demo.domain.ListingStatus;
import com.example.demo.domain.User;
import com.example.demo.service.ListingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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

    @PostMapping("/listings")
    public String createListing(@RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("price") BigDecimal price,
                                HttpSession session, Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";
        User user = (User) u;
        Listing result = listingService.addListing(user.getEmail(), title, description, price);
        if (result == null) {
            model.addAttribute("user", user);
            model.addAttribute("listings", listingService.getListingsForSeller(user.getEmail()));
            model.addAttribute("error", "Could not create listing. Please check your inputs.");
            return "my-listings";
        }
        return "redirect:/my-listings";
    }

    @PostMapping("/listings/{id}/delete")
    public String deleteListing(@PathVariable("id") long id,
                                HttpSession session, Model model) {
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

    /* =========================
       KAN-15 Filter Listings
       ========================= */

    @GetMapping("/listings/filter")
    public String filterListings(@RequestParam(value = "keyword",   required = false) String keyword,
                                 @RequestParam(value = "status",    required = false) ListingStatus status,
                                 @RequestParam(value = "minPrice",  required = false) BigDecimal minPrice,
                                 @RequestParam(value = "maxPrice",  required = false) BigDecimal maxPrice,
                                 HttpSession session, Model model) {

        if (session.getAttribute("user") == null) return "redirect:/login";

        List<Listing> listings = listingService.filterListings(keyword, status, minPrice, maxPrice);

        model.addAttribute("listings", listings);
        model.addAttribute("keyword",  keyword);
        model.addAttribute("status",   status);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "listings";
    }

    /* =========================
       KAN-17 Sort Listings
       ========================= */

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

    /* =========================
       Browse All Listings
       ========================= */

    @GetMapping("/listings")
    public String allListings(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        model.addAttribute("listings", listingService.getAllListings());
        return "listings";
    }
}
