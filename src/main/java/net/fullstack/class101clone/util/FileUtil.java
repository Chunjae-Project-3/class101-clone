package net.fullstack.class101clone.util;

import net.coobird.thumbnailator.Thumbnailator;
import net.fullstack.class101clone.dto.FileResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@PropertySource("classpath:application.properties")
public class FileUtil {

    @Value("${net.fullstack.upload.path}")
    private String uploadPath;

    // 이미지 확장자 & 최대 용량
    private static final Set<String> IMAGE_EXTS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final long MAX_IMAGE_SIZE = 1 * 1024 * 1024; // 1MB

    // 비디오 확장자 & 최대 용량
    private static final Set<String> VIDEO_EXTS = Set.of("mp4", "mov", "avi", "mkv");
    private static final long MAX_VIDEO_SIZE = 200 * 1024 * 1024; // 200MB

    public FileResponseDTO uploadImage(MultipartFile file) throws IOException {
        validateFile(file, IMAGE_EXTS, MAX_IMAGE_SIZE);

        String sFileName = saveFile(file);

        String thumbFileName = "s_" + sFileName;
        File thumbFile = Paths.get(uploadPath, thumbFileName).toFile();
        File saveFile = Paths.get(uploadPath, sFileName).toFile();
        Thumbnailator.createThumbnail(saveFile, thumbFile, 200, 200);

        String oFileName = file.getOriginalFilename();
        String ext = getFileExtension(oFileName);

        return FileResponseDTO.builder()
                .fileName(sFileName)
                .fileExt(ext)
                .fileOrgName(oFileName)
                .fileSize(file.getSize())
                .filePath(uploadPath)
                .imageFlag(true)
                .thumbnailFileName(thumbFileName)
                .thumbnailFileSize(thumbFile.length())
                .build();
    }

    public FileResponseDTO uploadVideo(MultipartFile file) throws IOException {
        validateFile(file, VIDEO_EXTS, MAX_VIDEO_SIZE);

        String sFileName = saveFile(file);
        String oFileName = file.getOriginalFilename();
        String ext = getFileExtension(oFileName);

        return FileResponseDTO.builder()
                .fileName(sFileName)
                .fileExt(ext)
                .fileOrgName(oFileName)
                .fileSize(file.getSize())
                .filePath(uploadPath)
                .imageFlag(false)
                .build();
    }

    public Map<String, Boolean> deleteFile(String fileName) throws IOException {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        Map<String, Boolean> result = new HashMap<>();
        boolean fileDeleteFlag = false;
        boolean thumbDeleteFlag = false;

        String contentType = Files.probeContentType(resource.getFile().toPath());
        if (contentType.startsWith("image")) {
            File thumbFile = new File(uploadPath + File.separator + "s_" + fileName);
            if (thumbFile.exists()) thumbDeleteFlag = thumbFile.delete();
        }
        fileDeleteFlag = resource.getFile().delete();

        result.put("fileDeleteFlag", fileDeleteFlag);
        result.put("thumbDeleteFlag", thumbDeleteFlag);

        return result;
    }

    private void validateFile(MultipartFile file, Set<String> allowedExts, long maxSize) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            throw new IllegalArgumentException("올바르지 않은 파일명입니다.");
        }

        String ext = getFileExtension(originalName);
        if (!allowedExts.contains(ext)) {
            throw new IllegalArgumentException("허용되지 않은 확장자입니다: " + ext);
        }

        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("파일 크기가 제한을 초과했습니다.");
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) return "";
        return fileName.substring(dotIndex + 1).toLowerCase();
    }

    private String saveFile(MultipartFile file) throws IOException {
        String oFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String sFileName = uuid + "." + getFileExtension(oFileName);
        File saveFile = Paths.get(uploadPath, sFileName).toFile();

        file.transferTo(saveFile);
        return sFileName;
    }
}
