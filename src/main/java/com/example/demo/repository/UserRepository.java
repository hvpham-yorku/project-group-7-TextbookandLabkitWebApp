package com.example.demo.repository;

import com.example.demo.domain.User;

public interface UserRepository {
    User findByEmail(String email);
    void save(User user);
    void updateUser(String oldEmail, User updatedUser);
}
