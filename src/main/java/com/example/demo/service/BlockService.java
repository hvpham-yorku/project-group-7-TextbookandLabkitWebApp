package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.domain.BlockedUser;
import com.example.demo.repository.BlockedUserRepository;

@Service
public class BlockService {

    private final BlockedUserRepository repo;

    public BlockService(BlockedUserRepository repo) {
        this.repo = repo;
    }

    public void blockUser(String blocker, String blocked) {
        if (!repo.isBlocked(blocker, blocked)) {
            repo.save(new BlockedUser(blocker, blocked));
        }
    }

    public boolean isBlocked(String blocker, String blocked) {
        return repo.isBlocked(blocker, blocked);
    }
}
