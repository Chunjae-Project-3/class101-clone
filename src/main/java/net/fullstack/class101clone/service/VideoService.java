package net.fullstack.class101clone.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.exception.NotFoundException;
import net.fullstack.class101clone.repository.file.FileRepository;
import net.fullstack.class101clone.type.FileType;
import net.fullstack.class101clone.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class VideoService {

    private final FileUtil fileUtil;
    private final FileRepository fileRepository;

    public Resource getVideo(int videoId, String ... path) {
        return fileRepository.findById(videoId)
                .map(video -> {
                    String fileName = video.getFileName();
                    String fullPath = Paths.get(fileName, path).normalize().toString();
                    return fileUtil.getFile(fullPath, FileType.VIDEO_HLS);
                })
                .orElseThrow(() -> new NotFoundException("Video not found. id: " + videoId));
    }
}
