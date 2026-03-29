package com.example.demo.repository;

import com.example.demo.domain.ContactMessage;
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
public class JdbcContactMessageRepository implements ContactMessageRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcContactMessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<ContactMessage> ROW_MAPPER = (rs, rowNum) -> {
        ContactMessage m = new ContactMessage(
                rs.getLong("id"),
                rs.getLong("listing_id"),
                rs.getString("sender_email"),
                rs.getString("seller_email"),
                rs.getString("subject"),
                rs.getString("message")
        );
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            m.setCreatedAt(ts.toLocalDateTime());
        }
        return m;
    };

    @Override
    public ContactMessage save(ContactMessage message) {
        String sql = """
                INSERT INTO contact_messages
                    (listing_id, sender_email, seller_email, subject, message, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, message.getListingId());
            ps.setString(2, message.getSenderEmail());
            ps.setString(3, message.getSellerEmail());
            ps.setString(4, message.getSubject());
            ps.setString(5, message.getMessage());
            ps.setTimestamp(6, Timestamp.valueOf(message.getCreatedAt()));
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        Number id = (Number) keys.get("id");
        message.setId(id.longValue());
        return message;
    }


    @Override
    public ContactMessage findById(long id) {
        String sql = "SELECT * FROM contact_messages WHERE id = ?";
        List<ContactMessage> results = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<ContactMessage> findBySellerEmail(String sellerEmail) {
        String sql = """
                SELECT * FROM contact_messages
                 WHERE LOWER(seller_email) = LOWER(?)
                 ORDER BY created_at DESC
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, sellerEmail);
    }

    @Override
    public List<ContactMessage> findBySenderEmail(String senderEmail) {
        String sql = """
                SELECT * FROM contact_messages
                 WHERE LOWER(sender_email) = LOWER(?)
                 ORDER BY created_at DESC
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, senderEmail);
    }
}
