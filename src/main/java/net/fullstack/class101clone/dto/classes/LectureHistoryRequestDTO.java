package net.fullstack.class101clone.dto.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureHistoryRequestDTO {
    private int lectureIdx;
    private int lastPosition;
    private int totalWatchTime;
}
