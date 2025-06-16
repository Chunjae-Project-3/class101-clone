package net.fullstack.class101clone.service.classes;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.domain.ClassLikeEntity;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.repository.classes.ClassLikeRepository;
import net.fullstack.class101clone.repository.classes.ClassRepository;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassLikeService {

    private final ClassLikeRepository classLikeRepository;
    private final ClassRepository classRepository;
    private final UserRepositoryIf userRepository;

    public Map<String, Object> toggleLike(String userId, int classId) {
        UserEntity user = userRepository.findByUserId(userId).orElseThrow();
        ClassEntity cls = classRepository.findById(classId).orElseThrow();

        Optional<ClassLikeEntity> existing = classLikeRepository.findByClassLikeUserAndClassLikeRef(user, cls);
        boolean liked;
        if (existing.isPresent()) {
            classLikeRepository.delete(existing.get());
            liked = false;
        } else {
            ClassLikeEntity like = ClassLikeEntity.builder()
                    .classLikeUser(user)
                    .classLikeRef(cls)
                    .build();
            classLikeRepository.save(like);
            liked = true;
        }

        long likeCount = classLikeRepository.countByClassLikeRef(cls);

        return Map.of(
                "liked", liked,
                "likeCount", likeCount
        );
    }

}
