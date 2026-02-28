package com.example.demo.web;

import com.example.demo.service.AuthService;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public LoginController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          Model model,
                          HttpSession session) {

        User user = authService.login(email, password);

        if (user == null) {
            model.addAttribute("error", "Invalid email/password (must be @my.yorku.ca).");
            return "login";
        }

        session.setAttribute("user", user);
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        model.addAttribute("user", u);
        return "dashboard";
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
    public String editProfileSubmit(@RequestParam String name,
                                    @RequestParam String email,
                                    HttpSession session) {
        User current = (User) session.getAttribute("user");
        if (current == null) return "redirect:/login";

        User updated = new User(email, current.getPassword(), name);
        userRepository.updateUser(updated);
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
