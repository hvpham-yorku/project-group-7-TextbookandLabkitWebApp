package com.example.demo.web;

import com.example.demo.domain.User;
import com.example.demo.service.FeedbackService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * GET /feedback/{email}
     * Show the feedback page for a given user (their received reviews).
     */
    @GetMapping("/feedback/{email}")
    public String viewFeedback(@PathVariable("email") String email,
                               HttpSession session,
                               Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";
        User currentUser = (User) u;

        double avg = feedbackService.getAverageRating(email);
        int rounded = (int) Math.round(avg);
        String starDisplay = "★".repeat(rounded) + "☆".repeat(5 - rounded);

        model.addAttribute("targetEmail", email);
        model.addAttribute("feedbackList", feedbackService.getFeedbackForUser(email));
        model.addAttribute("averageRating", avg);
        model.addAttribute("starDisplay", starDisplay);
        model.addAttribute("currentUserEmail", currentUser.getEmail());

        return "feedback";
    }

    /**
     * POST /feedback
     * Submit a rating and comment for a user.
     */
    @PostMapping("/feedback")
    public String submitFeedback(@RequestParam("targetEmail") String targetEmail,
                                 @RequestParam("rating") int rating,
                                 @RequestParam("comment") String comment,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";
        User currentUser = (User) u;

        try {
            feedbackService.addFeedback(currentUser.getEmail(), targetEmail, rating, comment);
            redirectAttributes.addFlashAttribute("successMessage", "Your feedback has been submitted.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/feedback/" + targetEmail;
    }
}
