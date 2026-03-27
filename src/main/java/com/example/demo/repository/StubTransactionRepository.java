package com.example.demo.repository;

import com.example.demo.domain.Transaction;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("stub")
public class StubTransactionRepository implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction.getId() <= 0) {
            transaction.setId(idSeq.getAndIncrement());
            transactions.add(transaction);
            return transaction;
        }

        Transaction existing = findById(transaction.getId());
        if (existing == null) {
            transactions.add(transaction);
            return transaction;
        }

        int index = transactions.indexOf(existing);
        transactions.set(index, transaction);
        return transaction;
    }

    @Override
    public Transaction findById(long id) {
        for (Transaction transaction : transactions) {
            if (transaction.getId() == id) return transaction;
        }
        return null;
    }

    @Override
    public Transaction findBySourceMessageId(long sourceMessageId) {
        for (Transaction transaction : transactions) {
            if (transaction.getSourceMessageId() == sourceMessageId) return transaction;
        }
        return null;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> copy = new ArrayList<>(transactions);
        copy.sort(Comparator.comparing(Transaction::getUpdatedAt).reversed());
        return copy;
    }
}
