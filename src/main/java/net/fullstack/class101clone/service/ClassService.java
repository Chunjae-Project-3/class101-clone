package net.fullstack.class101clone.service;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.domain.LectureEntity;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.repository.classes.ClassRepository;
import net.fullstack.class101clone.repository.file.FileRepository;
import net.fullstack.class101clone.repository.LectureRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClassService {
    private final ClassRepository classRepository;
    private final FileRepository fileRepository;
    private final LectureRepository lectureRepository;

    public List<ClassDTO> getClasses(String category, String userId) {
        return classRepository.getClasses(category, userId);
    }

    public List<ClassDTO> getTopLikedClasses(int limit) {
        return classRepository.getTopLikedClasses(limit);
    }

    public List<ClassDTO> getRecentClasses(int limit) {
        return classRepository.getRecentClasses(limit);
    }

    public List<ClassDTO> getClassesByCategoryIdx(Integer categoryIdx) {
        return classRepository.getClassesByCategoryIdx(categoryIdx);
    }

    public Page<ClassDTO> getPagedClassesByCategoryIdx(Integer categoryIdx, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size);
        return classRepository.getPagedClassesByCategoryIdx(categoryIdx, pageable, sort);
    }

    public List<Map<String, String>> getCreatorsByCategory(Integer categoryIdx) {
        return classRepository.getCreatorListByCategoryIdx(categoryIdx);
    }

    public ClassDTO getClassDetail(Integer classId) {
        ClassEntity entity = classRepository.findWithCreatorByClassIdx(classId)
                .orElseThrow(() -> new RuntimeException("클래스를 찾을 수 없습니다."));

        ClassDTO dto = new ClassDTO();
        dto.setClassTitle(entity.getClassTitle());
        dto.setClassDescription(entity.getClassDescription());
        dto.setCreatorName(entity.getCreator().getCreatorName());
        dto.setCreatorDescription(entity.getCreator().getCreatorDescription());
        dto.setCreatorProfileImg(entity.getCreator().getCreatorProfileImg());
        dto.setThumbnailUrl(entity.getClassThumbnailImg().getFilePath());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }

    public List<String> getClassImageList(Integer classId) {
        return fileRepository.findImagePathsByClassId(classId);
    }

    public Map<String, List<Map<String, String>>> getLectureCurriculum(Integer classId) {
        List<LectureEntity> lectures = lectureRepository.findAllByLectureRef_ClassIdx(classId);

        return lectures.stream().collect(Collectors.groupingBy(
                lec -> lec.getLectureSection() != null ? lec.getLectureSection() : "기타",
                LinkedHashMap::new,
                Collectors.mapping(
                        lec -> {
                            String duration = formatDuration(lec.getLectureDurationSec());
                            String thumbnailUrl = (lec.getLectureThumbnail() != null)
                                    ? lec.getLectureThumbnail().getFilePath()
                                    : "/images/default-image.png";

                            return Map.of(
                                    "title", lec.getLectureTitle(),
                                    "duration", duration,
                                    "thumbnailUrl", thumbnailUrl
                            );
                        },
                        Collectors.toList()
                )
        ));
    }

    // 초 단위 -> "HH:mm:ss" 또는 "mm:ss"로 변환
    private String formatDuration(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%02d:%02d", minutes, secs);
        }
    }

    public Map<String, Object> searchAll(String keyword, Pageable pageable, String sort, String userId) {
        return classRepository.searchClassesAndCreators(keyword, pageable, sort, userId);
    }

    public List<ClassDTO> getWishlist(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return classRepository.getWishListByUserId(userId);
    }
}
