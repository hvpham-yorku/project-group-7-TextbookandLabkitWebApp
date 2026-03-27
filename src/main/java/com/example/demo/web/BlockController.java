package com.example.demo.web;

import com.example.demo.service.BlockService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping("/block/{email}")
    public String blockUser(@PathVariable String email, HttpSession session) {
        String currentUser = (String) session.getAttribute("userEmail");

        blockService.blockUser(currentUser, email);

        return "redirect:/dashboard";
    }
}
