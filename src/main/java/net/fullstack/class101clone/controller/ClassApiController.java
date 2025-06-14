package net.fullstack.class101clone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.dto.classes.CurriculumDTO;
import net.fullstack.class101clone.service.classes.ClassService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/class")
@Tag(name = "클래스 API")
public class ClassApiController {

    private final ClassService classService;

    @GetMapping("/{classIdx}")
    @Operation(summary = "클래스 강좌 목록(커리큘럼) 조회")
    public CurriculumDTO getLectureListByClassIdx(@PathVariable int classIdx) {
        return classService.getCurriculum(classIdx);
    }
}
