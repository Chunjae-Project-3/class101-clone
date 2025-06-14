package net.fullstack.class101clone.repository.creator;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.ClassEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CreatorRepositoryImpl implements CreatorRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public List<ClassEntity> recentClasses(int creatorId) {
        return entityManager.createQuery(
                        "SELECT c FROM ClassEntity c WHERE c.creator.creatorId = :creatorId ORDER BY c.createdAt DESC",
                        ClassEntity.class
                ).setParameter("creatorId", creatorId)
                .setMaxResults(3)
                .getResultList();

    }
}
