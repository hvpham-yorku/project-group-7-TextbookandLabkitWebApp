package com.example.demo.web;

import com.example.demo.service.AuthService;
import com.example.demo.service.ListingService;
import com.example.demo.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final AuthService authService;
    private final ListingService listingService;

    public LoginController(AuthService authService, ListingService listingService) {
        this.authService = authService;
        this.listingService = listingService;
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
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";

        User user = (User) u;
        model.addAttribute("user", user);
        model.addAttribute("listings", listingService.getListingsForSeller(user.getEmail()));
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
