package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tbl_section_file")
public class SectionFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx",columnDefinition = "int(11) not null comment '섹션 파일 인덱스'")
    private int idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_idx", nullable = false,
            foreignKey = @ForeignKey(name = "FK_tbl_section_file_tbl_section"))
    private SectionEntity section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_idx", nullable = false,
            foreignKey = @ForeignKey(name = "FK_tbl_section_file_tbl_file"))
    private FileEntity file;
}
