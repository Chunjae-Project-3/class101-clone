package net.fullstack.class101clone.service.file;

import net.fullstack.class101clone.dto.file.FileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    public FileResponseDTO uploadImage(MultipartFile file) throws IOException;
    public FileResponseDTO uploadVideo(MultipartFile file) throws IOException;
    public int deleteFileByIdx(int fileIdx);
    public int deleteFileByName(String fileName);
}
