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
    void updateProfile_nameOnly_updatesNameKeepsEmail() {
        User current = new User("saif0@my.yorku.ca", "1234", "Saif");

        User result = auth.updateProfile(current, "Saif Updated", "saif0@my.yorku.ca");

        assertNotNull(result);
        assertEquals("Saif Updated", result.getName());
        assertEquals("saif0@my.yorku.ca", result.getEmail());
        assertEquals("1234", result.getPassword());

        User fromRepo = repo.findByEmail("saif0@my.yorku.ca");
        assertNotNull(fromRepo);
        assertEquals("Saif Updated", fromRepo.getName());
    }

    @Test
    void updateProfile_emailOnly_oldEmailGoneNewEmailFound() {
        User current = new User("saif0@my.yorku.ca", "1234", "Saif");

        User result = auth.updateProfile(current, "Saif", "saif.new@my.yorku.ca");

        assertNotNull(result);
        assertEquals("saif.new@my.yorku.ca", result.getEmail());
        assertEquals("Saif", result.getName());

        assertNull(repo.findByEmail("saif0@my.yorku.ca"));         // old gone
        assertNotNull(repo.findByEmail("saif.new@my.yorku.ca"));   // new present
    }

    @Test
    void updateProfile_bothNameAndEmail_persistsCorrectly() {
        User current = new User("saif0@my.yorku.ca", "1234", "Saif");

        User result = auth.updateProfile(current, "Ali", "ali@my.yorku.ca");

        assertNotNull(result);
        assertEquals("Ali", result.getName());
        assertEquals("ali@my.yorku.ca", result.getEmail());

        assertNull(repo.findByEmail("saif0@my.yorku.ca"));
        User fromRepo = repo.findByEmail("ali@my.yorku.ca");
        assertNotNull(fromRepo);
        assertEquals("Ali", fromRepo.getName());
    }

    @Test
    void updateProfile_invalidEmail_returnsNullRepositoryUnchanged() {
        User current = new User("saif0@my.yorku.ca", "1234", "Saif");

        User result = auth.updateProfile(current, "Saif", "saif@gmail.com");

        assertNull(result);
        // original record still intact
        User fromRepo = repo.findByEmail("saif0@my.yorku.ca");
        assertNotNull(fromRepo);
        assertEquals("Saif", fromRepo.getName());
    }

    @Test
    void updateProfile_nullCurrentUser_returnsNull() {
        User result = auth.updateProfile(null, "Name", "name@my.yorku.ca");
        assertNull(result);
    }

}
