package com.example.demo.web;

import com.example.demo.domain.ListingStatus;
import com.example.demo.domain.User;
import com.example.demo.service.ListingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
        model.addAttribute("statuses", ListingStatus.values());
        return "my-listings";
    }

    @PostMapping("/listings")
    public String createListing(@RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("price") BigDecimal price,
                                HttpSession session,
                                Model model) {

        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;

        com.example.demo.domain.Listing result = listingService.addListing(user.getEmail(), title, description, price);
        if (result == null) {
            model.addAttribute("user", user);
            model.addAttribute("listings", listingService.getListingsForSeller(user.getEmail()));
            model.addAttribute("statuses", ListingStatus.values());
            model.addAttribute("error", "Could not create listing. Please check your inputs.");
            return "my-listings";
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
            model.addAttribute("statuses", ListingStatus.values());
            model.addAttribute("error", "Could not delete listing (not found or not your listing).");
            return "my-listings";
        }

        return "redirect:/my-listings";
    }

    @PostMapping("/listings/{id}/status")
    public String updateListingStatus(@PathVariable("id") long id,
                                      @RequestParam("status") ListingStatus status,
                                      HttpSession session,
                                      Model model) {

        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;

        boolean ok = listingService.updateStatus(id, user.getEmail(), status);
        if (!ok) {
            model.addAttribute("user", user);
            model.addAttribute("listings", listingService.getListingsForSeller(user.getEmail()));
            model.addAttribute("statuses", ListingStatus.values());
            model.addAttribute("error", "Could not update listing status (not found or not your listing).");
            return "my-listings";
        }

        return "redirect:/my-listings";
    }
}