package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.domain.Feedback;
import com.example.demo.repository.FeedbackRepository;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository repo;

    public FeedbackService(FeedbackRepository repo) {
        this.repo = repo;
    }

    public void leaveFeedback(String reviewer, String target, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be 1-5");
        }

        repo.save(new Feedback(reviewer, target, rating, comment));
    }

    public List<Feedback> getFeedbackForUser(String email) {
        return repo.findByUser(email);
    }
}
