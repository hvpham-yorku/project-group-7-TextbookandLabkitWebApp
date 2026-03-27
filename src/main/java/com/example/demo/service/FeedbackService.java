package com.example.demo.service;

import com.example.demo.domain.Feedback;
import com.example.demo.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * Submits feedback from one user to another.
     */
    public void addFeedback(String reviewerEmail, String targetEmail, int rating, String comment) {

        if (reviewerEmail.equals(targetEmail)) {
            throw new IllegalArgumentException("You cannot leave feedback on your own profile.");
        }

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be empty.");
        }

        if (feedbackRepository.hasReviewed(reviewerEmail, targetEmail)) {
            throw new IllegalStateException("You have already left feedback for this user.");
        }

        feedbackRepository.save(new Feedback(reviewerEmail, targetEmail, rating, comment.trim()));
    }

    /**
     * Returns all feedback for a given user.
     */
    public List<Feedback> getFeedbackForUser(String targetEmail) {
        return feedbackRepository.findByTargetEmail(targetEmail);
    }

    /**
     * Returns the average star rating for a user.
     */
    public double getAverageRating(String targetEmail) {
        return feedbackRepository.getAverageRating(targetEmail);
    }
}
