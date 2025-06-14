package net.fullstack.class101clone.service.file;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.util.FileUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class VideoConvertService {

    private final ExecutorService executorService;

    private final FileUtil fileUtil;

    public void convert(String fileName) {
        executorService.submit(() -> {
            fileUtil.convertVideo(fileName);
        });
    }
}
