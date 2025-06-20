package net.fullstack.class101clone.repository.creator;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.domain.CreatorEntity;
import net.fullstack.class101clone.domain.QClassEntity;
import net.fullstack.class101clone.domain.QCreatorEntity;
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

    @Override
    public List<ClassEntity> allClasses(int creatorId) {
        return entityManager.createQuery(
                        "SELECT c FROM ClassEntity c WHERE c.creator.creatorId = :creatorId ORDER BY c.createdAt DESC",
                        ClassEntity.class
                ).setParameter("creatorId", creatorId)
                .getResultList();
    }
}
