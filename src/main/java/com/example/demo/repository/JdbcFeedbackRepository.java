package com.example.demo.repository;

import com.example.demo.domain.Feedback;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile("db")
public class JdbcFeedbackRepository implements FeedbackRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcFeedbackRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Feedback> ROW_MAPPER = (rs, rowNum) -> {
        Timestamp ts = rs.getTimestamp("submitted_at");
        LocalDateTime submittedAt = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();
        return new Feedback(
                rs.getString("reviewer_email"),
                rs.getString("target_email"),
                rs.getInt("rating"),
                rs.getString("comment"),
                submittedAt
        );
    };

    @Override
    public void save(Feedback feedback) {
        jdbcTemplate.update(
                "INSERT INTO feedback (reviewer_email, target_email, rating, comment, submitted_at) " +
                "VALUES (?, ?, ?, ?, ?)",
                feedback.getReviewerEmail(),
                feedback.getTargetEmail(),
                feedback.getRating(),
                feedback.getComment(),
                feedback.getSubmittedAt()
        );
    }

    @Override
    public List<Feedback> findByTargetEmail(String targetEmail) {
        return jdbcTemplate.query(
                "SELECT * FROM feedback WHERE LOWER(target_email) = LOWER(?) ORDER BY submitted_at DESC",
                ROW_MAPPER, targetEmail
        );
    }

    @Override
    public boolean hasReviewed(String reviewerEmail, String targetEmail) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM feedback " +
                "WHERE LOWER(reviewer_email) = LOWER(?) AND LOWER(target_email) = LOWER(?)",
                Integer.class, reviewerEmail, targetEmail
        );
        return count != null && count > 0;
    }

    @Override
    public double getAverageRating(String targetEmail) {
        Double avg = jdbcTemplate.queryForObject(
                "SELECT AVG(rating) FROM feedback WHERE LOWER(target_email) = LOWER(?)",
                Double.class, targetEmail
        );
        return avg != null ? avg : 0.0;
    }
}
