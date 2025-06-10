package net.fullstack.class101clone.repository;

import net.fullstack.class101clone.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
