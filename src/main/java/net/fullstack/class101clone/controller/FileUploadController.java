package net.fullstack.class101clone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import net.fullstack.api.dto.upload.FileUploadDTO;
import net.fullstack.api.dto.upload.FileUploadResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Log4j2
@RestController(value = "/file")
@Tag(name = "file upload", description = "파일 업로드")
public class FileUploadController {
    @Value("${net.fullstack.upload.path}")
    private String uploadPath;

    @Operation(summary = "파일 업로드", description = "파일 업로드 api")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<FileUploadResultDTO> upload(FileUploadDTO fileUploadDTO) {
        log.info(fileUploadDTO);
        if (fileUploadDTO.getFiles() != null) {
            List<FileUploadResultDTO> fileList = new ArrayList<>();
            fileUploadDTO.getFiles().forEach(file -> {
                log.info(file.getOriginalFilename());
                String orgFileName = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String newFileName = uploadPath + File.separator + uuid + "_" + orgFileName;
                Path savePath = Paths.get(newFileName);

                // 이미지 체크 후 썸네일로 변경
                boolean isImage = file.getContentType().startsWith("image");
                String thumbFileName = "";
                try {
                    file.transferTo(savePath.toFile());
                    thumbFileName = "s_" + uuid + "_" + orgFileName;
                    File thumbFile = new File(uploadPath + File.separator + thumbFileName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                } catch (Exception e) {
                    log.error(e);
                    throw new RuntimeException(e);
                }

                fileList.add(FileUploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(orgFileName)
                        .newFileName(newFileName)
                        .imageFlag(isImage)
                        .thumbnailFileName(thumbFileName)
                        .build());
            });
            return fileList;
        }

        return null;
    }

    //    @PostMapping("/view/{fileName}")
//    public ResponseEntity<Resource> viewFileList(@PathVariable("fileName") String fileName) {
//        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
//        String resourceName = resource.getFilename();
//        String encodedFileName = URLEncoder.encode(resourceName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
//
//        HttpHeaders headers = new HttpHeaders();
//        try {
//            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resourceName + "\"");
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//        return ResponseEntity.ok().headers(headers).body(resource); // 바디에 리소스 지정해서 자동으로 파일 다운로드 되게
//    }

    @Operation(summary = "파일 다운로드", description = "파일 다운로드 api")
    @PostMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileList(@PathVariable("fileName") String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            String encodedFileName = URLEncoder.encode(resourceName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + new String(resourceName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1) +
                            "\"; filename*=UTF-8''" + encodedFileName);

            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @Operation(summary = "파일 삭제", description = "파일 삭제 api")
    @DeleteMapping("/delete/{fileName}")
    public Map<String, Boolean> deleteFile(@PathVariable("fileName") String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        String resourceName = resource.getFilename();
        Map<String, Boolean> resultMap = new HashMap<>();
        boolean fileDeleteFlag = false;
        boolean thumbnailDeleteFlag = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            fileDeleteFlag = resource.getFile().delete();
            if (contentType.startsWith("image")) {
                File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                if (thumbnailFile.exists()) {
                    thumbnailDeleteFlag = thumbnailFile.delete();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        resultMap.put("fileDeleteFlag", fileDeleteFlag);
        resultMap.put("thumbnailDeleteFlag", thumbnailDeleteFlag);

        return resultMap;
    }
}
