package net.fullstack.class101clone.repository;

import net.fullstack.class101clone.domain.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    // 유저와 관리자 간의 메시지 전체 조회 (정렬 포함)
    @Query("SELECT c FROM ChatEntity c WHERE " +
            "(c.sender = :userId AND c.receiver = 'master') OR " +
            "(c.sender = 'master' AND c.receiver = :userId) " +
            "ORDER BY c.timestamp ASC")
    List<ChatEntity> findChatWithUser(@Param("userId") String userId);

    // 관리자와 채팅한 유저 목록 조회
    @Query("SELECT DISTINCT CASE " +
            "WHEN c.sender = 'master' THEN c.receiver ELSE c.sender END " +
            "FROM ChatEntity c WHERE c.sender = 'master' OR c.receiver = 'master'")
    List<String> findDistinctUserIdsCommunicatingWithAdmin();

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatEntity c WHERE c.timestamp < :expiry")
    void deleteOldMessages(@Param("expiry") LocalDateTime expiry);

    boolean existsByReceiverAndReadFalse(String receiver);

    @Modifying
    @Transactional
    @Query("UPDATE ChatEntity c SET c.read = true WHERE c.receiver = :userId AND c.read = false")
    void markAllAsRead(@Param("userId") String userId);

}
