package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_lecture_history")
public class LectureHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(11) not null comment '강의 시청 기록 인덱스'")
    private int lectureHistoryIdx;

    @ManyToOne
    @JoinColumn(name = "lecture_history_user_id", foreignKey = @ForeignKey(name = "FK_tbl_lecture_history_tbl_user"))
    private UserEntity lectureHistoryUser;

    @ManyToOne
    @JoinColumn(name = "lecture_history_ref_idx", foreignKey = @ForeignKey(name = "FK_tbl_lecture_history_tbl_lecture"))
    private LectureEntity lectureHistoryRef;

    @Column(columnDefinition = "time null default null comment '마지막 본 위치'")
    private LocalTime lectureHistoryLastPosition;
    @Column(columnDefinition = "time null default null comment '시청한 총 시간'")
    private LocalTime lectureHistoryTotalWatchTime;
    @Column(columnDefinition = "datetime null default null comment '마지막 시청 날짜'")
    private LocalDateTime lectureHistoryLastWatchDate;
}
