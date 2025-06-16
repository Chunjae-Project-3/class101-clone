package net.fullstack.class101clone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubCategoryDTO {
    private Integer subCategoryIdx;
    private String subCategoryName;
}
