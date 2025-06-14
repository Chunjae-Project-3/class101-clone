package net.fullstack.class101clone.service;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.dto.ChatDTO;
import net.fullstack.class101clone.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatAdminService {

    private final ChatRepository chatRepository;

    // 관리자와 채팅한 사용자 목록
    public List<String> getChatUserList() {
        return chatRepository.findDistinctUserIdsCommunicatingWithAdmin();
    }

    // 특정 사용자와의 채팅 메시지 조회
    public List<ChatDTO> getMessagesWithUser(String userId) {
        return chatRepository.findChatWithUser(userId).stream()
                .map(c -> ChatDTO.builder()
                        .idx(c.getIdx())
                        .sender(c.getSender())
                        .receiver(c.getReceiver())
                        .content(c.getContent())
                        .timestamp(c.getTimestamp().toString())
                        .build())
                .collect(Collectors.toList());
    }
}
