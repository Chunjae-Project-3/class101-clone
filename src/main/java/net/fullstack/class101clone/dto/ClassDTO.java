package net.fullstack.class101clone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDTO {
    private int classIdx;
    private String classTitle;
    private String classDescription;
    private String thumbnailUrl;
    private String categoryName;

    public ClassDTO(Integer classIdx, String classTitle, String classDescription, String thumbnailUrl, String categoryName) {
        this.classIdx = classIdx;
        this.classTitle = classTitle;
        this.classDescription = classDescription;
        this.thumbnailUrl = thumbnailUrl;
        this.categoryName = categoryName;
    }

}
