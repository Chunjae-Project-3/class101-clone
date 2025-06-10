package net.fullstack.class101clone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;
import net.fullstack.class101clone.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
public class LoginController {
	private final UserServiceImpl userService;
	@GetMapping("/login")
	public String loginPage() {
		return "login/login";
	}

	@PostMapping("/login")
	public UserDTO login(@ModelAttribute UserDTO userDTO) {
		log.info("UserDTO : {}", userDTO);
		UserEntity userInfo = userService.findByUserId(userDTO.getUserId());
		if (userInfo == null) {
			return null;
		} else {
			UserDTO dto = UserDTO.builder()
					.userId(userInfo.getUserId())
					.userName(userInfo.getUserName())
					.build();
			log.info("Login successful for user: {}", dto);
			return dto;
		}
	}

	@GetMapping("/register")
	public String registerPage() {
		return "login/register";
	}
}