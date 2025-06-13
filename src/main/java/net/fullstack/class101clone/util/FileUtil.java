package net.fullstack.class101clone.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import net.fullstack.class101clone.dto.file.FileResponseDTO;
import net.fullstack.class101clone.type.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Log4j2
@Component
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class FileUtil {

    @Value("${net.fullstack.upload.path}")
    private String basePath;

    @Value("${net.fullstack.upload.image.path}")
    private String imagePath;

    @Value("${net.fullstack.upload.video.path}")
    private String videoPath;

    @Value("${net.fullstack.upload.image.thumbnail.path}")
    private String thumbnailPath;

    // 이미지 확장자 & 최대 용량
    private static final Set<String> IMAGE_EXTS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final long MAX_IMAGE_SIZE = 1 * 1024 * 1024; // 1MB

    // 비디오 확장자 & 최대 용량
    private static final Set<String> VIDEO_EXTS = Set.of("mp4", "mov", "avi", "mkv");
    private static final long MAX_VIDEO_SIZE = 200 * 1024 * 1024; // 200MB

    private final FFmpegUtil ffmpegUtil;

    public FileResponseDTO uploadImage(MultipartFile file) throws IOException {
        validateFile(file, IMAGE_EXTS, MAX_IMAGE_SIZE);

        String sFileName = saveFile(file, imagePath);
        String oFileName = file.getOriginalFilename();
        String ext = getFileExtension(oFileName);

        String thumbFileName = saveThumbnail(sFileName);
        File thumbFile = Paths.get(basePath, thumbnailPath, thumbFileName).toFile();

        return FileResponseDTO.builder()
                .fileName(sFileName)
                .fileExt(ext)
                .fileOrgName(oFileName)
                .fileSize(file.getSize())
                .filePath(imagePath)
                .imageFlag(true)
                .thumbnailFileName(thumbFileName)
                .thumbnailFileSize(thumbFile.length())
                .thumbnailFilePath(thumbnailPath)
                .build();
    }

    public FileResponseDTO uploadVideo(MultipartFile file) throws IOException {
        validateFile(file, VIDEO_EXTS, MAX_VIDEO_SIZE);

        String sFileName = saveFile(file, videoPath);
        String oFileName = file.getOriginalFilename();
        String ext = getFileExtension(oFileName);

        return FileResponseDTO.builder()
                .fileName(sFileName)
                .fileExt(ext)
                .fileOrgName(oFileName)
                .fileSize(file.getSize())
                .filePath(videoPath)
                .imageFlag(false)
                .build();
    }

    public void convertVideo(String fileName) {
        Path originalPath = Paths.get(basePath, videoPath, fileName);
        Path savePath = Paths.get(basePath, videoPath, "hls", fileName);

        File directory = savePath.toFile();
        if (!directory.exists()) directory.mkdirs();

        try {
            Process process = ffmpegUtil.convert(savePath, originalPath);

//            // video convert 실시간 상태
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    log.info("[FFmpeg][{}] {}", fileName, line);
//                }
//            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Video Convert exited with code " + exitCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Video Convert failed", e);
        }
    }

    public Map<String, Boolean> deleteFile(String fileName, FileType type) throws IOException {
        Map<String, Boolean> result = new HashMap<>();
        boolean fileDeleteFlag = false;
        boolean thumbDeleteFlag = false;
        boolean convertedDeleteFlag = false;

        File file;
        switch (type) {
            case IMAGE -> {
                file = Paths.get(basePath, imagePath, fileName).toFile();

                File thumbFile = Paths.get(basePath, thumbnailPath, "s_" + fileName).toFile();
                if (thumbFile.exists()) thumbDeleteFlag = thumbFile.delete();
            }
            case VIDEO -> {
                file = Paths.get(basePath, videoPath, fileName).toFile();

                Path hlsDirectory = Paths.get(basePath, videoPath, "hls", fileName);
                if (Files.exists(hlsDirectory)) {
                    Files.walk(hlsDirectory)
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                    convertedDeleteFlag = true;
                }
            }
            default -> throw new IllegalArgumentException("지원하지 않는 파일 유형입니다. " + type);
        }

        if (file.exists()) {
            fileDeleteFlag = file.delete();
        }

        result.put("fileDeleteFlag", fileDeleteFlag);
        result.put("thumbDeleteFlag", thumbDeleteFlag);
        result.put("convertedDeleteFlag", convertedDeleteFlag);

        return result;
    }

    public Resource getFile(String videoId, String path, FileType type) {
        Path fullPath;
        boolean isEmpty = (path == null || path.isBlank());
        switch (type) {
            case IMAGE -> fullPath = isEmpty ?
                    Paths.get(basePath, imagePath, videoId) :
                    Paths.get(basePath, imagePath, videoId, path);
            case VIDEO -> fullPath = isEmpty ?
                    Paths.get(basePath, videoPath, videoId) :
                    Paths.get(basePath, videoPath, videoId, path);
            default -> throw new IllegalArgumentException("지원하지 않는 파일 유형입니다. " + type);
        }
        if (!Files.exists(fullPath)) return null;
        return new FileSystemResource(fullPath);
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

    private String saveFile(MultipartFile file, String path) throws IOException {
        Path fullPath = Paths.get(basePath, path);

        File directory = fullPath.toFile();
        if (!directory.exists()) directory.mkdirs();

        String oFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String sFileName = uuid + "." + getFileExtension(oFileName);

        File saveFile = new File(directory, sFileName);
        file.transferTo(saveFile);
        return sFileName;
    }

    private String saveThumbnail(String sFileName) throws IOException {
        Path fullPath = Paths.get(basePath, thumbnailPath);

        File directory = fullPath.toFile();
        if (!directory.exists()) directory.mkdirs();

        String thumbFileName = "s_" + sFileName;
        File thumbFile = new File(directory, thumbFileName);
        File saveFile = Paths.get(basePath, imagePath, sFileName).toFile();
        Thumbnailator.createThumbnail(saveFile, thumbFile, 200, 200);

        return thumbFileName;
    }
}
