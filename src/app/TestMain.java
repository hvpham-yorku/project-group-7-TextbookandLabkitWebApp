package app;

import domain.User;
import repository.StubUserRepository;
import repository.UserRepository;
import service.AuthService;

public class TestMain {

    public static void main(String[] args) {

        // Create repository
        UserRepository repo = new StubUserRepository();

        // Create auth service
        AuthService auth = new AuthService(repo);

        // Test correct login
        User user = auth.login("saif0@yorku.ca", "1234");

        if (user != null) {
            System.out.println("Login successful!");
            System.out.println("Welcome " + user.getName());
        } else {
            System.out.println("Login failed.");
        }

        //  Test wrong password
        User wrongPass = auth.login("saif0@yorku.ca", "wrong");

        if (wrongPass == null) {
            System.out.println("Wrong password test passed.");
        }

        //  Test non-York email
        User nonYork = auth.login("gmail@gmail.com", "1234");

        if (nonYork == null) {
            System.out.println("Non-York email test passed.");
        }
    }
}
