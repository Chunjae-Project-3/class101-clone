package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_creator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(exclude = { "classList" })
public class CreatorEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_id", columnDefinition = "int(11) not null comment '크리에이터 고유 ID'")
    private int creatorId;

    @Column(name = "creator_name", columnDefinition = "varchar(100) not null comment '크리에이터 이름'")
    private String creatorName;

    @Column(name = "creator_banner_img", columnDefinition = "varchar(255) null comment '크리에이터 배너 이미지 경로'")
    private String creatorBannerImg;

    @Column(name = "creator_profile_img", columnDefinition = "varchar(255) null comment '프로필 이미지 경로'")
    private String creatorProfileImg;

    @Column(name = "creator_description", columnDefinition = "text null comment '크리에이터 소개'")
    private String creatorDescription;

    // 양방향 관계 설정
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassEntity> classList = new ArrayList<>();
}
