package net.fullstack.class101clone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.service.ClassService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
@Tag(name = "클래스 API", description = "메인화면 클래스 조회용 API")
@RequiredArgsConstructor
public class ClassApiController {

    private final ClassService classService;

    @GetMapping
    @Operation(summary = "클래스 목록 조회", description = "카테고리별 등록된 클래스 목록을 반환합니다.")
    public List<ClassDTO> getClasses(@RequestParam(required = false) String category) {
        return classService.getClasses(category);
    }

    @GetMapping("/{id}")
    @Operation(summary = "클래스 상세 전체 조회", description = "클래스, 이미지, 커리큘럼까지 포함된 정보를 반환합니다.")
    public Map<String, Object> getClassAllDetail(@PathVariable Integer id) {
        ClassDTO classInfo = classService.getClassDetail(id);
        List<String> imageList = classService.getClassImageList(id);
        Map<String, List<Map<String, String>>> curriculum = classService.getLectureCurriculum(id);

        return Map.of(
                "class", classInfo,
                "classImageList", imageList,
                "lectureCurriculum", curriculum
        );
    }

}
