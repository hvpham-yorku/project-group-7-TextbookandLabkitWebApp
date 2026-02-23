package service;

import domain.User;
import repository.UserRepository;

public class AuthService {

    private final UserRepository userRepository;
    //for memory
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String email, String password) {

        // 1. Validate York email
        if (email == null || !email.endsWith("@yorku.ca")) {
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
        
        //to save the user
        this.currentUser = user;

        // 4. Success
        return user;
    }
    
    public User getCurrentUser() {
    	return this.currentUser;
    	
    }
    
    public void logout() {
    	this.currentUser = null;
    }
    
}
