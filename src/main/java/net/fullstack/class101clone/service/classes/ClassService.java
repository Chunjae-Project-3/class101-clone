package net.fullstack.class101clone.service.classes;

import net.fullstack.class101clone.dto.classes.CurriculumDTO;

public interface ClassService {
    public CurriculumDTO getCurriculum(int classIdx);
}