package com.example.demo.repository;

import com.example.demo.domain.BlockedUser;
import java.util.List;

public interface BlockedUserRepository {

    void save(BlockedUser block);

    boolean exists(String blockerEmail, String blockedEmail);

    List<BlockedUser> findByBlocker(String blockerEmail);

    void delete(String blockerEmail, String blockedEmail);
}
