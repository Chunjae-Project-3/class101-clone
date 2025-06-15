package net.fullstack.class101clone.repository.classes;

import net.fullstack.class101clone.dto.ClassDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ClassRepositoryCustom {
    List<ClassDTO> getClasses(String category, String userId);

    List<ClassDTO> getTopLikedClasses(int limit);

    List<ClassDTO> getRecentClasses(int limit);

    ClassDTO getClassDetailById(Integer classId);

    Page<ClassDTO> getPagedClassesByCategoryIdx(Integer categoryIdx, Pageable pageable, String sort);

    List<ClassDTO> getClassesByCategoryIdx(Integer categoryIdx);

    List<Map<String, String>> getCreatorListByCategoryIdx(Integer categoryIdx);

    Map<String, Object> searchClassesAndCreators(String keyword, Pageable pageable, String sort, String userId);

    Page<ClassDTO> getPagedClassesByCategoryAndSub(Integer categoryIdx, Integer subCategoryIdx, Pageable pageable, String sort);

}