package net.fullstack.class101clone.dto.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fullstack.class101clone.dto.file.FileDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectionDTO {
    private int sectionIdx;
    private int sectionRefIdx;
    private String sectionTitle;
    private int sectionOrder;
    private List<String> sectionThumbnailUrls;
    private List<LectureDTO> lectureList;
}
