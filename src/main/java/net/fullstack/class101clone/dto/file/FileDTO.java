package net.fullstack.class101clone.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FileDTO {
    private int fileIdx;
    private String fileName;
    private String fileExt;
    private String filePath;
    private long fileSize;
    private String fileOrgName;
}
