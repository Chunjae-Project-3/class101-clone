package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tbl_lecture")
public class LectureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(11) not null comment '강의 인덱스'")
    private int lectureIdx;

    @ManyToOne
    @JoinColumn(name = "lecture_ref_idx", foreignKey = @ForeignKey(name = "FK_tbl_lecture_tbl_class"))
    private ClassEntity lectureRef;

    @Column(columnDefinition = "varchar(100) not null comment '강의 제목'")
    private String lectureTitle;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lecture_video", foreignKey = @ForeignKey(name = "FK_tbl_lecture_tbl_file"))
    private FileEntity lectureVideo;

    @Column(columnDefinition = "varchar(100) default null comment '강의 그룹 또는 섹션명'")
    private String lectureSection;

    @Column(columnDefinition = "int default 0 comment '강의 재생 시간 (초 단위)'")
    private int lectureDurationSec;

    @ManyToOne
    @JoinColumn(name = "lecture_thumbnail", foreignKey = @ForeignKey(name = "FK_tbl_lecture_thumbnail_tbl_file"))
    private FileEntity lectureThumbnail;

}
