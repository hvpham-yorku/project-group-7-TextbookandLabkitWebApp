package app;

import domain.User;
import repository.StubUserRepository;
import repository.UserRepository;
import service.AuthService;

public class TestMain {

    public static void main(String[] args) {

        // 1️⃣ Create repository
        UserRepository repo = new StubUserRepository();

        // 2️⃣ Create auth service
        AuthService auth = new AuthService(repo);

        // 3️⃣ Test correct login
        User user = auth.login("saif0@yorku.ca", "1234");

        if (user != null) {
            System.out.println("Login successful!");
            System.out.println("Welcome " + user.getName());
        } else {
            System.out.println("Login failed.");
        }

        // 4️⃣ Test wrong password
        User wrongPass = auth.login("saif0@yorku.ca", "wrong");

        if (wrongPass == null) {
            System.out.println("Wrong password test passed.");
        }

        // 5️⃣ Test non-York email
        User nonYork = auth.login("gmail@gmail.com", "1234");

        if (nonYork == null) {
            System.out.println("Non-York email test passed.");
        }
    }
}
