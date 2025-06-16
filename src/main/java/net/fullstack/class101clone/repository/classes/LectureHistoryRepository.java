package net.fullstack.class101clone.repository.classes;

import net.fullstack.class101clone.domain.LectureEntity;
import net.fullstack.class101clone.domain.LectureHistoryEntity;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.service.classes.LectureHistoryService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureHistoryRepository extends JpaRepository<LectureHistoryEntity, Integer> {
    public Optional<LectureHistoryEntity> findByLectureHistoryUserAndLectureHistoryRef(UserEntity user, LectureEntity lecture);
}
