package net.fullstack.class101clone.service;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import org.springframework.stereotype.Service;

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
	public boolean signup(UserDTO userDTO) {
		return userRepository.signup(userDTO);
	}

	@Override
	public boolean existsByUserId(String userId) {
		return userRepository.existsByUserId(userId);
	}
}