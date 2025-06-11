package net.fullstack.class101clone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.service.ClassService;
import org.springframework.ui.Model;
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
    @Operation(summary = "클래스 목록 조회", description = "등록된 클래스들을 반환합니다.")
    public List<ClassDTO> getClasses(@RequestParam(required = false) String category) {
        return classService.getClasses(category);
    }

    @GetMapping("/{id}")
    @Operation(summary = "클래스 상세 조회 (API)", description = "클래스 ID로 JSON 형태 상세 정보를 반환합니다.")
    public ClassDTO getClassDetail(@PathVariable Integer id) {
        return new ClassDTO(
                id,
                "이모티콘 클래스",
                "귀여운 이모티콘 만들기",
                "/images/default-image.png",
                "일러스트"
        );
    }
}
