package net.fullstack.class101clone.dto.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureHistoryResponseDTO {
    private int classIdx;
    private String classTitle;

    private int lectureIdx;
    private String lectureTitle;

    private String thumbnailUrl;

    private int lastPosition;			    // 마지막 본 위치
    private int totalWatchTime; 		    // 시청한 총 시간(초)
    private LocalDateTime lastWatchDate;	// 마지막 시청 날짜

    private double progress;
}
