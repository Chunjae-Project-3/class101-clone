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
public class ClassDTO {
    private int classIdx;
    private String classTitle;
    private String classDescription;

    private String thumbnailUrl;

    private Integer categoryIdx;
    private String categoryName;
    private Integer subCategoryIdx;
    private String subCategoryName;

    private Integer creatorId;
    private String creatorName;
    private String creatorProfileImg;
    private String creatorDescription;

    @Builder.Default
    private boolean liked = false;

    private LocalDateTime createdAt;
}
