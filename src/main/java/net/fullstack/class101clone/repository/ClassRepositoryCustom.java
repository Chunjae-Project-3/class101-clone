package net.fullstack.class101clone.repository;

import net.fullstack.class101clone.dto.ClassDTO;

import java.util.List;

public interface ClassRepositoryCustom {
    List<ClassDTO> getClasses(String category);
    ClassDTO getClassDetailById(Integer classId);
}
