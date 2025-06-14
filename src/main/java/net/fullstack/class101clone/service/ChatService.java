package net.fullstack.class101clone.service;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.ChatEntity;
import net.fullstack.class101clone.dto.ChatDTO;
import net.fullstack.class101clone.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    // 채팅 저장
    public void saveMessage(ChatDTO dto) {
        ChatEntity message = ChatEntity.builder()
                .sender(dto.getSender())
                .receiver(dto.getReceiver())
                .content(dto.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        chatRepository.save(message);
    }

    // 채팅 이력 조회 (유저 기준: 사용자→관리자, 관리자→사용자)
    public List<ChatDTO> getChatHistory(String userId) {
        return chatRepository.findChatWithUser(userId).stream()
                .map(entity -> ChatDTO.builder()
                        .idx(entity.getIdx())
                        .sender(entity.getSender())
                        .receiver(entity.getReceiver())
                        .content(entity.getContent())
                        .timestamp(entity.getTimestamp().toString()) // ISO string
                        .build())
                .collect(Collectors.toList());
    }

    public boolean hasUnreadMessages(String userId) {
        return chatRepository.existsByReceiverAndReadFalse(userId);
    }

    @Transactional
    public void markMessagesAsRead(String userId) {
        chatRepository.markAllAsRead(userId);
    }

}
