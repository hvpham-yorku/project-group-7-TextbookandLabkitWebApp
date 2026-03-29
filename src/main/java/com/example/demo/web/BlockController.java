package com.example.demo.web;

import com.example.demo.domain.User;
import com.example.demo.service.BlockService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    /**
     * GET /blocked-users — view the list of blocked users (blockuser.html).
     */
    @GetMapping("/blocked-users")
    public String blockedUsersPage(HttpSession session, Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";
        User user = (User) u;
        model.addAttribute("blockedUsers", blockService.getBlockedUsers(user.getEmail()));
        return "blockuser";
    }

    /**
     * POST /block/{email} — block a user.
     * Accepts an optional {@code redirectAfter} form param so callers can control
     * where the user lands after blocking (defaults to /chat).
     */
    @PostMapping("/block/{email}")
    public String blockUser(@PathVariable("email") String emailToBlock,
                            @RequestParam(value = "redirectAfter", defaultValue = "/chat") String redirectAfter,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";
        User user = (User) u;

        try {
            blockService.blockUser(user.getEmail(), emailToBlock);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        // Only allow relative paths starting with / to prevent open-redirect
        String target = (redirectAfter != null && redirectAfter.startsWith("/")) ? redirectAfter : "/chat";
        return "redirect:" + target;
    }

    /**
     * POST /unblock/{email} — unblock a user.
     * Accepts an optional {@code redirectAfter} form param so callers can control
     * where the user lands after unblocking (defaults to /blocked-users).
     */
    @PostMapping("/unblock/{email}")
    public String unblockUser(@PathVariable("email") String emailToUnblock,
                              @RequestParam(value = "redirectAfter", defaultValue = "/blocked-users") String redirectAfter,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";
        User user = (User) u;

        blockService.unblockUser(user.getEmail(), emailToUnblock);
        redirectAttributes.addFlashAttribute("successMessage", emailToUnblock + " has been unblocked.");
        // Only allow relative paths starting with / to prevent open-redirect
        String target = (redirectAfter != null && redirectAfter.startsWith("/")) ? redirectAfter : "/blocked-users";
        return "redirect:" + target;
    }
}
