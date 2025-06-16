package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.fullstack.class101clone.domain.BaseEntity;
import net.fullstack.class101clone.domain.CreatorEntity;

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
    @Column(name = "community_id")
    private Long communityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creator;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "views", nullable = false)
    private int views = 0;

    @Column(name = "pinned", nullable = false)
    private boolean pinned = false;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

}