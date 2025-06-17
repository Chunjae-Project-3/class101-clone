package net.fullstack.class101clone.service.classes;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.LectureEntity;
import net.fullstack.class101clone.domain.LectureHistoryEntity;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.classes.LectureHistoryDTO;
import net.fullstack.class101clone.exception.NotFoundException;
import net.fullstack.class101clone.repository.classes.LectureHistoryRepository;
import net.fullstack.class101clone.repository.classes.LectureRepository;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class LectureHistoryService {

    private final ModelMapper modelMapper;
    private final UserRepositoryIf  userRepository;
    private final LectureRepository lectureRepository;
    private final LectureHistoryRepository lectureHistoryRepository;

    public void saveLectureHistory(String userId, LectureHistoryDTO reqDTO) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found. id: " + userId));

        LectureEntity lecture = lectureRepository.findById(reqDTO.getLectureIdx())
                .orElseThrow(() -> new NotFoundException("Lecture not found. id: " + reqDTO.getLectureIdx()));

        LectureHistoryEntity history =
                lectureHistoryRepository.findByLectureHistoryUserAndLectureHistoryRef(user, lecture)
                        .orElseGet(() -> {
                            LectureHistoryEntity entity = new LectureHistoryEntity();
                            entity.setLectureHistoryUser(user);
                            entity.setLectureHistoryRef(lecture);
                            return entity;
                        });

        history.setLectureHistoryLastPosition(reqDTO.getLastPosition());
        history.setLectureHistoryTotalWatchTime(history.getLectureHistoryTotalWatchTime() + reqDTO.getTotalWatchTime());
        history.setLectureHistoryLastWatchDate(LocalDateTime.now());
        lectureHistoryRepository.save(history);
    }

    public LectureHistoryDTO getLectureHistoryByLectureId(String userId, int lectureId) {
        return lectureHistoryRepository.getLectureHistoryByLectureId(userId, lectureId);
    }
}
