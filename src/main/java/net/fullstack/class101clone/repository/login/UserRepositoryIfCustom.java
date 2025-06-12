package net.fullstack.class101clone.repository.login;

import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;

public interface UserRepositoryIfCustom {
	// 회원가입
	boolean signup(UserDTO userDTO);
	// 아이디 중복 체크
	boolean existsByUserId(String userId);
	// 탈퇴
	boolean quit(String userId);
	// 로그인
	UserEntity login(UserDTO userDTO);
	// 회원정보 수정
	UserEntity updateUserInfo(UserDTO userDTO);
}
