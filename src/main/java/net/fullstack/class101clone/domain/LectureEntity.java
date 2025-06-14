package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "lectureHistoryList" })
@Table(name = "tbl_lecture")
public class LectureEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_idx", columnDefinition = "int(11) not null comment '강의 인덱스'")
    private int lectureIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_ref_idx", nullable = false,
            foreignKey = @ForeignKey(name = "FK_tbl_lecture_tbl_section"))
    private SectionEntity lectureRef;

    @Column(name = "lecture_title",columnDefinition = "varchar(100) not null comment '강의 제목'")
    private String lectureTitle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "lecture_video_idx", nullable = false,
            foreignKey = @ForeignKey(name = "FK_tbl_lecture_tbl_file"))
    private FileEntity lectureVideo;

    @Column(name = "lecture_duration",columnDefinition = "int default 0 comment '강의 재생 시간 (초 단위)'")
    private int lectureDuration;

    @Column(name = "lecture_order", columnDefinition = "int not null comment '강의 순서'")
    private int lectureOrder;

    // 양방향 관계 설정
    @OneToMany(mappedBy = "lectureHistoryRef", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureHistoryEntity> lectureHistoryList = new ArrayList<>();
}
