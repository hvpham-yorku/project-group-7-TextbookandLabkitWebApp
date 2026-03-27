package com.example.demo.repository;

import com.example.demo.domain.Listing;

import com.example.demo.domain.ListingStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Map;


@Repository
@Profile("db")
public class JdbcListingRepository implements ListingRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcListingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // RowMapper: converts a ResultSet row into a Listing object

    private static final RowMapper<Listing> LISTING_ROW_MAPPER = new RowMapper<Listing>() {
        @Override
        public Listing mapRow(ResultSet rs, int rowNum) throws SQLException {

            Listing listing = new Listing(
                    rs.getLong("id"),
                    rs.getString("seller_email"),
                    rs.getString("title"),
                 rs.getString("description"),
                    rs.getBigDecimal("price"),
                    rs.getString("course_code"),
                  rs.getString("semester"),
                    rs.getString("material_type"),
                    rs.getString("condition"),
                    rs.getString("exchange_type"),
                    ListingStatus.valueOf(rs.getString("status"))
            );

            listing.setIsbn(rs.getString("isbn"));

            BigDecimal bookstorePrice = rs.getBigDecimal("bookstore_price");
            listing.setBookstorePrice(bookstorePrice); // may be null

            Timestamp ts = rs.getTimestamp("date_posted");
            if (ts != null) {
                listing.setDatePosted(ts.toLocalDateTime());
            }

            listing.setImagePath(rs.getString("image_path"));

            return listing;
        }
    };


    @Override
    public Listing create(String sellerEmail, String title, String description, BigDecimal price,
                          String courseCode, String semester, String materialType,
                          String condition, String exchangeType,
                          String isbn, BigDecimal bookstorePrice) {

        String sql = """
                INSERT INTO listings
                    (seller_email, title, description, price,
                     course_code, semester, material_type, condition, exchange_type,
                     isbn, bookstore_price, date_posted, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        LocalDateTime now = LocalDateTime.now();

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sellerEmail);
            ps.setString(2, title == null ? "" : title.trim());
            ps.setString(3, description == null ? "" : description.trim());
            ps.setBigDecimal(4, price);
            ps.setString(5, courseCode);
            ps.setString(6, semester);
            ps.setString(7, materialType);
            ps.setString(8, condition);
            ps.setString(9, exchangeType);
            ps.setString(10, isbn == null ? "" : isbn.trim());
            if (bookstorePrice != null) {
                ps.setBigDecimal(11, bookstorePrice);
            } else {
                ps.setNull(11, Types.NUMERIC);
            }
            ps.setTimestamp(12, Timestamp.valueOf(now));
            ps.setString(13, ListingStatus.AVAILABLE.name());
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        Number id = (Number) keys.get("id");
        long generatedId = id.longValue();

        // Build and return the newly created listing
        Listing listing = new Listing(
                generatedId,
                sellerEmail,
                title == null ? "" : title.trim(),
                description == null ? "" : description.trim(),
                price,
                courseCode,
                semester,
                materialType,
                condition,
                exchangeType,
                ListingStatus.AVAILABLE
        );
        listing.setIsbn(isbn == null ? "" : isbn.trim());
        listing.setBookstorePrice(bookstorePrice);
        listing.setDatePosted(now);
        return listing;
    }

 
    @Override
    public void save(Listing listing) {
        String sql = """
                UPDATE listings
                   SET seller_email    = ?,
                       title           = ?,
                       description     = ?,
                       price           = ?,
                       course_code     = ?,
                       semester        = ?,
                       material_type   = ?,
                       condition       = ?,
                       exchange_type   = ?,
                       isbn            = ?,
                       bookstore_price = ?,
                       date_posted     = ?,
                       status          = ?,
                       image_path      = ?
                 WHERE id = ?
                """;

        jdbcTemplate.update(sql,
                listing.getSellerEmail(),
                listing.getTitle(),
                listing.getDescription(),
                listing.getPrice(),
                listing.getCourseCode(),
                listing.getSemester(),
                listing.getMaterialType(),
                listing.getCondition(),
                listing.getExchangeType(),
                listing.getIsbn(),
                listing.getBookstorePrice(),
                listing.getDatePosted() != null ? Timestamp.valueOf(listing.getDatePosted()) : null,
                listing.getStatus() != null ? listing.getStatus().name() : ListingStatus.AVAILABLE.name(),
                listing.getImagePath(),
                listing.getId());
    }


    // Find a listing by its id. Returns null if not found.

    @Override
    public Listing findById(long id) {
        String sql = "SELECT * FROM listings WHERE id = ?";
        List<Listing> results = jdbcTemplate.query(sql, LISTING_ROW_MAPPER, id);
        return results.isEmpty() ? null : results.get(0);
    }


    @Override
    public boolean deleteById(long id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM listings WHERE id = ?", id);
        return rowsAffected > 0;
    }

    // Find all listings for a given seller (case-insensitive email match).
    @Override
    public List<Listing> findBySellerEmail(String sellerEmail) {
        String sql = "SELECT * FROM listings WHERE LOWER(seller_email) = LOWER(?) ORDER BY date_posted DESC";
        return jdbcTemplate.query(sql, LISTING_ROW_MAPPER, sellerEmail);
    }


    // When a user changes their email, update all their listings to use the new email.
 
    @Override
    public void updateSellerEmail(String oldEmail, String newEmail) {
        String sql = "UPDATE listings SET seller_email = ? WHERE LOWER(seller_email) = LOWER(?)";
        jdbcTemplate.update(sql, newEmail, oldEmail);
    }

    // Return all listings ordered by newest first.

    @Override
    public List<Listing> findAll() {
        String sql = "SELECT * FROM listings ORDER BY date_posted DESC";
        return jdbcTemplate.query(sql, LISTING_ROW_MAPPER);
    }
}
