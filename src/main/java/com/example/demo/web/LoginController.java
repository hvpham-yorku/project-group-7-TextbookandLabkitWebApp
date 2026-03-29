package com.example.demo.web;

import com.example.demo.service.AuthService;
import com.example.demo.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String doSignup(@RequestParam("name") String name,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam("confirmPassword") String confirmPassword,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        boolean hasErrors = false;

        // Full name
        if (name.isBlank()) {
            model.addAttribute("nameError", "Full name is required.");
            hasErrors = true;
        }

        // Email — blank, then domain, then duplicate (in that order)
        if (email.isBlank()) {
            model.addAttribute("emailError", "Email is required.");
            hasErrors = true;
        } else if (!email.trim().endsWith("@my.yorku.ca")) {
            model.addAttribute("emailError", "Email must be a York University address (@my.yorku.ca).");
            hasErrors = true;
        } else if (authService.emailExists(email.trim())) {
            model.addAttribute("emailError", "An account with this email already exists.");
            hasErrors = true;
        }

        // Password
        if (password.isBlank()) {
            model.addAttribute("passwordError", "Password is required.");
            hasErrors = true;
        }

        // Confirm password — blank first, then mismatch
        if (confirmPassword.isBlank()) {
            model.addAttribute("confirmPasswordError", "Please confirm your password.");
            hasErrors = true;
        } else if (!password.isBlank() && !password.equals(confirmPassword)) {
            model.addAttribute("confirmPasswordError", "Passwords do not match.");
            hasErrors = true;
        }

        if (hasErrors) {
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            return "signup";
        }

        authService.register(name.trim(), email.trim(), password);
        redirectAttributes.addFlashAttribute("successMessage", "Account created! You can now log in.");
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("email") String email,
                          @RequestParam("password") String password,
                          Model model,
                          HttpSession session) {

        User user = authService.login(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid email/password (must be @my.yorku.ca).");
            return "login";
        }

        session.setAttribute("user", user);
        return "redirect:/browse";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        model.addAttribute("user", u);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfilePage(HttpSession session, Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        model.addAttribute("user", u);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String editProfileSubmit(
    		 @RequestParam("name") String name,
            @RequestParam(value = "phoneNumber", required = false, defaultValue = "") String phoneNumber,
            @RequestParam(value = "aboutMe",     required = false, defaultValue = "") String aboutMe,
            @RequestParam(value = "program",     required = false, defaultValue = "") String program,
            @RequestParam(value = "campus",      required = false, defaultValue = "") String campus,
            HttpSession session,
            Model model) {

        User current = (User) session.getAttribute("user");
        if (current == null) return "redirect:/login";

        // Email is read-only — always keep the existing email
        User updated = authService.updateProfile(current, name, current.getEmail());
        if (updated == null) {
            model.addAttribute("user", current);
            model.addAttribute("error", "Could not update profile.");
            return "profile-edit";
        }

        authService.updateProfileDetails(updated, phoneNumber, aboutMe, program, campus);
        session.setAttribute("user", updated);
        return "redirect:/profile";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        authService.logout();
        return "redirect:/login";
    }
}
