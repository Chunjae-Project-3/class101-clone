package net.fullstack.class101clone.dto.ffmpeg;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FFprobeResult {
    private int width;
    private int height;
    private double fps;
    private boolean hasAudio;

    public VideoStream toVideoStream() {
        return new VideoStream(width, height, fps);
    }

    public AudioStream toAudioStream() {
        return new AudioStream(hasAudio);
    }
}