package net.fullstack.class101clone.repository.classes;

import net.fullstack.class101clone.domain.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<ClassEntity, Integer>, ClassRepositoryCustom {
}
