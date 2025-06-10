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
@Table(name = "tbl_file")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(11) not null comment '파일 인덱스'")
    private int fileIdx;
    @Column(columnDefinition = "varchar(200) not null comment '파일명'")
    private String fileName;
    @Column(columnDefinition = "varchar(10) not null comment '파일 확장자'")
    private String fileExt;
    @Column(columnDefinition = "varchar(255) not null comment '파일 경로'")
    private String filePath;
    @Column(columnDefinition = "bigint(20) not null default '0' comment '파일 크기'")
    private long fileSize;
    @Column(columnDefinition = "varchar(200) not null comment '원본 파일 이름'")
    private String fileOrgName;
}
