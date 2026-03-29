package com.example.demo.repository;

import com.example.demo.domain.Feedback;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("stub")
public class StubFeedbackRepository implements FeedbackRepository {

    private final List<Feedback> feedbackList = new ArrayList<>();

    @Override
    public void save(Feedback feedback) {
        feedbackList.add(feedback);
    }

    @Override
    public List<Feedback> findByTargetEmail(String targetEmail) {
        return feedbackList.stream()
                .filter(f -> f.getTargetEmail().equalsIgnoreCase(targetEmail))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasReviewed(String reviewerEmail, String targetEmail) {
        return feedbackList.stream()
                .anyMatch(f -> f.getReviewerEmail().equalsIgnoreCase(reviewerEmail)
                        && f.getTargetEmail().equalsIgnoreCase(targetEmail));
    }

    @Override
    public double getAverageRating(String targetEmail) {
        return feedbackList.stream()
                .filter(f -> f.getTargetEmail().equalsIgnoreCase(targetEmail))
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }
}
