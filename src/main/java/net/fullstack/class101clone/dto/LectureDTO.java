package net.fullstack.class101clone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LectureDTO {
	private Integer lectureIdx;
	private String lectureTitle;
	private Integer lectureDurationSec;
	private Integer totalWatchedSec; // 시청한 총 시간(초)
	private Integer progress; // 진행률(%)
	private LocalDateTime lastWatchDate;
}