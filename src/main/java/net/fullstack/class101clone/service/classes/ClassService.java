package net.fullstack.class101clone.service.classes;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.CreatorEntity;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.dto.CreatorDTO;
import net.fullstack.class101clone.dto.SubCategoryDTO;
import net.fullstack.class101clone.dto.classes.LectureDTO;
import net.fullstack.class101clone.dto.classes.LectureHistoryResponseDTO;
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
        List<ClassDTO> classList = classRepository.getClasses(category, userId);
        if (userId != null) {
            for (ClassDTO dto : classList) {
                boolean liked = classLikeRepository.existsByClassLikeUser_UserIdAndClassLikeRef_ClassIdx(userId, dto.getClassIdx());
                dto.setLiked(liked);
            }
        }
        return classList;
    }

    public List<ClassDTO> getTopLikedClasses(int limit, String userId) {
        List<ClassDTO> list = classRepository.getTopLikedClasses(limit);
        if (userId != null) {
            for (ClassDTO dto : list) {
                boolean liked = classLikeRepository.existsByClassLikeUser_UserIdAndClassLikeRef_ClassIdx(userId, dto.getClassIdx());
                dto.setLiked(liked);
            }
        }
        return list;
    }

    public List<ClassDTO> getRecentClasses(int limit, String userId) {
        List<ClassDTO> list = classRepository.getRecentClasses(limit);
        if (userId != null) {
            for (ClassDTO dto : list) {
                boolean liked = classLikeRepository.existsByClassLikeUser_UserIdAndClassLikeRef_ClassIdx(userId, dto.getClassIdx());
                dto.setLiked(liked);
            }
        }
        return list;
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
        Map<String, Object> rawResult = classRepository.searchClassesAndCreators(keyword, pageable, sort, userId);

        List<CreatorEntity> creatorEntities = (List<CreatorEntity>) rawResult.get("creators");
        List<CreatorDTO> creatorDTOs = creatorEntities.stream()
                .map(creator -> modelMapper.map(creator, CreatorDTO.class))
                .toList();

        return Map.of(
                "classes", rawResult.get("classes"),
                "creators", creatorDTOs
        );
    }

    public List<ClassDTO> getWishListByUserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return classRepository.getWishListByUserId(userId);
    }

    public List<LectureHistoryResponseDTO> getLectureHistory(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        return classRepository.findLectureHistory(userId);
    }

    public List<SubCategoryDTO> getSubCategoriesByCategory(Integer categoryIdx) {
        return classRepository.getSubCategoriesByCategory(categoryIdx);
    }

    public List<SectionDTO> getCurriculum(Integer classIdx, @Nullable String userId, boolean includeFiles) {
        List<SectionDTO> sectionList = (includeFiles
                ? classRepository.getSectionsWithFilesByClassIdx(classIdx)
                : classRepository.findSectionsByClassIdx(classIdx));

        return sectionList.stream()
                .map(section -> {
                    List<LectureDTO> lectureList;
                    if (userId != null) {
                        lectureList = classRepository.findLecturesBySectionIdx(userId, section.getSectionIdx());
                    } else {
                        lectureList = classRepository.findLecturesBySectionIdx(section.getSectionIdx());
                    }
                    section.setLectureList(lectureList);
                    return section;
                })
                .collect(Collectors.toList());
    }

    public List<String> getSectionThumbnailUrlsByClassIdx(Integer classIdx) {
        return classRepository.findFilesByClassIdx(classIdx).stream()
                .map(file -> file.getFilePath() + "/" + file.getFileName())
                .collect(Collectors.toList());
    }
}
