package com.example.demo.repository;

import com.example.demo.domain.User;

public interface UserRepository {
    User findByEmail(String email);
    void updateUser(User updatedUser);
}
