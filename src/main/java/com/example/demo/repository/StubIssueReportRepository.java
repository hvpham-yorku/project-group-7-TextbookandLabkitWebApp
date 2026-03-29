package com.example.demo.repository;

import com.example.demo.domain.IssueReport;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("stub")
public class StubIssueReportRepository implements IssueReportRepository {

    private final List<IssueReport> reports = new ArrayList<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    @Override
    public IssueReport save(IssueReport report) {
        report.setId(idSeq.getAndIncrement());
        reports.add(report);
        return report;
    }

    @Override
    public List<IssueReport> findByTransactionId(long transactionId) {
        List<IssueReport> result = new ArrayList<>();
        for (IssueReport report : reports) {
            if (report.getTransactionId() == transactionId) {
                result.add(report);
            }
        }
        result.sort(Comparator.comparing(IssueReport::getCreatedAt).reversed());
        return result;
    }
}
