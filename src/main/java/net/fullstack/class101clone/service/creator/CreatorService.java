package net.fullstack.class101clone.service.creator;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.CreatorEntity;
import net.fullstack.class101clone.repository.creator.CreatorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorService {
    private final CreatorRepository creatorRepository;

    public CreatorEntity getCreator(Integer creatorId) {
        return creatorRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크리에이터. id :" + creatorId));
    }
}
