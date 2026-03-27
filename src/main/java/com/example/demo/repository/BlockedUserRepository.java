package com.example.demo.repository;

import com.example.demo.domain.BlockedUser;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BlockedUserRepository {

    private final List<BlockedUser> blockedUsers = new ArrayList<>();

    public void save(BlockedUser block) {
        blockedUsers.add(block);
    }

    public boolean exists(String blockerEmail, String blockedEmail) {
        return blockedUsers.stream()
                .anyMatch(b -> b.getBlockerEmail().equals(blockerEmail)
                        && b.getBlockedEmail().equals(blockedEmail));
    }

    public List<BlockedUser> findByBlocker(String blockerEmail) {
        return blockedUsers.stream()
                .filter(b -> b.getBlockerEmail().equals(blockerEmail))
                .collect(Collectors.toList());
    }

    public void delete(String blockerEmail, String blockedEmail) {
        blockedUsers.removeIf(b -> b.getBlockerEmail().equals(blockerEmail)
                && b.getBlockedEmail().equals(blockedEmail));
    }
}
