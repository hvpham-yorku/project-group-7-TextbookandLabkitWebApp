package com.example.demo.domain;

public class BlockedUser {
    private String blockerEmail;
    private String blockedEmail;

    public BlockedUser(String blockerEmail, String blockedEmail) {
        this.blockerEmail = blockerEmail;
        this.blockedEmail = blockedEmail;
    }

    public String getBlockerEmail() { return blockerEmail; }
    public String getBlockedEmail() { return blockedEmail; }
}
