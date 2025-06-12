package net.fullstack.class101clone.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.FileEntity;
import net.fullstack.class101clone.dto.FileDTO;
import net.fullstack.class101clone.repository.FileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final ModelMapper modelMapper;

    public int insertFile(FileDTO dto) {
        FileEntity entity = modelMapper.map(dto, FileEntity.class);
        FileEntity result = fileRepository.save(entity);
        return (result != null) ? result.getFileIdx() : 0;
    }

    @Override
    public int deleteFileByIdx(int fileIdx) {
        return fileRepository.findById(fileIdx)
                .map(entity -> {
                    try {
                        fileRepository.deleteById(fileIdx);
                        return entity.getFileIdx();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to delete file. id: " + fileIdx, e);
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("File not found. id: " + fileIdx));
    }

    @Override
    public int deleteFileByName(String fileName) {
        return fileRepository.findByFileName(fileName)
                .map(entity -> {
                    try {
                        fileRepository.deleteByFileName(fileName);
                        return entity.getFileIdx();
                    } catch (Exception e) {
                        log.info("Failed to delete file record: {}", e.getMessage());
                        throw new RuntimeException("Failed to delete file record. name: " + fileName, e);
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("File not found. name: " + fileName));
    }
}
