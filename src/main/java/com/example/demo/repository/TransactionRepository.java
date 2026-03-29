package com.example.demo.repository;

import com.example.demo.domain.Transaction;

import java.util.List;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Transaction findById(long id);
    Transaction findBySourceMessageId(long sourceMessageId);
    List<Transaction> findAll();
}
