package net.fullstack.class101clone.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceIf {
	private final UserRepositoryIf userRepository;

	@Override
	public UserDTO login(UserDTO userDTO) {
		UserEntity user = userRepository.login(userDTO);
		if (user != null) {
			// 로그인 성공
			return UserDTO.builder()
					.userId(user.getUserId())
					.userName(user.getUserName())
					.build();
		}
		return null; // 로그인 실패
	}

	@Override
	public boolean quit(UserDTO userDTO) {
		return userRepository.quit(userDTO.getUserId());
	}

	@Override
	public boolean signup(UserDTO userDTO) {
		return userRepository.signup(userDTO);
	}

	@Override
	public boolean existsByUserId(String userId) {
		return userRepository.existsByUserId(userId);
	}

	@Override
	@Transactional
	public UserDTO updateUserInfo(UserDTO userDTO, String newPwd) {
		// 로그인 방식으로 인증
		UserEntity user = userRepository.login(userDTO);
		if (user != null) {
			// 새 비밀번호가 있으면 userDTO에 반영
			if (newPwd != null && !newPwd.isEmpty()) {
				userDTO.setUserPwd(newPwd);
			}
			UserEntity updatedUser = userRepository.updateUserInfo(userDTO);
			if (updatedUser != null) {
				return UserDTO.builder()
						.userId(updatedUser.getUserId())
						.userName(updatedUser.getUserName())
						.build();
			}
		}
		return null;
	}
}