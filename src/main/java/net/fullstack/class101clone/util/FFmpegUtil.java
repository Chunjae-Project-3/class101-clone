package net.fullstack.class101clone.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.dto.file.FFprobeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class FFmpegUtil {

    @Value("${net.fullstack.ffmpeg.mpeg}")
    private String ffmpegPath;

    private final FFprobeUtil ffprobeUtil;

    public Process convert(Path videoPath, Path originalVideoPath) throws IOException {
        FFprobeResult analyze = ffprobeUtil.analyze(originalVideoPath);

        List<String> command = FFmpegCommand.command(ffmpegPath, videoPath, originalVideoPath, analyze)
                .buildCommand();

        return new ProcessBuilder(command)
                .redirectErrorStream(true)
                .start();
    }
}
