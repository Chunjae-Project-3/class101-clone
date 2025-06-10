package net.fullstack.class101clone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.service.ClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@Tag(name = "클래스 API", description = "메인화면 클래스 조회용 API")
public class ClassApiController {

    private final ClassService classService;

    public ClassApiController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    @Operation(summary = "클래스 목록 조회", description = "등록된 클래스들을 반환합니다.")
    public List<ClassDTO> getClasses(@RequestParam(required = false) String category) {
        return classService.getClasses(category);
    }
}
