package com.example.demo.repository;

import com.example.demo.domain.Notification;
import com.example.demo.domain.NotificationType;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Map;

@Repository
@Profile("db")
public class JdbcNotificationRepository implements NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNotificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Notification> ROW_MAPPER = (rs, rowNum) -> {
        Notification n = new Notification(
                rs.getLong("id"),
                rs.getString("recipient_email"),
                rs.getString("message"),
                NotificationType.valueOf(rs.getString("type"))
        );
        n.setRead(rs.getBoolean("is_read"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            n.setCreatedAt(ts.toLocalDateTime());
        }
        return n;
    };

    @Override
    public Notification save(Notification notification) {
        String sql = """
                INSERT INTO notifications
                    (recipient_email, message, type, is_read, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, notification.getRecipientEmail());
            ps.setString(2, notification.getMessage());
            ps.setString(3, notification.getType().name());
            ps.setBoolean(4, notification.isRead());
            ps.setTimestamp(5, Timestamp.valueOf(notification.getCreatedAt()));
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        Number id = (Number) keys.get("id");
        notification.setId(id.longValue());
        return notification;
    }

    @Override
    public Notification findById(long id) {
        String sql = "SELECT * FROM notifications WHERE id = ?";
        List<Notification> results = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Notification> findByRecipientEmail(String recipientEmail) {
        String sql = """
                SELECT * FROM notifications
                 WHERE LOWER(recipient_email) = LOWER(?)
                 ORDER BY created_at DESC
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, recipientEmail);
    }

    @Override
    public List<Notification> findUnreadByRecipientEmail(String recipientEmail) {
        String sql = """
                SELECT * FROM notifications
                 WHERE LOWER(recipient_email) = LOWER(?)
                   AND is_read = false
                 ORDER BY created_at DESC
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, recipientEmail);
    }

    @Override
    public int countUnreadByRecipientEmail(String recipientEmail) {
        String sql = """
                SELECT COUNT(*) FROM notifications
                 WHERE LOWER(recipient_email) = LOWER(?)
                   AND is_read = false
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, recipientEmail);
        return count != null ? count : 0;
    }

    @Override
    public void markAsRead(long id) {
        String sql = "UPDATE notifications SET is_read = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
