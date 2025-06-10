package net.fullstack.class101clone.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResultDTO {
    private String uuid;
    private String fileName;
    private String fileType;
    private long fileSize;
    private String filePath;
    private boolean imageFlag;
    private String newFileName;
    private String thumbnailFileName;
    private String thumbnailFileType;
    private String thumbnailFileSize;

    public String getNewFileNameWithUUID() {
        if (imageFlag) {
            return "s_" + uuid + fileName;
        } else {
            return uuid + fileName;
        }
    }
}
