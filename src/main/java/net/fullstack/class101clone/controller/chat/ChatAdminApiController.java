package net.fullstack.class101clone.controller.chat;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.dto.ChatDTO;
import net.fullstack.class101clone.service.ChatAdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/chat")
@RequiredArgsConstructor
public class ChatAdminApiController {

    private final ChatAdminService chatAdminService;

    // 유저 목록
    @GetMapping("/users")
    public List<String> getChatUserList() {
        return chatAdminService.getChatUserList();
    }

    // 특정 유저와의 메시지 이력
    @GetMapping("/{userId}")
    public List<ChatDTO> getMessages(@PathVariable String userId) {
        return chatAdminService.getMessagesWithUser(userId);
    }

}
