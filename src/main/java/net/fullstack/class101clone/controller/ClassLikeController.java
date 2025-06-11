package net.fullstack.class101clone.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.repository.ClassLikeRepository;
import net.fullstack.class101clone.repository.classes.ClassRepository;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import net.fullstack.class101clone.service.ClassLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/class-like")
@RequiredArgsConstructor
public class ClassLikeController {

    private final ClassLikeService classLikeService;
    private final UserRepositoryIf userRepository;
    private final ClassRepository classRepository;
    private final ClassLikeRepository classLikeRepository;

    // 찜 토글
    @PostMapping("/{classId}")
    public ResponseEntity<?> toggleLike(@PathVariable Integer classId, HttpSession session) {
        String userId = (String) session.getAttribute("loginId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Map<String, Object> result = classLikeService.toggleLike(userId, classId);
        return ResponseEntity.ok(result);
    }

    // 찜 상태 및 개수 조회
    @GetMapping("/status/{classId}")
    public ResponseEntity<?> getLikeStatus(@PathVariable Integer classId, HttpSession session) {
        String userId = (String) session.getAttribute("loginId");
        boolean liked = false;

        ClassEntity cls = classRepository.findById(classId).orElse(null);
        if (cls == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("클래스를 찾을 수 없습니다.");
        }

        if (userId != null) {
            UserEntity user = userRepository.findByUserId(userId).orElse(null);
            if (user != null) {
                liked = classLikeRepository.existsByClassLikeUserAndClassLikeRef(user, cls);
            }
        }

        long likeCount = classLikeRepository.countByClassLikeRef(cls);

        return ResponseEntity.ok(Map.of(
                "liked", liked,
                "likeCount", likeCount
        ));
    }
}
