package net.fullstack.class101clone.repository.classes;

import net.fullstack.class101clone.domain.ClassEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Integer>, ClassRepositoryCustom {

    @EntityGraph(attributePaths = {"creator"})
    Optional<ClassEntity> findWithCreatorByClassIdx(Integer classIdx);
}
