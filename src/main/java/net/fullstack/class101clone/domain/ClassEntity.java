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
@ToString(exclude = { "sectionList", "classLikeList" })
@Table(name = "tbl_class")
public class ClassEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_idx", columnDefinition = "int(11) not null comment '클래스 인덱스'")
    private int classIdx;

    @Column(name = "class_title", columnDefinition = "varchar(100) not null comment '클래스 제목'")
    private String classTitle;

    @Column(name = "class_description", columnDefinition = "text null default null comment '클래스 소개'")
    private String classDescription;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "class_thumbnail_img", nullable = true,
            foreignKey = @ForeignKey(name = "FK_tbl_class_tbl_file"))
    private FileEntity classThumbnailImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_category_idx", nullable = false,
            foreignKey = @ForeignKey(name = "FK_tbl_class_tbl_category"))
    private CategoryEntity classCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_sub_category_idx", foreignKey = @ForeignKey(name = "FK_tbl_class_tbl_sub_category"))
    private SubCategoryEntity classSubCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_tbl_class_tbl_creator"))
    private CreatorEntity creator;

    // 양방향 관계 설정
    @OneToMany(mappedBy = "sectionRef", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SectionEntity> sectionList = new ArrayList<>();

    @OneToMany(mappedBy = "classLikeRef", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassLikeEntity> classLikeList = new ArrayList<>();

}
