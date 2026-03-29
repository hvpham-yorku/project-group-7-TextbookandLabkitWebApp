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
     * POST /block/{email} — block a user from the chat page.
     * Redirects back to the messages inbox.
     */
    @PostMapping("/block/{email}")
    public String blockUser(@PathVariable("email") String emailToBlock,
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
        return "redirect:/chat";
    }

    /**
     * POST /unblock/{email} — unblock a user from the blocked-users page.
     */
    @PostMapping("/unblock/{email}")
    public String unblockUser(@PathVariable("email") String emailToUnblock,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";
        User user = (User) u;

        blockService.unblockUser(user.getEmail(), emailToUnblock);
        redirectAttributes.addFlashAttribute("successMessage", emailToUnblock + " has been unblocked.");
        return "redirect:/blocked-users";
    }
}
