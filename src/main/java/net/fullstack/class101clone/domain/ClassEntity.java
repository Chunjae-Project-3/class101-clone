package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "lectureList")
@Table(name = "tbl_class")
public class ClassEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_idx", columnDefinition = "int(11) not null comment '클래스 인덱스'")
    private int classIdx;
    @Column(name = "class_title", columnDefinition = "varchar(100) not null comment '클래스 제목'")
    private String classTitle;
    @Column(name = "class_description",columnDefinition = "text null default null comment '클래스 소개'")
    private String classDescription;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "class_thumbnail_img", foreignKey = @ForeignKey(name = "FK_tbl_class_tbl_file"))
    private FileEntity classThumbnailImg;

    @ManyToOne(fetch = FetchType.LAZY) // default 이미지
    @JoinColumn(name = "class_category_idx", foreignKey = @ForeignKey(name = "FK_tbl_class_tbl_category"))
    private CategoryEntity classCategory;

    @OneToMany(mappedBy = "lectureRef", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    @Builder.Default
    private List<LectureEntity> lectureList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", foreignKey = @ForeignKey(name = "FK_tbl_class_tbl_creator"))
    private CreatorEntity creator;

}
