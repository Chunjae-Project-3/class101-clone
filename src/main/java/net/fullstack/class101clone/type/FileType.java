package net.fullstack.class101clone.type;

import java.util.Arrays;

public enum FileType {
    IMAGE("image", "/image"),
    THUMBNAIL("thumbnail", "/image/thumbnail"),
    VIDEO("video", "/video"),
    VIDEO_HLS("hls", "/video/hls");

    private final String type;
    private final String path;

    FileType(String type, String path) {
        this.type = type;
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public static FileType fromValue(String value) {
        return Arrays.stream(FileType.values())
                .filter(type -> type.type.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown file type: " + value));
    }

    public static FileType fromPath(String path) {
        return Arrays.stream(FileType.values())
                .filter(type -> type.path.equalsIgnoreCase(path))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown file path: " + path));
    }
}
