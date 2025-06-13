package net.fullstack.class101clone.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.fullstack.class101clone.dto.file.AudioStream;
import net.fullstack.class101clone.dto.file.FFprobeResult;
import net.fullstack.class101clone.dto.file.VideoStream;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FFmpegCommand {
    private final String ffmpegPath;

    private final Path videoPath;
    private final Path originalVideoPath;

    private final VideoStream videoStream;
    private final AudioStream audioStream;

    public static FFmpegCommand command(String ffmpegPath, Path videoPath, Path originalVideoPath, FFprobeResult fFprobeResult) {
        return new FFmpegCommand(ffmpegPath, videoPath, originalVideoPath, fFprobeResult.toVideoStream(), fFprobeResult.toAudioStream());
    }

    public List<String> buildCommand() {
        List<String> command = new ArrayList<>();

        // 입력 영상 설정
        command.addAll(List.of(ffmpegPath, "-i", originalVideoPath.toString()));
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
                "-hls_segment_filename", videoPath + "/stream_%v/segment_%03d.ts",  // 세그먼트 파일명
                "-master_pl_name", "master.m3u8",                                   // 마스터 플레이리스트
                videoPath + "/stream_%v/playlist.m3u8"                              // 각 해상도별 플레이리스트
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
    }

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

    private List<String> buildBitrates() {
        List<String> bitrates = new ArrayList<>();
        List<String> qualities = videoStream.getAvailableQualities();

        for (int i = 0; i < qualities.size(); i++) {
            bitrates.add("-b:v:" + i);
            bitrates.add(VideoStream.BITRATE.get(qualities.get(i)));
        }

        return bitrates;
    }

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