package com.example.demo.web;

import com.example.demo.domain.User;
import com.example.demo.service.CourseMaterialService;
import com.example.demo.service.ListingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CourseMaterialController {

    private final CourseMaterialService courseMaterialService;
    private final ListingService listingService;

    public CourseMaterialController(CourseMaterialService courseMaterialService,
                                    ListingService listingService) {
        this.courseMaterialService = courseMaterialService;
        this.listingService = listingService;
    }

    @GetMapping("/course-materials")
    public String courseMaterials(@RequestParam(value = "courseCode", required = false) String courseCode,
                                  HttpSession session,
                                  Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;
        model.addAttribute("user", user);

        String cc = courseCode == null ? "" : courseCode.trim();
        model.addAttribute("courseCode", cc);
        model.addAttribute("seededCourseCodes", courseMaterialService.getSeededCourseCodes());

        if (!cc.isBlank()) {
            model.addAttribute("materials", courseMaterialService.getMaterialsForCourse(cc));
            model.addAttribute("matchingListings", listingService.searchAndSort(cc, "newest"));
        }

        return "course-materials";
    }
}