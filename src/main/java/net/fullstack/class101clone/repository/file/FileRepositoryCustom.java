package net.fullstack.class101clone.repository.file;

import java.util.List;

public interface FileRepositoryCustom {
    List<String> findImagePathsByClassId(Integer classId);

}
