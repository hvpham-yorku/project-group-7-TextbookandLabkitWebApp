package ui;

import javax.swing.*;

import domain.User;
import repository.StubUserRepository;
import repository.UserRepository;
import service.AuthService;

import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    private AuthService authService;

    public LoginFrame() {

        // Setup backend
        UserRepository repo = new StubUserRepository();
        authService = new AuthService(repo);

        setTitle("T&L Exchange App - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("York Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        add(loginButton);

        messageLabel = new JLabel("");
        add(messageLabel);

        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {

        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        User user = authService.login(email, password);

        if (user != null) {
            messageLabel.setText("Welcome " + user.getName());
        } else {
            messageLabel.setText("Login failed.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}

