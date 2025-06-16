package net.fullstack.class101clone.scheduler;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.repository.ChatRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChatCleanupScheduler {

    private final ChatRepository chatRepository;

    // 매일 새벽 3시에 실행 (서버 시간 기준)
    @Scheduled(cron = "0 0 3 * * *")
    // 테스트용 1분마다 삭제
    //@Scheduled(cron = "0 * * * * *")
    public void deleteOldChats() {
        //LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        LocalDateTime threshold = LocalDateTime.now().minusDays(1); // 테스트용 1일 기준
        chatRepository.deleteOldMessages(threshold);
        System.out.println("채팅 메시지 정리 완료");
    }
}
