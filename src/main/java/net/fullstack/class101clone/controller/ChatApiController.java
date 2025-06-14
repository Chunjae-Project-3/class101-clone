package net.fullstack.class101clone.controller;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.dto.ChatDTO;
import net.fullstack.class101clone.service.ChatService;
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
}
