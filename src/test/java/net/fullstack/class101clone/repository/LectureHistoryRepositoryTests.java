package net.fullstack.class101clone.repository;

import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.LectureEntity;
import net.fullstack.class101clone.domain.LectureHistoryEntity;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.classes.LectureHistoryDTO;
import net.fullstack.class101clone.repository.classes.LectureHistoryRepository;
import net.fullstack.class101clone.repository.classes.LectureRepository;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
public class LectureHistoryRepositoryTests {
    @Autowired
    private LectureHistoryRepository lectureHistoryRepository;

    @Test
    public void testGetLectureHistoryByLectureId() {
        LectureHistoryDTO dto = lectureHistoryRepository.getLectureHistoryByLectureId("test01", 1);

        log.info("result: {}", dto);
    }
}