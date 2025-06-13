package net.fullstack.class101clone.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.dto.file.FFprobeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class FFprobeUtil {

    @Value("${net.fullstack.ffmpeg.probe}")
    private String ffprobePath;

    private final ObjectMapper objectMapper;

    public FFprobeResult analyze(Path path) throws IOException {
        try {
            Process process = convert(path);
            String json = readJson(process);

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("FFprobe process exited with code " + exitCode);
            }

            return parseFFprobeResult(json);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("FFprobe process was interrupted", e);
        } catch (Exception e) {
            throw new RuntimeException("FFprobe process failed", e);
        }
    }

    private String readJson(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            return output.toString();
        }
    }

    private Process convert(Path path) throws IOException {
        List<String> command = List.of(
          ffprobePath,
          "-v", "error",
          "-show_entries", "stream=codec_type,width,height,r_frame_rate, duration",
          "-of", "json",
          path.toString()
        );

        return new ProcessBuilder(command)
                .redirectErrorStream(true)
                .start();
    }

    private FFprobeResult parseFFprobeResult(String json) throws IOException {
        JsonNode root = objectMapper.readTree(json);
        JsonNode streams = root.path("streams");

        JsonNode videoStream = null;
        boolean hasAudio = false;

        for (JsonNode stream : streams) {
            String codecType = stream.path("codec_type").asText();
            switch (codecType) {
                case "video" -> videoStream = stream;
                case "audio" -> hasAudio = true;
            }
        }

        if (videoStream == null) {
            throw new IOException("No video stream found");
        }

        int width = videoStream.path("width").asInt();
        int height = videoStream.path("height").asInt();
        double fps = calculateFps(videoStream.path("r_frame_rate").asText());

        return new FFprobeResult(width, height, fps, hasAudio);
    }

    private double calculateFps(String frameRate) {
        String[] parts = frameRate.split("/");
        if (parts.length == 2) {
            double numerator = Double.parseDouble(parts[0]);
            double denominator = Double.parseDouble(parts[1]);
            return numerator / denominator;
        }
        return Double.parseDouble(frameRate);
    }
}
