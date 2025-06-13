package net.fullstack.class101clone.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDTO extends FileDTO {
    private boolean imageFlag;

    private String thumbnailFileName;
    private String thumbnailFilePath;
    private long thumbnailFileSize;
}
