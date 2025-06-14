package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "lectureList", "sectionFileList" })
@Table(name = "tbl_section")
public class SectionEntity extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_idx", columnDefinition = "int(11) not null comment '섹션 인덱스'")
    private int sectionIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_ref_idx", nullable = false,
            foreignKey = @ForeignKey(name = "FK_tbl_section_tbl_class"))
    private ClassEntity sectionRef;

    @Column(name = "section_title", columnDefinition = "varchar(100) not null comment '섹션 제목'")
    private String sectionTitle;

    @Column(name = "section_order",columnDefinition = "int not null comment '섹션 순서'")
    private int sectionOrder;

    // 양방향 관계 설정
    @OneToMany(mappedBy = "lectureRef", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureEntity> lectureList = new ArrayList<>();

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SectionFileEntity> sectionFileList = new ArrayList<>();
}
