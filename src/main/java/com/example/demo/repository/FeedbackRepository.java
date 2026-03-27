package com.example.demo.repository;

import com.example.demo.domain.Feedback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FeedbackRepository {

    private final List<Feedback> feedbackList = new ArrayList<>();

    public void save(Feedback feedback) {
        feedbackList.add(feedback);
    }

    public List<Feedback> findByTargetEmail(String targetEmail) {
        return feedbackList.stream()
                .filter(f -> f.getTargetEmail().equals(targetEmail))
                .collect(Collectors.toList());
    }

    public boolean hasReviewed(String reviewerEmail, String targetEmail) {
        return feedbackList.stream()
                .anyMatch(f -> f.getReviewerEmail().equals(reviewerEmail)
                        && f.getTargetEmail().equals(targetEmail));
    }

    public double getAverageRating(String targetEmail) {
        return feedbackList.stream()
                .filter(f -> f.getTargetEmail().equals(targetEmail))
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }
}
