package net.fullstack.class101clone.repository.classes;

import net.fullstack.class101clone.domain.CreatorEntity;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.dto.SubCategoryDTO;
import net.fullstack.class101clone.dto.classes.LectureDTO;
import net.fullstack.class101clone.dto.classes.SectionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ClassRepositoryCustom {
    public List<ClassDTO> getClasses(String category, String userId);
    public List<ClassDTO> getTopLikedClasses(int limit);
    public List<ClassDTO> getRecentClasses(int limit);
    public List<ClassDTO> getClassesByCategoryIdx(Integer categoryIdx);
    public Page<ClassDTO> getClassesByCategoryIdx(Integer categoryIdx, Pageable pageable, String sort);

    public List<CreatorEntity> getCreatorsByCategoryIdx(Integer categoryIdx);

    public ClassDTO getClassByIdx(int classIdx);

    public Map<String, Object> searchClassesAndCreators(String keyword, Pageable pageable, String sort, String userId);
    public Page<ClassDTO> getPagedClassesByCategoryAndSub(Integer categoryIdx, Integer subCategoryIdx, Pageable pageable, String sort);

    public List<ClassDTO> getWishListByUserId(String userId);

    public List<SubCategoryDTO> getSubCategoriesByCategory(Integer categoryIdx);

    public List<SectionDTO> getSectionsByClassIdx(Integer classIdx);
    public List<SectionDTO> getSectionsWithFilesByClassIdx(Integer classIdx);

    public List<LectureDTO> getLecturesBySectionIdx(Integer sectionIdx);
    public List<LectureDTO> getLecturesBySectionIdx(String userId, Integer sectionIdx);

    public List<LectureDTO> getLectureHistory(String userId);
}
