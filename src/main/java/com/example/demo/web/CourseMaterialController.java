package com.example.demo.web;

import com.example.demo.domain.User;
import com.example.demo.service.CourseMaterialService;
import com.example.demo.service.ListingService;
import com.example.demo.service.MaterialRequestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CourseMaterialController {

    private final CourseMaterialService courseMaterialService;
    private final ListingService listingService;
    private final MaterialRequestService materialRequestService;

    public CourseMaterialController(CourseMaterialService courseMaterialService,
                                    ListingService listingService,
                                    MaterialRequestService materialRequestService) {
        this.courseMaterialService = courseMaterialService;
        this.listingService = listingService;
        this.materialRequestService = materialRequestService;
    }

    @GetMapping("/course-materials")
    public String courseMaterials(@RequestParam(value = "courseCode", required = false) String courseCode,
                                  HttpSession session,
                                  Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;
        model.addAttribute("user", user);

        String cc = courseCode == null ? "" : courseCode.trim().toUpperCase();
        model.addAttribute("courseCode", cc);
        model.addAttribute("seededCourseCodes", courseMaterialService.getSeededCourseCodes());

        if (!cc.isBlank()) {
            model.addAttribute("materials", courseMaterialService.getMaterialsForCourse(cc));
            model.addAttribute("matchingListings", listingService.searchAndSort(cc, "newest"));
            model.addAttribute("requestDemandCount", materialRequestService.getOpenRequestCountForCourse(cc));
            model.addAttribute("requestInsights", materialRequestService.getDemandInsightsForCourse(cc));
            model.addAttribute("recentRequests", materialRequestService.getRecentRequestsForCourse(cc, 4));
        }

        return "course-materials";
    }

    @PostMapping("/course-materials/request")
    public String submitMaterialRequest(@RequestParam("courseCode") String courseCode,
                                        @RequestParam("materialTitle") String materialTitle,
                                        @RequestParam(value = "materialType", defaultValue = "") String materialType,
                                        @RequestParam(value = "note", defaultValue = "") String note,
                                        HttpSession session) {

        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;
        materialRequestService.submitRequest(user.getEmail(), courseCode, materialTitle, materialType, note);

        return "redirect:/course-materials?courseCode=" + courseCode.trim().toUpperCase() + "&requestSubmitted=true";
    }
}
