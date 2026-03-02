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
        User alex = new User("abc123@my.yorku.ca", "pass123", "Alex York");
        alex.setStudentId("100234567");
        alex.setPhoneNumber("416-555-0101");
        alex.setAboutMe("Hi! I'm Alex, a CS student at York.");
        alex.setProgram("Computer Science");
        alex.setCampus("Keele");
        users.add(alex);

        User saif = new User("saif0@my.yorku.ca", "1234", "Saif");
        saif.setStudentId("100345678");
        saif.setPhoneNumber("647-555-0202");
        saif.setAboutMe("Software Engineering student at York University.");
        saif.setProgram("Software Engineering");
        saif.setCampus("Keele");
        users.add(saif);

        User student1 = new User("student1@my.yorku.ca", "welcome", "Student One");
        student1.setStudentId("100456789");
        student1.setProgram("Business Administration");
        student1.setCampus("Glendon");
        users.add(student1);
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
    public void updateUser(String oldEmail, User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equalsIgnoreCase(oldEmail)) {
                users.set(i, updatedUser);
                return;
            }
        }
        // oldEmail not found → silent no-op (user never existed)
    }
}
