package net.fullstack.class101clone.service.creator;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.domain.CreatorEntity;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.repository.creator.CreatorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorServiceImpl implements CreatorService {
    private final CreatorRepository creatorRepository;

    public CreatorEntity getCreator(Integer creatorId) {
        return creatorRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크리에이터. id :" + creatorId));
    }

    public List<ClassEntity> showAllClassesOfCreator(Integer creatorId) {
        return creatorRepository.allClasses(creatorId);
    }

    @Override
    public List<ClassDTO> showRecentClassesOfCreator(Integer creatorId) {
        List<ClassEntity> entities = creatorRepository.recentClasses(creatorId);

        return entities.stream().map(entity -> {
            String thumbnailUrl = null;
            if (entity.getClassThumbnailImg() != null) {
                thumbnailUrl = "/" + entity.getClassThumbnailImg().getFilePath()
                        + entity.getClassThumbnailImg().getFileName()
                        + "." + entity.getClassThumbnailImg().getFileExt();
            }

            return new ClassDTO(
                    entity.getClassIdx(),
                    entity.getClassTitle(),
                    entity.getClassDescription(),
                    thumbnailUrl,
                    entity.getClassCategory() != null ? entity.getClassCategory().getCategoryName() : null,
                    entity.getCreator().getCreatorName(),
                    entity.getCreator().getCreatorProfileImg(),
                    entity.getCreator().getCreatorDescription()
            );
        }).toList();
    }


}
