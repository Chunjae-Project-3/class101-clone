package net.fullstack.class101clone.controller.classes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.dto.CreatorDTO;
import net.fullstack.class101clone.dto.SubCategoryDTO;
import net.fullstack.class101clone.dto.classes.ClassResponseDTO;
import net.fullstack.class101clone.dto.classes.LectureDTO;
import net.fullstack.class101clone.dto.classes.LectureHistoryResponseDTO;
import net.fullstack.class101clone.dto.classes.SectionDTO;
import net.fullstack.class101clone.service.classes.ClassService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    public List<ClassDTO> getClasses(
            @RequestParam(required = false) String category,
            HttpSession session
    ) {
        String userId = (String) session.getAttribute("loginId");
        return classService.getClasses(category, userId);
    }

    @GetMapping("/top-liked")
    public ResponseEntity<List<ClassDTO>> getTopLikedClasses() {
        return ResponseEntity.ok(classService.getTopLikedClasses(10));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ClassDTO>> getRecentClasses() {
        return ResponseEntity.ok(classService.getRecentClasses(10));
    }

    @GetMapping("/{id}")
    @Operation(summary = "클래스 상세 전체 조회", description = "클래스, 이미지, 커리큘럼까지 포함된 정보를 반환합니다.")
    public ResponseEntity<ClassResponseDTO> getClassAllDetail(@PathVariable Integer id) {
        ClassDTO classInfo = classService.getClassByIdx(id);
        List<SectionDTO> curriculum = classService.getCurriculum(id, null, true);
        List<String> thumbnailUrls = classService.getSectionThumbnailUrlsByClassIdx(id);

        ClassResponseDTO responseDTO = ClassResponseDTO.builder()
                .classInfo(classInfo)
                .curriculum(curriculum)
                .thumbnailUrls(thumbnailUrls)
                .build();

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/category/{categoryIdx}")
    public Page<ClassDTO> getPagedClassesByCategoryIdx(
            @PathVariable Integer categoryIdx,
            @RequestParam(required = false) Integer subCategoryIdx,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "recent") String sort,
            HttpSession session
    ) {
        String userId = (String) session.getAttribute("loginId");
        return classService.getPagedClassesByCategoryAndSub(categoryIdx, subCategoryIdx, page, size, sort, userId);
    }


    @GetMapping("/sub-categories/{categoryIdx}")
    public ResponseEntity<List<SubCategoryDTO>> getSubCategories(@PathVariable Integer categoryIdx) {
        List<SubCategoryDTO> result = classService.getSubCategoriesByCategory(categoryIdx);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/creators/category/{categoryIdx}")
    @Operation(summary = "카테고리별 크리에이터 목록", description = "카테고리에 속한 클래스의 모든 크리에이터 목록을 반환합니다.")
    public ResponseEntity<List<CreatorDTO>> getCreatorsByCategory(@PathVariable Integer categoryIdx) {
        List<CreatorDTO> result = classService.getCreatorsByCategoryIdx(categoryIdx);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    @Operation(summary = "클래스 및 크리에이터 검색", description = "검색어를 기반으로 클래스 제목/설명/크리에이터명을 검색합니다.")
    public ResponseEntity<Map<String, Object>> searchAll(@RequestParam String q,
                                                         @RequestParam(defaultValue = "recent") String sort,
                                                         Pageable pageable,
                                                         HttpSession session) {
        String userId = (String) session.getAttribute("loginId");
        Map<String, Object> result = classService.searchClassesAndCreators(q, pageable, sort, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<List<ClassDTO>> getWishlist(HttpSession session) {
        String userId = (String) session.getAttribute("loginId");
        List<ClassDTO> wishlist = classService.getWishListByUserId(userId);
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping("/history")
    public ResponseEntity<List<LectureHistoryResponseDTO>> getHistory(HttpSession session) {
        String userId = (String) session.getAttribute("loginId");
        List<LectureHistoryResponseDTO> history = classService.getLectureHistory(userId);
        return ResponseEntity.ok(history);
    }
}
