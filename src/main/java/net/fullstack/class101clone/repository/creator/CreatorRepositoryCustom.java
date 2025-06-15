package net.fullstack.class101clone.repository.creator;

import net.fullstack.class101clone.domain.ClassEntity;
import java.util.List;

public interface CreatorRepositoryCustom {
    List<ClassEntity> recentClasses(int creatorId);
}
