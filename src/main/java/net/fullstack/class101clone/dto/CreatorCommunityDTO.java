package net.fullstack.class101clone.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorCommunityDTO {
    private Long communityId;
    private String title;
    private String content;
    private int views;
    private boolean pinned;
    private LocalDateTime createdAt;


    public static CreatorCommunityDTO fromEntity(net.fullstack.class101clone.domain.CreatorCommunityEntity entity) {
        return CreatorCommunityDTO.builder()
                .communityId(entity.getCommunityId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .views(entity.getViews())
                .pinned(entity.isPinned())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
