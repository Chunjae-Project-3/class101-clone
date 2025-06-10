package net.fullstack.class101clone.dto;

import lombok.Data;

@Data
public class ClassResponseDTO {
    private int classIdx;
    private String classTitle;
    private String classDescription;
    private String thumbnailUrl;
    private String categoryName;
}
