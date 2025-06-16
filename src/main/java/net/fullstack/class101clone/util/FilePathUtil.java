package net.fullstack.class101clone.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.type.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Component
@RequiredArgsConstructor
public class FilePathUtil {
    @Value("${net.fullstack.upload.path}")
    private String basePath;

    @Value("${net.fullstack.upload.relative}")
    private String relativePath;

    public Path getFullPath(FileType type) {
        return Paths.get(basePath, type.getPath());
    }

    public Path getFullPath(FileType type, String path) {
        return Paths.get(basePath, type.getPath(), path);
    }

    public String getRelativePath(FileType type) {
        return relativePath + type.getPath();
    }
}
