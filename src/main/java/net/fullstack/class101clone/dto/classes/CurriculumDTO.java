package net.fullstack.class101clone.dto.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.dto.file.FileDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurriculumDTO {
    private int classIdx;
    private List<SectionDTO> sectionList;
}
