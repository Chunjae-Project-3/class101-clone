package net.fullstack.class101clone.controller.classes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.dto.classes.*;
import net.fullstack.class101clone.service.classes.ClassService;
import net.fullstack.class101clone.service.classes.LectureHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lecture")
@Tag(name = "강의 API")
public class LectureApiController {

    private final ClassService classService;
    private final LectureHistoryService lectureHistoryService;

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

    @GetMapping("/history")
    @Operation(summary = "영상 시청 내역 조회")
    public ResponseEntity<LectureHistoryDTO> getLectureHistory(
            @RequestParam("lectureIdx") int lectureIdx,
            HttpSession session
    ) {
        String userId = (String) session.getAttribute("loginId");

        LectureHistoryDTO history = null;

        // 비회원: 세션에서 조회
        if (userId == null) {
            List<LectureHistoryDTO> guestHistory = (List<LectureHistoryDTO>) session.getAttribute("guestHistory");
            if (guestHistory != null) {
                history = guestHistory.stream()
                        .filter(dto -> dto.getLectureIdx() == lectureIdx)
                        .findFirst()
                        .orElse(null);
            }
            return ResponseEntity.ok().body(history);
        }

        // 회원: DB에서 조회
        history = lectureHistoryService.getLectureHistoryByLectureId(userId, lectureIdx);
        return ResponseEntity.ok().body(history);

    }

    @PostMapping("/history")
    @Operation(summary = "영상 시청 내역 저장")
    public ResponseEntity<?> saveLectureHistory(
            @RequestBody LectureHistoryDTO reqDTO,
            HttpSession session
    ) {
        String userId = (String) session.getAttribute("loginId");

        // 비회원: 세션에 저장
        if (userId == null) {
            List<LectureHistoryDTO> guestHistory = (List<LectureHistoryDTO>) session.getAttribute("guestHistory");
            if (guestHistory == null) guestHistory = new ArrayList<>();

            // 중복 제거
            guestHistory.removeIf(history -> history.getLectureIdx() == reqDTO.getLectureIdx());
            guestHistory.add(reqDTO);
            session.setAttribute("guestHistory", guestHistory);

            return ResponseEntity.ok().build();
        }

        // 회원: DB에 저장
        lectureHistoryService.saveLectureHistory(userId, reqDTO);
        return ResponseEntity.ok().build();
    }
}
