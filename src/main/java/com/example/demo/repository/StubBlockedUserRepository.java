package com.example.demo.repository;

import com.example.demo.domain.BlockedUser;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("stub")
public class StubBlockedUserRepository implements BlockedUserRepository {

    private final List<BlockedUser> blockedUsers = new ArrayList<>();

    @Override
    public void save(BlockedUser block) {
        blockedUsers.add(block);
    }

    @Override
    public boolean exists(String blockerEmail, String blockedEmail) {
        return blockedUsers.stream()
                .anyMatch(b -> b.getBlockerEmail().equalsIgnoreCase(blockerEmail)
                        && b.getBlockedEmail().equalsIgnoreCase(blockedEmail));
    }

    @Override
    public List<BlockedUser> findByBlocker(String blockerEmail) {
        return blockedUsers.stream()
                .filter(b -> b.getBlockerEmail().equalsIgnoreCase(blockerEmail))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String blockerEmail, String blockedEmail) {
        blockedUsers.removeIf(b -> b.getBlockerEmail().equalsIgnoreCase(blockerEmail)
                && b.getBlockedEmail().equalsIgnoreCase(blockedEmail));
    }
}
