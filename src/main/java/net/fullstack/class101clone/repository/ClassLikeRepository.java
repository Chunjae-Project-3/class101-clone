package net.fullstack.class101clone.repository;

import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.domain.ClassLikeEntity;
import net.fullstack.class101clone.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassLikeRepository extends JpaRepository<ClassLikeEntity, Integer> {
    // 해당 사용자가 특정 클래스를 찜했는지 여부
    boolean existsByClassLikeUserAndClassLikeRef(UserEntity user, ClassEntity classEntity);

    // 특정 클래스의 총 찜 수
    long countByClassLikeRef(ClassEntity classEntity);

    // 삭제 또는 조회용으로도 쓸 수 있는 optional
    Optional<ClassLikeEntity> findByClassLikeUserAndClassLikeRef(UserEntity user, ClassEntity classEntity);
}
