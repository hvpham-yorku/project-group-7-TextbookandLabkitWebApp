package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    //for memory this would save what u did in the run and keep it save as long program is running
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String email, String password) {

        // 1. Validate York email
        if (email == null || !email.endsWith("@my.yorku.ca")) {
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

        //to save the user - for login/out when session starts
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
        if (newEmail == null || !newEmail.endsWith("@my.yorku.ca")) return null;

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
