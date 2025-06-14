package net.fullstack.class101clone.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import net.fullstack.class101clone.type.FileType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Log4j2
@Component
@RequiredArgsConstructor
public class FileUtil {

    private final FilePathUtil filePathUtil;
    private final FFmpegUtil ffmpegUtil;

    // 이미지 확장자 & 최대 용량
    private static final Set<String> IMAGE_EXTS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final long MAX_IMAGE_SIZE = 1 * 1024 * 1024; // 1MB

    // 비디오 확장자 & 최대 용량
    private static final Set<String> VIDEO_EXTS = Set.of("mp4", "mov", "avi", "mkv");
    private static final long MAX_VIDEO_SIZE = 200 * 1024 * 1024; // 200MB

    public String uploadImage(MultipartFile file) throws IOException {
        validateFile(file, IMAGE_EXTS, MAX_IMAGE_SIZE);
        return saveFile(file, FileType.IMAGE);
    }

    public String uploadVideo(MultipartFile file) throws IOException {
        validateFile(file, VIDEO_EXTS, MAX_VIDEO_SIZE);
        return saveFile(file, FileType.VIDEO);
    }

    public String generateThumbnail(String fileName) throws IOException {
        Path thumbPath = filePathUtil.getFullPath(FileType.THUMBNAIL);

        File directory = thumbPath.toFile();
        if (!directory.exists()) directory.mkdirs();

        String thumbFileName = "s_" + fileName;
        File thumbFile = new File(directory, thumbFileName);
        File saveFile =  filePathUtil.getFullPath(FileType.IMAGE, fileName).toFile();
        Thumbnailator.createThumbnail(saveFile, thumbFile, 200, 200);

        return thumbFileName;
    }

    public void convertVideo(String fileName) {
        Path videoPath = filePathUtil.getFullPath(FileType.VIDEO, fileName);
        Path hlsPath = filePathUtil.getFullPath(FileType.VIDEO_HLS, fileName);

        File directory = hlsPath.toFile();
        if (!directory.exists()) directory.mkdirs();

        try {
            Process process = ffmpegUtil.convert(hlsPath, videoPath);

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
                file = filePathUtil.getFullPath(FileType.IMAGE, fileName).toFile();
                File thumbFile = filePathUtil.getFullPath(FileType.THUMBNAIL, "s_" + fileName).toFile();
                if (thumbFile.exists()) thumbDeleteFlag = thumbFile.delete();
            }
            case VIDEO -> {
                file = filePathUtil.getFullPath(FileType.VIDEO, fileName).toFile();

                Path hlsDirectory = filePathUtil.getFullPath(FileType.VIDEO_HLS, fileName);
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

    public Resource getFile(String path, FileType type) {
        File file = filePathUtil.getFullPath(type, path).toFile();
        if (!file.exists()) return null;
        return new FileSystemResource(file);
    }

    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("파일명이 비어 있거나 null입니다.");
        }

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            throw new IllegalArgumentException("파일 확장자가 존재하지 않거나 올바르지 않은 파일명입니다: " + fileName);
        }

        return fileName.substring(dotIndex + 1).toLowerCase();
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

    private String saveFile(MultipartFile file, FileType type) throws IOException {
        Path path = filePathUtil.getFullPath(type);

        File directory = path.toFile();
        if (!directory.exists()) directory.mkdirs();

        String oFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String sFileName = uuid + "." + getFileExtension(oFileName);

        File saveFile = new File(directory, sFileName);
        file.transferTo(saveFile);
        return sFileName;
    }

}
