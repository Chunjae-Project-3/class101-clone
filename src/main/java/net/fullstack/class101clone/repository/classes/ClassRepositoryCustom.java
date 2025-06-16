package net.fullstack.class101clone.repository.classes;

import net.fullstack.class101clone.domain.CreatorEntity;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.dto.SubCategoryDTO;
import net.fullstack.class101clone.dto.classes.LectureDTO;
import net.fullstack.class101clone.dto.classes.LectureHistoryResponseDTO;
import net.fullstack.class101clone.dto.classes.SectionDTO;
import net.fullstack.class101clone.dto.file.FileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ClassRepositoryCustom {
    public ClassDTO getClassByIdx(int classIdx);

    public List<ClassDTO> getClasses(String category, String userId);
    public List<ClassDTO> getTopLikedClasses(int limit);
    public List<ClassDTO> getRecentClasses(int limit);
    public List<ClassDTO> getClassesByCategoryIdx(Integer categoryIdx);
    public Page<ClassDTO> getClassesByCategoryIdx(Integer categoryIdx, Pageable pageable, String sort);

    public List<CreatorEntity> getCreatorsByCategoryIdx(Integer categoryIdx);

    public Map<String, Object> searchClassesAndCreators(String keyword, Pageable pageable, String sort, String userId);
    public Page<ClassDTO> getPagedClassesByCategoryAndSub(Integer categoryIdx, Integer subCategoryIdx, Pageable pageable, String sort);

    public List<ClassDTO> getWishListByUserId(String userId);

    public List<SubCategoryDTO> getSubCategoriesByCategory(Integer categoryIdx);

    public List<SectionDTO> findSectionsByClassIdx(Integer classIdx);
    public List<SectionDTO> getSectionsWithFilesByClassIdx(Integer classIdx);
    public List<FileDTO> findFilesByClassIdx(Integer classIdx);

    public List<LectureDTO> findLecturesBySectionIdx(Integer sectionIdx);
    public List<LectureDTO> findLecturesBySectionIdx(String userId, Integer sectionIdx);

    public List<LectureHistoryResponseDTO> findLectureHistory(String userId);
}
