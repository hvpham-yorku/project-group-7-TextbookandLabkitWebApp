package com.example.demo.service;

import com.example.demo.domain.ContactMessage;
import com.example.demo.domain.Listing;
import com.example.demo.domain.ListingStatus;
import com.example.demo.domain.Transaction;
import com.example.demo.domain.TransactionStatus;
import com.example.demo.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ListingService listingService;

    public TransactionService(TransactionRepository transactionRepository,
                              ListingService listingService) {
        this.transactionRepository = transactionRepository;
        this.listingService = listingService;
    }

    public Transaction startExchange(ContactMessage message, Listing listing, String sellerEmail) {
        if (message == null || listing == null || sellerEmail == null || sellerEmail.isBlank()) return null;
        if (!sellerEmail.equalsIgnoreCase(message.getSellerEmail())) return null;
        if (!sellerEmail.equalsIgnoreCase(listing.getSellerEmail())) return null;

        Transaction existing = transactionRepository.findBySourceMessageId(message.getId());
        if (existing != null) return existing;

        Transaction transaction = new Transaction();
        transaction.setListingId(listing.getId());
        transaction.setListingTitle(listing.getTitle());
        transaction.setCourseCode(listing.getCourseCode());
        transaction.setSourceMessageId(message.getId());
        transaction.setBuyerEmail(message.getSenderEmail());
        transaction.setSellerEmail(message.getSellerEmail());
        transaction.setStatus(TransactionStatus.MEETUP_PENDING);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);
        listingService.updateStatus(listing.getId(), sellerEmail, ListingStatus.UNAVAILABLE);
        return saved;
    }

    public Transaction findById(long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    public Transaction findBySourceMessageId(long messageId) {
        return transactionRepository.findBySourceMessageId(messageId);
    }

    public List<Transaction> getTransactionsForUser(String email) {
        if (email == null || email.isBlank()) return List.of();

        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.isParty(email))
                .sorted(Comparator.comparing(Transaction::getUpdatedAt).reversed())
                .toList();
    }

    public boolean confirmExchange(long transactionId, String userEmail) {
        Transaction transaction = transactionRepository.findById(transactionId);
        if (transaction == null || userEmail == null || userEmail.isBlank()) return false;
        if (!transaction.isParty(userEmail)) return false;
        if (transaction.isTerminal()) return false;

        if (transaction.isSeller(userEmail)) {
            transaction.setSellerConfirmed(true);
        }
        if (transaction.isBuyer(userEmail)) {
            transaction.setBuyerConfirmed(true);
        }

        if (transaction.isBuyerConfirmed() && transaction.isSellerConfirmed()) {
            transaction.setStatus(TransactionStatus.COMPLETED);
            listingService.updateStatus(transaction.getListingId(), transaction.getSellerEmail(), ListingStatus.SOLD);
        } else if (transaction.isSellerConfirmed()) {
            transaction.setStatus(TransactionStatus.AWAITING_BUYER_CONFIRMATION);
        } else if (transaction.isBuyerConfirmed()) {
            transaction.setStatus(TransactionStatus.AWAITING_SELLER_CONFIRMATION);
        }

        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
        return true;
    }

    public boolean cancelTransaction(long transactionId, String userEmail) {
        Transaction transaction = transactionRepository.findById(transactionId);
        if (transaction == null || userEmail == null || userEmail.isBlank()) return false;
        if (!transaction.isParty(userEmail)) return false;
        if (transaction.isTerminal()) return false;

        transaction.setStatus(TransactionStatus.CANCELLED);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
        listingService.updateStatus(transaction.getListingId(), transaction.getSellerEmail(), ListingStatus.AVAILABLE);
        return true;
    }

    public boolean openIssue(long transactionId, String userEmail) {
        Transaction transaction = transactionRepository.findById(transactionId);
        if (transaction == null || userEmail == null || userEmail.isBlank()) return false;
        if (!transaction.isParty(userEmail)) return false;
        if (transaction.isTerminal()) return false;

        transaction.setStatus(TransactionStatus.ISSUE_OPEN);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
        return true;
    }
}
