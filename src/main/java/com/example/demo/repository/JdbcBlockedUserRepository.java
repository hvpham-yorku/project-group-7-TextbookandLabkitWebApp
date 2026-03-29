package com.example.demo.repository;

import com.example.demo.domain.BlockedUser;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile("db")
public class JdbcBlockedUserRepository implements BlockedUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcBlockedUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<BlockedUser> ROW_MAPPER = (rs, rowNum) -> {
        Timestamp ts = rs.getTimestamp("blocked_at");
        LocalDateTime blockedAt = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();
        return new BlockedUser(
                rs.getString("blocker_email"),
                rs.getString("blocked_email"),
                blockedAt
        );
    };

    @Override
    public void save(BlockedUser block) {
        jdbcTemplate.update(
                "INSERT INTO blocked_users (blocker_email, blocked_email) VALUES (?, ?) ON CONFLICT DO NOTHING",
                block.getBlockerEmail(), block.getBlockedEmail()
        );
    }

    @Override
    public boolean exists(String blockerEmail, String blockedEmail) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM blocked_users " +
                "WHERE LOWER(blocker_email) = LOWER(?) AND LOWER(blocked_email) = LOWER(?)",
                Integer.class, blockerEmail, blockedEmail
        );
        return count != null && count > 0;
    }

    @Override
    public List<BlockedUser> findByBlocker(String blockerEmail) {
        return jdbcTemplate.query(
                "SELECT * FROM blocked_users WHERE LOWER(blocker_email) = LOWER(?) ORDER BY blocked_at DESC",
                ROW_MAPPER, blockerEmail
        );
    }

    @Override
    public void delete(String blockerEmail, String blockedEmail) {
        jdbcTemplate.update(
                "DELETE FROM blocked_users " +
                "WHERE LOWER(blocker_email) = LOWER(?) AND LOWER(blocked_email) = LOWER(?)",
                blockerEmail, blockedEmail
        );
    }
}
