package net.fullstack.class101clone.dto.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureDTO {
    private int lectureIdx;
    private int lectureRefIdx;
    private String lectureTitle;
    private int lectureVideoIdx;
    private int lectureDuration;
    private int lectureOrder;

    private LectureHistoryDTO lectureHistory;
}
