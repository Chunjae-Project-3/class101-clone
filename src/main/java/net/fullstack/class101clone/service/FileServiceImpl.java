package net.fullstack.class101clone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.FileEntity;
import net.fullstack.class101clone.dto.FileDTO;
import net.fullstack.class101clone.repository.file.FileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileServiceImpl {

    private final FileRepository fileRepository;

    private final ModelMapper modelMapper;

    public int insertFile(FileDTO dto) {
        FileEntity entity = modelMapper.map(dto, FileEntity.class);
        FileEntity result = fileRepository.save(entity);
        return (result != null) ? result.getFileIdx() : 0;
    }
}
