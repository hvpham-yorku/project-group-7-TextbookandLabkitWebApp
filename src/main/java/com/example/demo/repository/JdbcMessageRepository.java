package com.example.demo.repository;

import com.example.demo.domain.Message;
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
public class JdbcMessageRepository implements MessageRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper: converts a ResultSet row into a Message object
    private static final RowMapper<Message> MESSAGE_ROW_MAPPER = (rs, rowNum) -> {
        Message msg = new Message();
        msg.setId(rs.getLong("id"));
        msg.setSenderEmail(rs.getString("sender_email"));
        msg.setReceiverEmail(rs.getString("receiver_email"));
        msg.setContent(rs.getString("content"));
        Timestamp ts = rs.getTimestamp("sent_at");
        if (ts != null) {
            msg.setSentAt(ts.toLocalDateTime());
        }
        msg.setRead(rs.getBoolean("is_read"));
        return msg;
    };

    @Override
    public Message save(Message message) {
        String sql = """
                INSERT INTO direct_messages (sender_email, receiver_email, content, sent_at, is_read)
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, message.getSenderEmail());
            ps.setString(2, message.getReceiverEmail());
            ps.setString(3, message.getContent());
            ps.setTimestamp(4, message.getSentAt() != null
                    ? Timestamp.valueOf(message.getSentAt())
                    : Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setBoolean(5, message.isRead());
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        message.setId(((Number) keys.get("id")).longValue());
        return message;
    }

    @Override
    public List<Message> findConversation(String emailA, String emailB) {
        String sql = """
                SELECT * FROM direct_messages
                 WHERE (LOWER(sender_email) = LOWER(?) AND LOWER(receiver_email) = LOWER(?))
                    OR (LOWER(sender_email) = LOWER(?) AND LOWER(receiver_email) = LOWER(?))
                 ORDER BY sent_at ASC
                """;
        return jdbcTemplate.query(sql, MESSAGE_ROW_MAPPER, emailA, emailB, emailB, emailA);
    }

    @Override
    public List<String> findConversationPartners(String email) {
        String sql = """
                SELECT DISTINCT
                    CASE WHEN LOWER(sender_email) = LOWER(?) THEN receiver_email
                         ELSE sender_email
                    END AS partner
                FROM direct_messages
                WHERE LOWER(sender_email) = LOWER(?) OR LOWER(receiver_email) = LOWER(?)
                """;
        return jdbcTemplate.queryForList(sql, String.class, email, email, email);
    }

    @Override
    public void markAsRead(String receiverEmail, String senderEmail) {
        String sql = """
                UPDATE direct_messages
                   SET is_read = TRUE
                 WHERE LOWER(receiver_email) = LOWER(?)
                   AND LOWER(sender_email)   = LOWER(?)
                   AND is_read = FALSE
                """;
        jdbcTemplate.update(sql, receiverEmail, senderEmail);
    }

    @Override
    public long countUnread(String receiverEmail) {
        String sql = """
                SELECT COUNT(*) FROM direct_messages
                 WHERE LOWER(receiver_email) = LOWER(?) AND is_read = FALSE
                """;
        Long count = jdbcTemplate.queryForObject(sql, Long.class, receiverEmail);
        return count != null ? count : 0L;
    }

    @Override
    public List<Message> findAllGeneralChatMessages() {
        String sql = """
                SELECT * FROM direct_messages
                 WHERE receiver_email = 'GENERAL_CHAT'
                 ORDER BY sent_at ASC
                """;
        return jdbcTemplate.query(sql, MESSAGE_ROW_MAPPER);
    }
}
