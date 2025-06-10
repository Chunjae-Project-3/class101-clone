package net.fullstack.class101clone.repository.login;

import net.fullstack.class101clone.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
	// 사용자 이름으로 사용자 정보를 조회하는 메소드
	UserEntity findByUserName(String userName);
}
