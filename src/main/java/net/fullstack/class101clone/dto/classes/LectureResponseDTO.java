package net.fullstack.class101clone.dto.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fullstack.class101clone.dto.ClassDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureResponseDTO {
    private ClassDTO classInfo;
    private List<SectionDTO> curriculum;
}
