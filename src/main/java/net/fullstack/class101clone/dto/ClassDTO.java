package net.fullstack.class101clone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDTO {
    private int classIdx;
    private String classTitle;
    private String classDescription;
    private String thumbnailUrl;
    private String categoryName;

    private String creatorName;
    private String creatorProfileImg;
    private String creatorDescription;

    private boolean liked;

    private LocalDateTime createdAt;

    public ClassDTO(Integer classIdx, String classTitle, String classDescription, String thumbnailUrl, String categoryName) {
        this.classIdx = classIdx;
        this.classTitle = classTitle;
        this.classDescription = classDescription;
        this.thumbnailUrl = thumbnailUrl;
        this.categoryName = categoryName;
    }

    public ClassDTO(Integer classIdx, String classTitle, String classDescription,
                    String thumbnailUrl, String categoryName,
                    String creatorName, String creatorProfileImg, String creatorDescription) {
        this.classIdx = classIdx;
        this.classTitle = classTitle;
        this.classDescription = classDescription;
        this.thumbnailUrl = thumbnailUrl;
        this.categoryName = categoryName;
        this.creatorName = creatorName;
        this.creatorProfileImg = creatorProfileImg;
        this.creatorDescription = creatorDescription;
    }


}
