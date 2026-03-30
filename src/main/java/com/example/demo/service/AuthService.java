package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String YORK_EMAIL_DOMAIN = "@my.yorku.ca";
    private static final String YORK_EMAIL_ERROR =
            "Email must be a York University address (@my.yorku.ca).";

    public boolean isYorkEmail(String email) {
        return email != null && email.trim().toLowerCase().endsWith(YORK_EMAIL_DOMAIN);
    }

    public String getYorkEmailError() {
        return YORK_EMAIL_ERROR;
    }

    private final UserRepository userRepository;

    // for memory this would save what u did in the run and keep it saved as long as program is running
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns true if an account already exists for this email (case-insensitive).
     * Used by the signup controller to place a per-field duplicate-email error.
     */
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    /**
     * Registers a new user account.
     * Throws IllegalArgumentException for a non-York email.
     * Throws IllegalStateException if the email is already registered.
     * Uses isYorkEmail() and YORK_EMAIL_ERROR constant for consistency.
     */
    public void register(String name, String email, String password) {
        if (!isYorkEmail(email)) {
            throw new IllegalArgumentException(YORK_EMAIL_ERROR);
        }
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalStateException("An account with this email already exists.");
        }
        userRepository.save(new User(email, password, name));
    }

    public User login(String email, String password) {
        // 1. Validate York email
        if (!isYorkEmail(email)) {
            return null;
        }
        // 2. Find user in repository
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        // 3. Check password
        if (!user.getPassword().equals(password)) {
            return null;
        }
        // to save the user - for login/out when session starts
        this.currentUser = user;
        // 4. Success
        return user;
    }

    // save the user to know who logged in
    public User getCurrentUser() {
        return this.currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }

    // Updates name and email. Preserves all other profile fields from currentUser.
    public User updateProfile(User currentUser, String newName, String newEmail) {
        if (currentUser == null) return null;
        if (!isYorkEmail(newEmail)) return null;

        User updated = new User(newEmail, currentUser.getPassword(), newName);

        // Preserve existing profile fields so they are not lost on name/email change
        updated.setStudentId(currentUser.getStudentId());
        updated.setPhoneNumber(currentUser.getPhoneNumber());
        updated.setAboutMe(currentUser.getAboutMe());
        updated.setProgram(currentUser.getProgram());
        updated.setCampus(currentUser.getCampus());

        userRepository.updateUser(currentUser.getEmail(), updated);
        return updated;
    }

    // Updates the extended profile fields (phone, about me, program, campus).
    public void updateProfileDetails(User user, String phoneNumber, String aboutMe,
                                     String program, String campus) {
        if (user == null) return;
        user.setPhoneNumber(phoneNumber);
        user.setAboutMe(aboutMe);
        user.setProgram(program);
        user.setCampus(campus);
        userRepository.updateUser(user.getEmail(), user);
    }
}
