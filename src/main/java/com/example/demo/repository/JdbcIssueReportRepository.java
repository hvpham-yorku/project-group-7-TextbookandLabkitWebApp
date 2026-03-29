package com.example.demo.repository;

import com.example.demo.domain.IssueCategory;
import com.example.demo.domain.IssueReport;
import com.example.demo.domain.IssueSeverity;
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
public class JdbcIssueReportRepository implements IssueReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcIssueReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper: converts a ResultSet row into an IssueReport object
    private static final RowMapper<IssueReport> ISSUE_REPORT_ROW_MAPPER = new RowMapper<IssueReport>() {
        @Override
        public IssueReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            IssueReport report = new IssueReport();
            report.setId(rs.getLong("id"));
            report.setTransactionId(rs.getLong("transaction_id"));
            report.setReporterEmail(rs.getString("reporter_email"));
            report.setCategory(IssueCategory.valueOf(rs.getString("category")));
            report.setSeverity(IssueSeverity.valueOf(rs.getString("severity")));
            report.setDescription(rs.getString("description"));
            Timestamp ts = rs.getTimestamp("created_at");
            if (ts != null) {
                report.setCreatedAt(ts.toLocalDateTime());
            }
            return report;
        }
    };

    @Override
    public IssueReport save(IssueReport report) {
        String sql = """
                INSERT INTO issue_reports
                    (transaction_id, reporter_email, category, severity, description, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, report.getTransactionId());
            ps.setString(2, report.getReporterEmail());
            ps.setString(3, report.getCategory() != null ? report.getCategory().name() : null);
            ps.setString(4, report.getSeverity() != null ? report.getSeverity().name() : null);
            ps.setString(5, report.getDescription());
            ps.setTimestamp(6, report.getCreatedAt() != null
                    ? Timestamp.valueOf(report.getCreatedAt()) : Timestamp.valueOf(java.time.LocalDateTime.now()));
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        Number id = (Number) keys.get("id");
        report.setId(id.longValue());
        return report;
    }

    @Override
    public List<IssueReport> findByTransactionId(long transactionId) {
        String sql = "SELECT * FROM issue_reports WHERE transaction_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, ISSUE_REPORT_ROW_MAPPER, transactionId);
    }
}
