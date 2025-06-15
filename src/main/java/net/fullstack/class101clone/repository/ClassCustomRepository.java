package net.fullstack.class101clone.repository;

import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.dto.classes.LectureDTO;
import net.fullstack.class101clone.dto.classes.SectionDTO;
import net.fullstack.class101clone.dto.file.FileDTO;

import java.util.List;

public interface ClassCustomRepository {
    public List<SectionDTO> getSectionsByClassIdx(int classIdx);
    public List<SectionDTO> getSectionsWithFilesByClassIdx(int classIdx);
    public List<LectureDTO> getLecturesBySectionIdx(int sectionIdx);
}
