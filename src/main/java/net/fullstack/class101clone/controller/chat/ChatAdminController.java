package net.fullstack.class101clone.controller.chat;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/chat")
public class ChatAdminController {

    @GetMapping("")
    public String adminChatPage(HttpSession session, Model model) {
        return "main/adminChat";
    }
}
