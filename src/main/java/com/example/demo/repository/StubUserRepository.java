package com.example.demo.repository;

import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.User;

@Repository
public class StubUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    public StubUserRepository() {
        // Sample York users (stub)
        users.add(new User("abc123@my.yorku.ca", "pass123", "Alex York"));
        users.add(new User("saif0@my.yorku.ca", "1234", "Saif"));
        users.add(new User("student1@my.yorku.ca", "welcome", "Student One"));
    }

    @Override
    public User findByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equalsIgnoreCase(updatedUser.getEmail())) {
                users.set(i, updatedUser);
                return;
            }
        }
    }
}
