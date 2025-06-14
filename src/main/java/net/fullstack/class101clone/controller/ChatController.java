package net.fullstack.class101clone.controller;

import jakarta.servlet.http.HttpSession;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.ChatDTO;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import net.fullstack.class101clone.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ChatController {

    private final UserRepositoryIf userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(UserRepositoryIf userRepository, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/chat")
    public String chatPage(HttpSession session, Model model) {
        String myUserId = (String) session.getAttribute("loginId");
        String otherUserId = "master";

        if ("master".equals(myUserId)) {
            throw new IllegalArgumentException("관리자는 고객센터 채팅을 이용할 수 없습니다.");
        }

        Optional<UserEntity> masterUser = userRepository.findByUserId(otherUserId);
        if (masterUser.isEmpty()) {
            throw new IllegalStateException("관리자 계정이 존재하지 않습니다.");
        }

        model.addAttribute("myUserId", myUserId);
        model.addAttribute("otherUserId", otherUserId);
        return "main/chat";
    }

    @Autowired
    private ChatService chatMessageService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatDTO message, Principal principal) {
        chatMessageService.saveMessage(message); // DB 저장
        messagingTemplate.convertAndSend("/topic/admin", message);

    }

}
