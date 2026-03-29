package com.example.demo.repository;

import com.example.demo.domain.Feedback;
import java.util.List;

public interface FeedbackRepository {

    void save(Feedback feedback);

    List<Feedback> findByTargetEmail(String targetEmail);

    boolean hasReviewed(String reviewerEmail, String targetEmail);

    double getAverageRating(String targetEmail);
}
