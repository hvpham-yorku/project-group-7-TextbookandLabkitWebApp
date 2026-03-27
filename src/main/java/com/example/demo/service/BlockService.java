package com.example.demo.service;

import com.example.demo.domain.BlockedUser;
import com.example.demo.repository.BlockedUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {

    private final BlockedUserRepository blockedUserRepository;

    public BlockService(BlockedUserRepository blockedUserRepository) {
        this.blockedUserRepository = blockedUserRepository;
    }

    /**
     * Blocks a user. Does nothing if already blocked.
     */
    public void blockUser(String blockerEmail, String blockedEmail) {
        if (blockerEmail.equals(blockedEmail)) {
            throw new IllegalArgumentException("You cannot block yourself.");
        }
        if (!blockedUserRepository.exists(blockerEmail, blockedEmail)) {
            blockedUserRepository.save(new BlockedUser(blockerEmail, blockedEmail));
        }
    }

    /**
     * Unblocks a user.
     */
    public void unblockUser(String blockerEmail, String blockedEmail) {
        blockedUserRepository.delete(blockerEmail, blockedEmail);
    }

    /**
     * Returns true if either user has blocked the other.
     */
    public boolean isBlocked(String userA, String userB) {
        return blockedUserRepository.exists(userA, userB)
                || blockedUserRepository.exists(userB, userA);
    }

    /**
     * Returns all users blocked by the given email.
     */
    public List<BlockedUser> getBlockedUsers(String blockerEmail) {
        return blockedUserRepository.findByBlocker(blockerEmail);
    }
}
