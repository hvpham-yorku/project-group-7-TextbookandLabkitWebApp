package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.User;
import com.example.demo.repository.StubUserRepository;
import com.example.demo.repository.UserRepository;

public class AuthServiceTest {

    private AuthService auth;

    @BeforeEach
    void setUp() {
        UserRepository repo = new StubUserRepository();
        auth = new AuthService(repo);
    }

    @Test
    void validLoginShouldReturnUser() {
        User user = auth.login("saif0@yorku.ca", "1234");
        assertNotNull(user);
        assertEquals("Saif", user.getName());
    }

    @Test
    void wrongPasswordShouldReturnNull() {
        User user = auth.login("saif0@yorku.ca", "wrong");
        assertNull(user);
    }

    @Test
    void nonYorkEmailShouldReturnNull() {
        User user = auth.login("gmail@gmail.com", "1234");
        assertNull(user);
    }

    @Test
    void unknownUserShouldReturnNull() {
        User user = auth.login("unknown@yorku.ca", "1234");
        assertNull(user);
    }
}
