package com.example.demo.repository;

import com.example.demo.domain.Feedback;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackRepository {

    private final List<Feedback> feedbacks = new ArrayList<>();

    public void save(Feedback feedback) {
        feedbacks.add(feedback);
    }

    public List<Feedback> findByUser(String email) {
        return feedbacks.stream()
                .filter(f -> f.getTargetUserEmail().equals(email))
                .collect(Collectors.toList());
    }
}
