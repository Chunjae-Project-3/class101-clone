package net.fullstack.class101clone.repository.file;

import net.fullstack.class101clone.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Integer>, FileRepositoryCustom {
}
