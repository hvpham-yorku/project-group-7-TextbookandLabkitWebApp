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
        model.addAttribute("listings",
                listingService.getListingsForSeller(user.getEmail()));

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

        Listing result =
                listingService.addListing(user.getEmail(), title, description, price);

        if (result == null) {

            model.addAttribute("user", user);
            model.addAttribute("listings",
                    listingService.getListingsForSeller(user.getEmail()));

            model.addAttribute("error",
                    "Could not create listing. Please check your inputs.");

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
            model.addAttribute("listings",
                    listingService.getListingsForSeller(user.getEmail()));

            model.addAttribute("error",
                    "Could not delete listing.");

            return "my-listings";
        }

        return "redirect:/my-listings";
    }


    /* =====================================
       KAN-15 Filter Listings
       ===================================== */

    @GetMapping("/listings/filter")
    public String filterListings(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) return "redirect:/login";

        List<Listing> listings =
                listingService.filterListingsBySeller(user.getEmail());

        model.addAttribute("listings", listings);

        return "listings";
    }


    /* =====================================
       KAN-17 Sort Listings
       ===================================== */

    @GetMapping("/listings/sort/price")
    public String sortListingsByPrice(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) return "redirect:/login";

        List<Listing> listings =
                listingService.sortListingsByPrice(user.getEmail());

        model.addAttribute("listings", listings);

        return "listings";
    }


    @GetMapping("/listings/sort/title")
    public String sortListingsByTitle(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) return "redirect:/login";

        List<Listing> listings =
                listingService.sortListingsByTitle(user.getEmail());

        model.addAttribute("listings", listings);

        return "listings";
    }

}
