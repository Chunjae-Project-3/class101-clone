package net.fullstack.class101clone.service;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.LectureEntity;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.repository.ClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassService {
    private final ClassRepository classRepository;

    public List<ClassDTO> getClasses(String category) {
        return classRepository.getClasses(category);
    }

    public ClassDTO getClassDetail(Integer classId) {
        return classRepository.getClassDetailById(classId);
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
                        lec -> Map.of(
                                "title", lec.getLectureTitle(),
                                "duration", formatDuration(lec.getLectureDurationSec())
                        ),
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

}
