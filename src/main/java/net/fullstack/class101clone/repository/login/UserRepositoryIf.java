package net.fullstack.class101clone.repository.login;

import net.fullstack.class101clone.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryIf extends JpaRepository<UserEntity, String>, UserRepositoryIfCustom {
}
