package net.fullstack.class101clone.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.type.FileType;
import net.fullstack.class101clone.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
@Tag(name = "동영상 스트리밍 API")
public class VideoController {

    private final FileUtil fileUtil;

    @GetMapping("/{videoId}/master.m3u8")
    public ResponseEntity<Resource> getVideo(@PathVariable String videoId) {
        String path = Paths.get(videoId, "master.m3u8").toString();
        Resource resource = fileUtil.getFile(path, FileType.VIDEO_HLS);
        return createResponseEntity(resource);
    }

    @GetMapping("/{videoId}/{stream}/{segment}")
    public ResponseEntity<Resource> getPlaylist(
            @PathVariable String videoId,
            @PathVariable String stream,
            @PathVariable String segment
    ) {
        String path = Paths.get(videoId, stream, segment).toString();
        Resource resource = fileUtil.getFile(path, FileType.VIDEO_HLS);
        return createResponseEntity(resource);
    }

    private ResponseEntity<Resource> createResponseEntity(Resource resource) {
        if (resource == null) return ResponseEntity.notFound().build();

        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
