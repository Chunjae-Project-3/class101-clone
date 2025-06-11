package net.fullstack.class101clone.repository;

import net.fullstack.class101clone.domain.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<LectureEntity, Integer> {
    List<LectureEntity> findAllByLectureRef_ClassIdx(Integer classIdx);
}
