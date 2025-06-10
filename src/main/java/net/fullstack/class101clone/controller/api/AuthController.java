package net.fullstack.class101clone.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.dto.UserDTO;
import net.fullstack.class101clone.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final UserServiceImpl userService;

	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(@ModelAttribute UserDTO userDTO) {
		// 1. 사용자 인증 (예시: userService.authenticate)
		log.info("UserDTO : {}" , userDTO);
		boolean authenticated =  userService.authenticate(userDTO.getUserId(), userDTO.getUserPwd()) ;
		if (!authenticated) {
			return ResponseEntity.status(401)
					.body(null); // 인증 실패 시 401 Unauthorized 응답
		}

//		// 2. JWT 토큰 생성 (예시: jwtTokenProvider.createToken)
//		String token = /* JWT 토큰 생성 로직 (예: jwtTokenProvider.createToken(userDTO.getUserId())) */;
//
//		// 3. 토큰 반환
		return ResponseEntity.ok()
				.body(userDTO);
	}

}
