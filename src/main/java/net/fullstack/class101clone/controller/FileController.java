package net.fullstack.class101clone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.dto.FileResponseDTO;
import net.fullstack.class101clone.service.FileServiceImpl;
import net.fullstack.class101clone.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
@Tag(name = "파일 API")
public class FileController {

    private final FileUtil fileUtil;
    private final FileServiceImpl fileService;

    @Operation(summary = "파일 업로드")
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = FileResponseDTO.class))
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "파일 유형 ('image' 또는 'video')")
            @RequestParam("type") String type
    ) {
        try {
            // 1. 파일 저장
            FileResponseDTO fileResponseDTO;
            if (type.equalsIgnoreCase("image")) {
                fileResponseDTO = fileUtil.uploadImage(file);
            } else if (type.equalsIgnoreCase("video")) {
                fileResponseDTO = fileUtil.uploadVideo(file);
            } else {
                return ResponseEntity.badRequest().body("type 파라미터는 'image' 또는 'video'만 허용됩니다.");
            }

            // 2. DB 저장
            fileService.insertFile(fileResponseDTO);

            return ResponseEntity.ok().body(fileResponseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Operation(summary = "파일 삭제")
    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            fileUtil.deleteFile(fileName);
            fileService.deleteFileByName(fileName);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        return ResponseEntity.ok().body("File deleted successfully.");
    }
}
