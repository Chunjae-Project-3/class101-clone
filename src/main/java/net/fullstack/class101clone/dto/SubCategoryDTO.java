package net.fullstack.class101clone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDTO {
    private Integer subCategoryIdx;
    private String subCategoryName;
}
