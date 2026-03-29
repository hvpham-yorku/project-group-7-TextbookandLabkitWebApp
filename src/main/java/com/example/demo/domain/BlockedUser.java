package com.example.demo.domain;

import java.time.LocalDateTime;

public class BlockedUser {

    private final String blockerEmail;
    private final String blockedEmail;
    private final LocalDateTime blockedAt;

    public BlockedUser(String blockerEmail, String blockedEmail) {
        this.blockerEmail = blockerEmail;
        this.blockedEmail = blockedEmail;
        this.blockedAt = LocalDateTime.now();
    }

    public String getBlockerEmail() { return blockerEmail; }
    public String getBlockedEmail() { return blockedEmail; }
    public LocalDateTime getBlockedAt() { return blockedAt; }
}
