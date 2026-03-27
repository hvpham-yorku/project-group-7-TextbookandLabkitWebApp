package com.example.demo.web;

import com.example.demo.service.FeedbackService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/feedback")
    public String leaveFeedback(
            @RequestParam String targetEmail,
            @RequestParam int rating,
            @RequestParam String comment,
            HttpSession session) {

        String reviewer = (String) session.getAttribute("userEmail");

        feedbackService.leaveFeedback(reviewer, targetEmail, rating, comment);

        return "redirect:/user/" + targetEmail;
    }
}
