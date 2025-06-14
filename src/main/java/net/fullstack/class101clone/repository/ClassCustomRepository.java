package net.fullstack.class101clone.repository;

import net.fullstack.class101clone.domain.ClassEntity;

public interface ClassCustomRepository {
    public ClassEntity getCurriculumByClassIdx(int classIdx);
}
