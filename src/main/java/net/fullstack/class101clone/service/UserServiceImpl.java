package net.fullstack.class101clone.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceIf {
	private final UserRepositoryIf userRepository;
	private final PasswordEncoder passwordEncoder;

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

	public boolean verifyPassword(String userId, String password) {
		UserEntity user = userRepository.findByUserId(userId)
				.orElse(null);
		if (user == null) {
			return false;
		}

		log.info("Verifying password for userId: {}", user);
		log.info("isCorrect: {}", passwordEncoder.matches(password, user.getUserPwd()));
		return passwordEncoder.matches(password, user.getUserPwd());
	}
}