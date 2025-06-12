package net.fullstack.class101clone.repository.classes;

import net.fullstack.class101clone.dto.ClassDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassRepositoryCustom {
    List<ClassDTO> getClasses(String category, String userId);

    ClassDTO getClassDetailById(Integer classId);

    Page<ClassDTO> getPagedClassesByCategoryIdx(Integer categoryIdx, Pageable pageable);

    List<ClassDTO> getClassesByCategoryIdx(Integer categoryIdx);
}
