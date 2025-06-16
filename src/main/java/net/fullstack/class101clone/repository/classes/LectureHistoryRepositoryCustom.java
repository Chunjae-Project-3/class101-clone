package net.fullstack.class101clone.repository.classes;

import net.fullstack.class101clone.dto.classes.LectureHistoryDTO;

public interface LectureHistoryRepositoryCustom {
    public LectureHistoryDTO getLectureHistoryByLectureId(String userId, int lectureIdx);
}
