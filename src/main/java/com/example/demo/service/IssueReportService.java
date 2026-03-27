package com.example.demo.service;

import com.example.demo.domain.IssueCategory;
import com.example.demo.domain.IssueReport;
import com.example.demo.domain.IssueSeverity;
import com.example.demo.domain.Transaction;
import com.example.demo.repository.IssueReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IssueReportService {

    private final IssueReportRepository issueReportRepository;
    private final TransactionService transactionService;

    public IssueReportService(IssueReportRepository issueReportRepository,
                              TransactionService transactionService) {
        this.issueReportRepository = issueReportRepository;
        this.transactionService = transactionService;
    }

    public IssueReport submitIssue(long transactionId, String reporterEmail,
                                   IssueCategory category, IssueSeverity severity,
                                   String description) {

        if (reporterEmail == null || reporterEmail.isBlank()) return null;
        if (category == null || severity == null) return null;
        if (description == null || description.isBlank()) return null;

        Transaction transaction = transactionService.findById(transactionId);
        if (transaction == null || !transaction.isParty(reporterEmail) || transaction.isTerminal()) return null;

        IssueReport report = new IssueReport();
        report.setTransactionId(transactionId);
        report.setReporterEmail(reporterEmail.trim());
        report.setCategory(category);
        report.setSeverity(severity);
        report.setDescription(description.trim());
        report.setCreatedAt(LocalDateTime.now());

        IssueReport saved = issueReportRepository.save(report);
        transactionService.openIssue(transactionId, reporterEmail);
        return saved;
    }

    public List<IssueReport> getReportsForTransaction(long transactionId) {
        return issueReportRepository.findByTransactionId(transactionId);
    }
}
