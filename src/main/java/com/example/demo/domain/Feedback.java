package com.example.demo.domain;

import java.time.LocalDateTime;

public class Feedback {

    private final String reviewerEmail;
    private final String targetEmail;
    private final int rating;
    private final String comment;
    private final LocalDateTime submittedAt;

    public Feedback(String reviewerEmail, String targetEmail, int rating, String comment) {
        this.reviewerEmail = reviewerEmail;
        this.targetEmail = targetEmail;
        this.rating = rating;
        this.comment = comment;
        this.submittedAt = LocalDateTime.now();
    }

    public String getReviewerEmail() { return reviewerEmail; }
    public String getTargetEmail() { return targetEmail; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
}
