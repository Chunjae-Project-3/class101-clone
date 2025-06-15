package net.fullstack.class101clone.service.creator;

import net.fullstack.class101clone.domain.ClassEntity;
import net.fullstack.class101clone.domain.CreatorEntity;
import net.fullstack.class101clone.dto.ClassDTO;

import java.util.List;

public interface CreatorService {
    CreatorEntity getCreator(Integer creatorId);
    List<ClassDTO> showRecentClassesOfCreator(Integer creatorId);
}
