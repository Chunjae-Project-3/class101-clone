package net.fullstack.class101clone.service.file;

import net.fullstack.class101clone.dto.file.FileDTO;

public interface FileService {
    public int insertFile(FileDTO dto);
    public int deleteFileByIdx(int fileIdx);
    public int deleteFileByName(String fileName);
}
