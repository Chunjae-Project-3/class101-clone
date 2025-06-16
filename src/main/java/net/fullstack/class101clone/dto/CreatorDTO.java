package net.fullstack.class101clone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatorDTO {
    private int creatorId;
    private String creatorName;
    private String creatorBannerImg;
    private String creatorProfileImg;
    private String creatorDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
