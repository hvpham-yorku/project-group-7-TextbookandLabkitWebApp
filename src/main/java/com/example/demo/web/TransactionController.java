package com.example.demo.web;

import com.example.demo.domain.ContactMessage;
import com.example.demo.domain.IssueCategory;
import com.example.demo.domain.IssueSeverity;
import com.example.demo.domain.Listing;
import com.example.demo.domain.Transaction;
import com.example.demo.domain.User;
import com.example.demo.service.ContactMessageService;
import com.example.demo.service.IssueReportService;
import com.example.demo.service.ListingService;
import com.example.demo.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TransactionController {

    private final TransactionService transactionService;
    private final ContactMessageService contactMessageService;
    private final ListingService listingService;
    private final IssueReportService issueReportService;

    public TransactionController(TransactionService transactionService,
                                 ContactMessageService contactMessageService,
                                 ListingService listingService,
                                 IssueReportService issueReportService) {
        this.transactionService = transactionService;
        this.contactMessageService = contactMessageService;
        this.listingService = listingService;
        this.issueReportService = issueReportService;
    }

    @GetMapping("/transactions")
    public String transactions(HttpSession session, Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;
        List<Transaction> transactions = transactionService.getTransactionsForUser(user.getEmail());
        Map<Long, List<com.example.demo.domain.IssueReport>> issueMap = new LinkedHashMap<>();
        for (Transaction transaction : transactions) {
            issueMap.put(transaction.getId(), issueReportService.getReportsForTransaction(transaction.getId()));
        }

        model.addAttribute("user", user);
        model.addAttribute("transactions", transactions);
        model.addAttribute("issueMap", issueMap);
        model.addAttribute("issueCategories", IssueCategory.values());
        model.addAttribute("issueSeverities", IssueSeverity.values());
        return "transactions";
    }

    @PostMapping("/transactions/start")
    public String startTransaction(@RequestParam("messageId") long messageId,
                                   HttpSession session) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;
        ContactMessage message = contactMessageService.findById(messageId);
        if (message == null) return "redirect:/inbox";

        Listing listing = listingService.findById(message.getListingId());
        if (listing == null) return "redirect:/inbox";

        transactionService.startExchange(message, listing, user.getEmail());
        return "redirect:/transactions?started=true";
    }

    @PostMapping("/transactions/{id}/confirm")
    public String confirmTransaction(@PathVariable("id") long transactionId,
                                     HttpSession session) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;
        transactionService.confirmExchange(transactionId, user.getEmail());
        return "redirect:/transactions?confirmed=true";
    }

    @PostMapping("/transactions/{id}/cancel")
    public String cancelTransaction(@PathVariable("id") long transactionId,
                                    HttpSession session) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;
        transactionService.cancelTransaction(transactionId, user.getEmail());
        return "redirect:/transactions?cancelled=true";
    }

    @PostMapping("/transactions/{id}/report-issue")
    public String reportIssue(@PathVariable("id") long transactionId,
                              @RequestParam("category") IssueCategory category,
                              @RequestParam("severity") IssueSeverity severity,
                              @RequestParam("description") String description,
                              HttpSession session) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;
        issueReportService.submitIssue(transactionId, user.getEmail(), category, severity, description);
        return "redirect:/transactions?issueReported=true";
    }
}
