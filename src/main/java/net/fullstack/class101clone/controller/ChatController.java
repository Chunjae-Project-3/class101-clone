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

        // 1. 메시지 읽음 처리
        chatMessageService.markMessagesAsRead(myUserId);

        // 2. 알림 뱃지 제거를 위한 플래그 설정 (세션 or 모델)
        session.setAttribute("hasUnread", false); // JS 대신 이걸로 타임리프 뱃지 제어 가능

        // 3. 관리자 계정 존재 확인
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

        // 관리자(admin) 페이지에서 전체 감시용 채널로도 보냄
//        messagingTemplate.convertAndSend("/topic/admin", message);
        messagingTemplate.convertAndSendToUser("master", "/queue/messages", message);

        // 1:1 채팅 대상에게 직접 전송
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/queue/messages", message);

        // 내가 보낸 내 메시지를 돌려받을 필요는 없지만, 필요하다면 이것도 가능
        // messagingTemplate.convertAndSendToUser(message.getSender(), "/queue/messages", message);
    }
}
