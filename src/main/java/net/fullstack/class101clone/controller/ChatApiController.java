package net.fullstack.class101clone.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.dto.ChatDTO;
import net.fullstack.class101clone.service.ChatService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatService chatService;

    @GetMapping("/history")
    public List<ChatDTO> getChatHistory(@RequestParam String userId) {
        return chatService.getChatHistory(userId);
    }

    // 로그인한 사용자 기준, 안 읽은 메시지 존재 여부 반환
    @GetMapping("/unread")
    public boolean hasUnreadMessages(HttpSession session) {
        String userId = (String) session.getAttribute("loginId");
        if (userId == null) return false;

        return chatService.hasUnreadMessages(userId);
    }
}
