package net.fullstack.class101clone.dto.upload;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Log4j2
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileUploadDTO {
    private List<MultipartFile> files;
}
