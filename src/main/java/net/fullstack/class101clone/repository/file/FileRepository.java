package net.fullstack.class101clone.repository.file;

import net.fullstack.class101clone.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    public Optional<FileEntity> findByFileName(String fileName);
    public void deleteByFileName(String fileName);
}
