package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * PostgreSQL-backed implementation of UserRepository.
 * Active only when the "db" Spring profile is set.
 *
 * Switch between stub and real DB:
 *   stub mode (default): spring.profiles.active=stub
 *   real DB mode:        spring.profiles.active=db
 */
@Repository
@Profile("db")
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ---------------------------------------------------------------------------
    // RowMapper: converts a ResultSet row into a User object
    // ---------------------------------------------------------------------------
    private static final RowMapper<User> USER_ROW_MAPPER = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
            );
            user.setStudentId(rs.getString("student_id"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setAboutMe(rs.getString("about_me"));
            user.setProgram(rs.getString("program"));
            user.setCampus(rs.getString("campus"));
            return user;
        }
    };

    // ---------------------------------------------------------------------------
    // Find a user by email (case-insensitive)
    // Returns null if no user is found.
    // ---------------------------------------------------------------------------
    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE LOWER(email) = LOWER(?)";
        List<User> results = jdbcTemplate.query(sql, USER_ROW_MAPPER, email);
        return results.isEmpty() ? null : results.get(0);
    }

    // ---------------------------------------------------------------------------
    // Insert a brand-new user row.
    // ---------------------------------------------------------------------------
    @Override
    public void save(User user) {
        String sql = """
                INSERT INTO users (email, password, name, student_id, phone_number, about_me, program, campus)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getStudentId(),
                user.getPhoneNumber(),
                user.getAboutMe(),
                user.getProgram(),
                user.getCampus());
    }

    // ---------------------------------------------------------------------------
    // Update an existing user.
    //
    // If the email has not changed: UPDATE the existing row.
    // If the email DID change: INSERT a new row with the new email, then DELETE
    //   the old row. (We do this because email is the primary key.)
    //
    // Note: listings.seller_email is updated separately by ListingService
    //   when the user changes their email — no foreign key cascade needed.
    // ---------------------------------------------------------------------------
    @Override
    public void updateUser(String oldEmail, User updatedUser) {

        if (oldEmail.equalsIgnoreCase(updatedUser.getEmail())) {
            // Email is unchanged — just update the other fields
            String sql = """
                    UPDATE users
                       SET name         = ?,
                           student_id   = ?,
                           phone_number = ?,
                           about_me     = ?,
                           program      = ?,
                           campus       = ?
                     WHERE LOWER(email) = LOWER(?)
                    """;
            jdbcTemplate.update(sql,
                    updatedUser.getName(),
                    updatedUser.getStudentId(),
                    updatedUser.getPhoneNumber(),
                    updatedUser.getAboutMe(),
                    updatedUser.getProgram(),
                    updatedUser.getCampus(),
                    oldEmail);

        } else {
            // Email changed — insert with new email, then delete old row
            String insertSql = """
                    INSERT INTO users (email, password, name, student_id, phone_number, about_me, program, campus)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;
            jdbcTemplate.update(insertSql,
                    updatedUser.getEmail(),
                    updatedUser.getPassword(),
                    updatedUser.getName(),
                    updatedUser.getStudentId(),
                    updatedUser.getPhoneNumber(),
                    updatedUser.getAboutMe(),
                    updatedUser.getProgram(),
                    updatedUser.getCampus());

            jdbcTemplate.update("DELETE FROM users WHERE LOWER(email) = LOWER(?)", oldEmail);
        }
    }
}
