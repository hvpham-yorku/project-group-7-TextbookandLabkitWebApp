package com.example.demo.repository;

import com.example.demo.domain.IssueReport;

import java.util.List;

public interface IssueReportRepository {
    IssueReport save(IssueReport report);
    List<IssueReport> findByTransactionId(long transactionId);
}
