package com.example.demo.repository;

import com.example.demo.domain.Transaction;
import com.example.demo.domain.TransactionStatus;
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
public class JdbcTransactionRepository implements TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper: converts a ResultSet row into a Transaction object
    private static final RowMapper<Transaction> TRANSACTION_ROW_MAPPER = new RowMapper<Transaction>() {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setId(rs.getLong("id"));
            transaction.setListingId(rs.getLong("listing_id"));
            transaction.setListingTitle(rs.getString("listing_title"));
            transaction.setCourseCode(rs.getString("course_code"));
            transaction.setSourceMessageId(rs.getLong("source_message_id"));
            transaction.setBuyerEmail(rs.getString("buyer_email"));
            transaction.setSellerEmail(rs.getString("seller_email"));
            transaction.setStatus(TransactionStatus.valueOf(rs.getString("status")));
            transaction.setBuyerConfirmed(rs.getBoolean("buyer_confirmed"));
            transaction.setSellerConfirmed(rs.getBoolean("seller_confirmed"));
            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                transaction.setCreatedAt(createdAt.toLocalDateTime());
            }
            Timestamp updatedAt = rs.getTimestamp("updated_at");
            if (updatedAt != null) {
                transaction.setUpdatedAt(updatedAt.toLocalDateTime());
            }
            return transaction;
        }
    };

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction.getId() <= 0) {
            return insert(transaction);
        } else {
            return update(transaction);
        }
    }

    private Transaction insert(Transaction transaction) {
        String sql = """
                INSERT INTO transactions
                    (listing_id, listing_title, course_code, source_message_id,
                     buyer_email, seller_email, status,
                     buyer_confirmed, seller_confirmed, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, transaction.getListingId());
            ps.setString(2, transaction.getListingTitle());
            ps.setString(3, transaction.getCourseCode());
            ps.setLong(4, transaction.getSourceMessageId());
            ps.setString(5, transaction.getBuyerEmail());
            ps.setString(6, transaction.getSellerEmail());
            ps.setString(7, transaction.getStatus() != null
                    ? transaction.getStatus().name() : TransactionStatus.MEETUP_PENDING.name());
            ps.setBoolean(8, transaction.isBuyerConfirmed());
            ps.setBoolean(9, transaction.isSellerConfirmed());
            ps.setTimestamp(10, transaction.getCreatedAt() != null
                    ? Timestamp.valueOf(transaction.getCreatedAt()) : Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setTimestamp(11, transaction.getUpdatedAt() != null
                    ? Timestamp.valueOf(transaction.getUpdatedAt()) : Timestamp.valueOf(java.time.LocalDateTime.now()));
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        Number id = (Number) keys.get("id");
        transaction.setId(id.longValue());
        return transaction;
    }

    private Transaction update(Transaction transaction) {
        String sql = """
                UPDATE transactions
                   SET listing_id        = ?,
                       listing_title     = ?,
                       course_code       = ?,
                       source_message_id = ?,
                       buyer_email       = ?,
                       seller_email      = ?,
                       status            = ?,
                       buyer_confirmed   = ?,
                       seller_confirmed  = ?,
                       updated_at        = ?
                 WHERE id = ?
                """;

        jdbcTemplate.update(sql,
                transaction.getListingId(),
                transaction.getListingTitle(),
                transaction.getCourseCode(),
                transaction.getSourceMessageId(),
                transaction.getBuyerEmail(),
                transaction.getSellerEmail(),
                transaction.getStatus() != null
                        ? transaction.getStatus().name() : TransactionStatus.MEETUP_PENDING.name(),
                transaction.isBuyerConfirmed(),
                transaction.isSellerConfirmed(),
                transaction.getUpdatedAt() != null
                        ? Timestamp.valueOf(transaction.getUpdatedAt()) : Timestamp.valueOf(java.time.LocalDateTime.now()),
                transaction.getId());

        return transaction;
    }

    @Override
    public Transaction findById(long id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        List<Transaction> results = jdbcTemplate.query(sql, TRANSACTION_ROW_MAPPER, id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public Transaction findBySourceMessageId(long sourceMessageId) {
        String sql = "SELECT * FROM transactions WHERE source_message_id = ?";
        List<Transaction> results = jdbcTemplate.query(sql, TRANSACTION_ROW_MAPPER, sourceMessageId);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transactions ORDER BY updated_at DESC";
        return jdbcTemplate.query(sql, TRANSACTION_ROW_MAPPER);
    }
}
