package net.fullstack.class101clone.service;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;
import net.fullstack.class101clone.repository.login.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceIf {
	private final UserRepository userRepository;

	@Override
	public boolean authenticate(String userId, String userPwd) {
		UserEntity user = userRepository.findById(userId).orElse(null);
		// 비밀번호 암호화 비교 필요
		return user != null && user.getUserPwd().equals(userPwd);
	}

	@Override
	public UserEntity findByUserId(String userId) {
		return userRepository.findById(userId).orElse(null);
	}

	@Override
	public void register(UserDTO userDTO) {
		UserEntity user = UserEntity.builder()
				.userId(userDTO.getUserId())
				.userPwd(userDTO.getUserPwd()) // 암호화 필요
				.userName(userDTO.getUserName())
				.build();
		userRepository.save(user);
	}
}