package net.fullstack.class101clone.dto;

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
    // private String thumbnailFilePath; // 원본과 같은 파일에 저장한다고 가정
    private long thumbnailFileSize;
}
