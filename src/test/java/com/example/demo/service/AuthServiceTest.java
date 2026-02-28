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
        User user = auth.login("saif0@my.yorku.ca", "1234");

        assertNotNull(user);
        assertEquals("Saif", user.getName());
    }

    @Test
    void wrongPasswordShouldReturnNull() {
        User user = auth.login("saif0@my.yorku.ca", "wrong");

        assertNull(user);
    }

    @Test
    void nonYorkEmailShouldReturnNull() {
        User user = auth.login("gmail@gmail.com", "1234");

        assertNull(user);
    }

    @Test
    void wrongYorkDomainShouldReturnNull() {
        User user = auth.login("saif0@yorku.ca", "1234");

        assertNull(user);
    }

    @Test
    void unknownUserShouldReturnNull() {
        User user = auth.login("unknown@my.yorku.ca", "1234");

        assertNull(user);
    }
    
    @Test
    void successfulLoginShouldSetCurrentUser() {
        User user = auth.login("saif0@my.yorku.ca", "1234");

        assertNotNull(user);
        assertEquals(user, auth.getCurrentUser());
    }
    
    @Test
    void logoutShouldClearCurrentUser() {
        auth.login("saif0@my.yorku.ca", "1234");

        assertNotNull(auth.getCurrentUser());

        auth.logout();

        assertNull(auth.getCurrentUser());
    }

    @Test
    void logoutWithoutLoginShouldStillLeaveCurrentUserNull() {
        auth.logout();

        assertNull(auth.getCurrentUser());
    }
    
    
}
