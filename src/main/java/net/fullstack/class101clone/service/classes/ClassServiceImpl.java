package net.fullstack.class101clone.service.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.dto.classes.CurriculumDTO;
import net.fullstack.class101clone.dto.classes.LectureDTO;
import net.fullstack.class101clone.dto.classes.SectionDTO;
import net.fullstack.class101clone.dto.file.FileDTO;
import net.fullstack.class101clone.exception.NotFoundException;
import net.fullstack.class101clone.repository.ClassRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ModelMapper modelMapper;
    private final ClassRepository classRepository;

    @Override
    public CurriculumDTO getCurriculum(int classIdx) {
        List<SectionDTO> sectionList = classRepository.getSectionsByClassIdx(classIdx).stream()
                .map(section -> {
                    List<LectureDTO> lectureList = classRepository.getLecturesBySectionIdx(section.getSectionIdx());
                    section.setLectureList(lectureList);
                    return section;
                })
                .collect(Collectors.toList());

        return CurriculumDTO.builder()
                .classIdx(classIdx)
                .sectionList(sectionList)
                .build();
    }
}
