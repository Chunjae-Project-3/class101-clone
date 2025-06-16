package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_creator_community")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CreatorCommunityEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id", columnDefinition = "BIGINT(20) NOT NULL COMMENT '커뮤니티 인덱스'")
    private Long communityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false, columnDefinition = "INT(11) NOT NULL COMMENT '크리에이터 고유 ID'")
    private CreatorEntity creator;

    @Column(name = "title", nullable = false, length = 200, columnDefinition = "VARCHAR(200) NOT NULL COMMENT '게시글 제목'")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT NOT NULL COMMENT '게시글 컨텐츠'")
    private String content;

    @Column(name = "views", nullable = false, columnDefinition = "INT(11) NOT NULL DEFAULT 0 COMMENT '게시글 조회수'")
    private int views = 0;

    @Column(name = "pinned", nullable = false, columnDefinition = "BIT(1) NOT NULL DEFAULT b'0' COMMENT '상단 고정여부(1차 정렬 스탯)'")
    private boolean pinned = false;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BIT(1) NOT NULL DEFAULT b'0' COMMENT '게시글 삭제여부(논리삭제)'")
    private boolean deleted = false;
}
