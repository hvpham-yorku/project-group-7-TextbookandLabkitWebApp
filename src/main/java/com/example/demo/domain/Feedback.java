package com.example.demo.domain;

public class Feedback {
    private String reviewerEmail;
    private String targetUserEmail;
    private int rating;
    private String comment;

    public Feedback(String reviewerEmail, String targetUserEmail, int rating, String comment) {
        this.reviewerEmail = reviewerEmail;
        this.targetUserEmail = targetUserEmail;
        this.rating = rating;
        this.comment = comment;
    }

    public String getReviewerEmail() { return reviewerEmail; }
    public String getTargetUserEmail() { return targetUserEmail; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
}
