package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.BlockedUser;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BlockedUserRepository {

    private final List<BlockedUser> blockedUsers = new ArrayList<>();

    public void save(BlockedUser block) {
        blockedUsers.add(block);
    }

    public boolean isBlocked(String blocker, String blocked) {
        return blockedUsers.stream()
                .anyMatch(b -> b.getBlockerEmail().equals(blocker)
                        && b.getBlockedEmail().equals(blocked));
    }
}
