package net.fullstack.class101clone.repository.creator;

import net.fullstack.class101clone.domain.CreatorCommunityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreatorCommunityRepository extends JpaRepository<CreatorCommunityEntity, Long> {
    List<CreatorCommunityEntity> findByCreator_CreatorIdAndDeletedFalseOrderByCreatedAtDesc(Long creatorId);

    List<CreatorCommunityEntity> findTop3ByCreator_CreatorIdAndDeletedFalseOrderByCreatedAtDesc(Long creatorId);



}
