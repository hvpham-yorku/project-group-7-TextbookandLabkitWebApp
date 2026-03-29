package com.example.demo.web;

import com.example.demo.domain.Conversation;
import com.example.demo.domain.User;
import com.example.demo.service.MessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // ── General Chat Room ─────────────────────────────────────────────

    /**
     * GET /chat
     * Loads all general chat room messages and renders the shared chat room.
     */
    @GetMapping("/chat")
    public String chatRoom(HttpSession session, Model model) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";
        User user = (User) u;

        model.addAttribute("messages", messageService.getGeneralChatMessages());
        model.addAttribute("currentUserEmail", user.getEmail());
        return "chat";
    }

    /**
     * POST /chat/send
     * Posts a message from the logged-in user into the general chat room,
     * then redirects back to /chat.
     */
    @PostMapping("/chat/send")
    public String sendChatMessage(@RequestParam("content") String content,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";
        User user = (User) u;

        try {
            messageService.sendGeneralChatMessage(user.getEmail(), content);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/chat";
    }

    // ── DM Backward-Compatibility Redirects ───────────────────────────
    // /messages and /messages/{email} previously served the DM inbox UI.
    // Now that /chat is the general chat room, redirect old DM paths to /chat.

    @GetMapping("/messages")
    public String messagesRedirect() {
        return "redirect:/chat";
    }

    @GetMapping("/messages/{email}")
    public String messageThreadRedirect() {
        return "redirect:/chat";
    }

    /**
     * POST /messages/send — kept for backward compatibility with any DM send forms
     * that may still be in flight. Redirects to /chat after sending.
     */
    @PostMapping("/messages/send")
    public String dmSendCompat(@RequestParam("receiverEmail") String receiverEmail,
                               @RequestParam("content") String content,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";
        User user = (User) u;

        try {
            messageService.sendMessage(user.getEmail(), receiverEmail, content);
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/chat";
    }
}
