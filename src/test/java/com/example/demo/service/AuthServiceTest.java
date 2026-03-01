package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.User;
import com.example.demo.repository.StubUserRepository;
import com.example.demo.repository.UserRepository;

public class AuthServiceTest {

    private StubUserRepository repo;
    private AuthService auth;

    @BeforeEach
    void setUp() {
        repo = new StubUserRepository();
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

    @Test
    void updateProfile_withValidUser_updatesRepositoryAndReturnsUpdatedUser() {
        User current = new User("saif0@my.yorku.ca", "1234", "Saif");

        User result = auth.updateProfile(current, "New Name", "saif0@my.yorku.ca");

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("saif0@my.yorku.ca", result.getEmail());
        assertEquals("1234", result.getPassword());

        User fromRepo = repo.findByEmail("saif0@my.yorku.ca");
        assertNotNull(fromRepo);
        assertEquals("New Name", fromRepo.getName());
    }

    @Test
    void updateProfile_withNullUser_returnsNullAndDoesNotUpdateRepository() {
        User result = auth.updateProfile(null, "New Name", "saif0@my.yorku.ca");

        assertNull(result);
        // Original stub entry must still be intact
        User fromRepo = repo.findByEmail("saif0@my.yorku.ca");
        assertNotNull(fromRepo);
        assertEquals("Saif", fromRepo.getName());
    }

}
