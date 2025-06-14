package net.fullstack.class101clone.service.file;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.FileEntity;
import net.fullstack.class101clone.dto.file.FileResponseDTO;
import net.fullstack.class101clone.exception.NotFoundException;
import net.fullstack.class101clone.repository.file.FileRepository;
import net.fullstack.class101clone.type.FileType;
import net.fullstack.class101clone.util.FilePathUtil;
import net.fullstack.class101clone.util.FileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ModelMapper modelMapper;

    private final FileUtil fileUtil;
    private final FilePathUtil filePathUtil;

    private final FileRepository fileRepository;
    private final VideoConvertService videoConvertService;

    @Override
    public FileResponseDTO uploadImage(MultipartFile file) throws IOException {
        // 1. 이미지 파일 저장
        String sFileName = fileUtil.uploadImage(file);
        String oFileName = file.getOriginalFilename();
        String ext = fileUtil.getFileExtension(oFileName);

        // 2. 이미지 썸네일 저장
        String thumbName = fileUtil.generateThumbnail(sFileName);
        File thumbFile = filePathUtil.getFullPath(FileType.THUMBNAIL).resolve(thumbName).toFile();

        FileResponseDTO responseDTO = FileResponseDTO.builder()
                .fileName(sFileName)
                .fileExt(ext)
                .filePath(filePathUtil.getRelativePath(FileType.IMAGE))
                .fileSize(file.getSize())
                .fileOrgName(oFileName)
                .thumbnailFileName(thumbName)
                .thumbnailFilePath(filePathUtil.getRelativePath(FileType.THUMBNAIL))
                .thumbnailFileSize(thumbFile.length())
                .imageFlag(true)
                .build();

        // 3. 이미지 메타 정보 DB 저장
        FileEntity entity = modelMapper.map(responseDTO, FileEntity.class);
        fileRepository.save(entity);

        return responseDTO;
    }

    @Override
    public FileResponseDTO uploadVideo(MultipartFile file) throws IOException {
        // 1. 비디오 파일 저장
        String sFileName = fileUtil.uploadVideo(file);
        String oFileName = file.getOriginalFilename();
        String ext = fileUtil.getFileExtension(oFileName);

        // 2. 비디오 hls 변환
        videoConvertService.convert(sFileName);

        FileResponseDTO responseDTO = FileResponseDTO.builder()
                .fileName(sFileName)
                .fileExt(ext)
                .filePath(filePathUtil.getRelativePath(FileType.VIDEO))
                .fileSize(file.getSize())
                .fileOrgName(oFileName)
                .imageFlag(false)
                .build();

        // 3. 비디오 메타 정보 DB 저장
        FileEntity entity = modelMapper.map(responseDTO, FileEntity.class);
        fileRepository.save(entity);

        return responseDTO;
    }

    @Override
    public int deleteFileByIdx(int fileIdx) {
        return fileRepository.findById(fileIdx)
                .map(entity -> {
                    try {
                        String fileName = entity.getFileName();
                        FileType type = FileType.fromPath(entity.getFilePath());
                        fileUtil.deleteFile(fileName, type);
                        fileRepository.deleteById(fileIdx);
                        return entity.getFileIdx();
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Unknown file path. " + entity.getFilePath(), e);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to delete file. id: " + fileIdx, e);
                    }
                })
                .orElseThrow(() -> new NotFoundException("File not found. id: " + fileIdx));
    }

    @Override
    public int deleteFileByName(String fileName) {
        return fileRepository.findByFileName(fileName)
                .map(entity -> {
                    try {
                        FileType type = FileType.fromPath(entity.getFilePath());
                        fileUtil.deleteFile(fileName, type);
                        fileRepository.deleteByFileName(fileName);
                        return entity.getFileIdx();
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Unknown file path. " + entity.getFilePath(), e);
                    }catch (Exception e) {
                        log.info("Failed to delete file record: {}", e.getMessage());
                        throw new RuntimeException("Failed to delete file record. name: " + fileName, e);
                    }
                })
                .orElseThrow(() -> new NotFoundException("File not found. name: " + fileName));
    }
}
