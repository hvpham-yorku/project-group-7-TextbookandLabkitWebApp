package com.example.demo.repository;

import com.example.demo.domain.MaterialRequest;
import com.example.demo.domain.MaterialRequestStatus;
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
public class JdbcMaterialRequestRepository implements MaterialRequestRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMaterialRequestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper: converts a ResultSet row into a MaterialRequest object
    private static final RowMapper<MaterialRequest> MATERIAL_REQUEST_ROW_MAPPER = new RowMapper<MaterialRequest>() {
        @Override
        public MaterialRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
            MaterialRequest request = new MaterialRequest();
            request.setId(rs.getLong("id"));
            request.setRequesterEmail(rs.getString("requester_email"));
            request.setCourseCode(rs.getString("course_code"));
            request.setMaterialTitle(rs.getString("material_title"));
            request.setMaterialType(rs.getString("material_type"));
            request.setNote(rs.getString("note"));
            request.setStatus(MaterialRequestStatus.valueOf(rs.getString("status")));
            Timestamp ts = rs.getTimestamp("created_at");
            if (ts != null) {
                request.setCreatedAt(ts.toLocalDateTime());
            }
            return request;
        }
    };

    @Override
    public MaterialRequest save(MaterialRequest request) {
        String sql = """
                INSERT INTO material_requests
                    (requester_email, course_code, material_title, material_type, note, status, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.getRequesterEmail());
            ps.setString(2, request.getCourseCode());
            ps.setString(3, request.getMaterialTitle());
            ps.setString(4, request.getMaterialType());
            ps.setString(5, request.getNote());
            ps.setString(6, request.getStatus() != null
                    ? request.getStatus().name() : MaterialRequestStatus.OPEN.name());
            ps.setTimestamp(7, request.getCreatedAt() != null
                    ? Timestamp.valueOf(request.getCreatedAt()) : Timestamp.valueOf(java.time.LocalDateTime.now()));
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        Number id = (Number) keys.get("id");
        request.setId(id.longValue());
        return request;
    }

    @Override
    public List<MaterialRequest> findAll() {
        String sql = "SELECT * FROM material_requests ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, MATERIAL_REQUEST_ROW_MAPPER);
    }

    @Override
    public List<MaterialRequest> findByCourseCode(String courseCode) {
        String sql = "SELECT * FROM material_requests WHERE LOWER(course_code) = LOWER(?) ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, MATERIAL_REQUEST_ROW_MAPPER, courseCode);
    }
}
