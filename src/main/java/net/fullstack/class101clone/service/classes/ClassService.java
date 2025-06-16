package net.fullstack.class101clone.service.classes;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.domain.CreatorEntity;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.dto.CreatorDTO;
import net.fullstack.class101clone.dto.SubCategoryDTO;
import net.fullstack.class101clone.dto.classes.CurriculumDTO;
import net.fullstack.class101clone.dto.classes.LectureDTO;
import net.fullstack.class101clone.dto.classes.SectionDTO;
import net.fullstack.class101clone.repository.classes.ClassLikeRepository;
import net.fullstack.class101clone.repository.classes.ClassRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClassService {

    private final ModelMapper modelMapper;
    private final ClassRepository classRepository;
    private final ClassLikeRepository classLikeRepository;

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

    public Page<ClassDTO> getClassesByCategoryIdx(Integer categoryIdx, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size);
        return classRepository.getClassesByCategoryIdx(categoryIdx, pageable, sort);
    }

    public Page<ClassDTO> getPagedClassesByCategoryAndSub(Integer categoryIdx, Integer subCategoryIdx, int page, int size, String sort, String userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClassDTO> resultPage = classRepository.getPagedClassesByCategoryAndSub(categoryIdx, subCategoryIdx, pageable, sort);

        if (userId != null) {
            for (ClassDTO dto : resultPage.getContent()) {
                boolean liked = classLikeRepository.existsByClassLikeUser_UserIdAndClassLikeRef_ClassIdx(userId, dto.getClassIdx());
                dto.setLiked(liked);
            }
        }

        return resultPage;
    }

    public List<CreatorDTO> getCreatorsByCategoryIdx(Integer categoryIdx) {
        List<CreatorEntity> entity = classRepository.getCreatorsByCategoryIdx(categoryIdx);
        return entity.stream()
                .map(creator -> modelMapper.map(creator, CreatorDTO.class))
                .collect(Collectors.toList());
    }

    public ClassDTO getClassByIdx(Integer classIdx) {
        return classRepository.getClassByIdx(classIdx);
    }

    public Map<String, Object> searchClassesAndCreators(String keyword, Pageable pageable, String sort, String userId) {
        return classRepository.searchClassesAndCreators(keyword, pageable, sort, userId);
    }

    public List<ClassDTO> getWishListByUserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return classRepository.getWishListByUserId(userId);
    }

    public List<LectureDTO> getLectureHistory(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        return classRepository.getLectureHistory(userId);
    }

    public List<SubCategoryDTO> getSubCategoriesByCategory(Integer categoryIdx) {
        return classRepository.getSubCategoriesByCategory(categoryIdx);
    }

    public CurriculumDTO getCurriculum(Integer classIdx, @Nullable String userId, boolean includeFiles) {
        ClassDTO classDTO = classRepository.getClassByIdx(classIdx);

        List<SectionDTO> sectionList = (includeFiles
                ? classRepository.getSectionsWithFilesByClassIdx(classIdx)
                : classRepository.getSectionsByClassIdx(classIdx));

        sectionList.stream()
                .map(section -> {
                    List<LectureDTO> lectureList;
                    if (userId != null) {
                        lectureList = classRepository.getLecturesBySectionIdx(userId, section.getSectionIdx());
                    } else {
                        lectureList = classRepository.getLecturesBySectionIdx(section.getSectionIdx());
                    }
                    section.setLectureList(lectureList);
                    return section;
                })
                .collect(Collectors.toList());

        return CurriculumDTO.builder()
                .classInfo(classDTO)
                .sectionList(sectionList)
                .build();
    }
}
