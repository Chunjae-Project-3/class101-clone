package net.fullstack.class101clone.controller.classes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.dto.classes.ClassResponseDTO;
import net.fullstack.class101clone.dto.classes.LectureResponseDTO;
import net.fullstack.class101clone.dto.classes.SectionDTO;
import net.fullstack.class101clone.service.classes.ClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lecture")
@Tag(name = "강의 API")
public class LectureApiController {

    private final ClassService classService;

    @GetMapping("/curriculum/{classId}")
    @Operation(summary = "강의 목록(커리큘럼) 조회 (썸네일 이미지 미포함)")
    public ResponseEntity<LectureResponseDTO> getLectureListByClassIdx(
            @PathVariable int classId,
            HttpSession session
    ) {
        String userId = (String) session.getAttribute("loginId");
        ClassDTO classInfo = classService.getClassByIdx(classId);
        List<SectionDTO> curriculum = classService.getCurriculum(classId, userId, true);

        LectureResponseDTO responseDTO = LectureResponseDTO.builder()
                .classInfo(classInfo)
                .curriculum(curriculum)
                .build();

        return ResponseEntity.ok().body(responseDTO);
    }
}
