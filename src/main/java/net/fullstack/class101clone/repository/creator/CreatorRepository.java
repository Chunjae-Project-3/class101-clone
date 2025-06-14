package net.fullstack.class101clone.repository.creator;

import net.fullstack.class101clone.domain.CreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<CreatorEntity, Integer> , CreatorRepositoryCustom {

}
