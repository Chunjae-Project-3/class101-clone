package net.fullstack.class101clone.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.fullstack.class101clone.dto.ffmpeg.AudioStream;
import net.fullstack.class101clone.dto.ffmpeg.FFprobeResult;
import net.fullstack.class101clone.dto.ffmpeg.VideoStream;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FFmpegCommand {
    private final String ffmpegPath;

    private final Path savePath;
    private final Path videoPath;

    private final VideoStream videoStream;
    private final AudioStream audioStream;

    public static FFmpegCommand command(String ffmpegPath, Path savePath, Path videoPath, FFprobeResult ffprobeResult) {
        return new FFmpegCommand(ffmpegPath, savePath, videoPath, ffprobeResult.toVideoStream(), ffprobeResult.toAudioStream());
    }

    public List<String> buildCommand() {
        List<String> command = new ArrayList<>();

        // 입력 영상 설정
        // command.addAll(List.of(ffmpegPath, "-i", videoPath.toString()));
        command.addAll(List.of(ffmpegPath, "-loglevel", "info", "-i", videoPath.toString()));
        // 영상 해상도별 분할 필터 설정
        command.addAll(List.of("-filter_complex", buildFilterComplex()));
        // 비디오/오디오 스트림 매핑
        command.addAll(buildMaps());
        // 비디오 인코딩 설정
        command.addAll(List.of("-c:v", "libx264"));
        // 오디오 인코딩 설정
        if (audioStream.isExists()) {
            command.addAll(List.of("-c:a", "aac"));
        }
        // 각 화질에 대한 비디오 비트레이트 설정
        command.addAll(buildBitrates());
        // HLS 스트리밍 설정
        command.addAll(List.of(
                "-var_stream_map", buildVarStreamMap(),
                "-f", "hls",                                                        // 출력 포맷
                "-hls_time", "10",                                                  // 세그먼트 길이 (초)
                "-hls_list_size", "0",                                              // 전체 세그먼트 목록 포함
                "-hls_segment_type", "mpegts",                                      // TS 형식으로 분할
                "-hls_segment_filename", savePath + "/stream_%v/segment_%03d.ts",  // 세그먼트 파일명
                "-master_pl_name", "master.m3u8",                                   // 마스터 플레이리스트
                savePath + "/stream_%v/playlist.m3u8"                              // 각 해상도별 플레이리스트
        ));

        return command;
    }

    private String buildFilterComplex() {
        List<String> scales = videoStream.getScales();

        String splitCount = String.format("[0:v]split=%d", scales.size());
        String outputs = scales.stream()
                .map(s -> String.format("[v%d]", scales.indexOf(s) + 1))
                .collect(Collectors.joining(""));

        return splitCount + outputs + ";" + String.join(";", scales);
        // 최종 결과 예:
        // "[0:v]split=3[v1][v2][v3];[v1]scale=1280:720[v720p];[v2]scale=854:480[v480p];[v3]scale=640:360[v360p]"
    }

    // 비디오/오디오를 여러 품질로 출력하기 위해 각각의 스트림 매핑
    private List<String> buildMaps() {
        List<String> maps = new ArrayList<>();
        List<String> qualities = videoStream.getAvailableQualities();

        for (String quality : qualities) {
            maps.add("-map");
            maps.add(String.format("[v%sp]", quality));
            if (audioStream.isExists()) {
                maps.add("-map"); maps.add("0:a");
            }
        }
        return maps;
    }

    // 각 해상도별 비트레이 설정
    private List<String> buildBitrates() {
        List<String> bitrates = new ArrayList<>();
        List<String> qualities = videoStream.getAvailableQualities();

        for (int i = 0; i < qualities.size(); i++) {
            bitrates.add("-b:v:" + i);
            bitrates.add(VideoStream.BITRATE.get(qualities.get(i)));
        }

        return bitrates;
    }

    // 각 해상도에 대응되는 비디오/오디오 스트림을 묶어 이름 포맷
    // -var_stream_map "v:0,a:0,name:720p v:1,a:1,name:480p v:2,a:2,name:360p" => playlist_720p.m3u8 등과 연결
    private String buildVarStreamMap() {
        List<String> streamMaps = new ArrayList<>();
        List<String> qualities = videoStream.getAvailableQualities();

        for (int i = 0; i < qualities.size(); i++) {
            String quality = qualities.get(i);
            streamMaps.add(audioStream.isExists()
                    ? String.format("v:%d,a:%d,name:%sp", i, i, quality)
                    : String.format("v:%d,name:%sp", i, quality));
        }

        return String.join(" ", streamMaps);
    }
}