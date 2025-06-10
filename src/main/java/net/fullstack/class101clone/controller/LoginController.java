package net.fullstack.class101clone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;
import net.fullstack.class101clone.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String login(
			@ModelAttribute UserDTO userDTO,
			RedirectAttributes ra,
			Model model
	) {
		log.info("UserDTO : {}", userDTO);
		UserEntity userInfo = userService.findByUserId(userDTO.getUserId());
		if (userInfo == null) {
			ra.addFlashAttribute("msg", "잘못된 아이디 또는 비밀번호입니다.");
			return "redirect:/login"; // Redirect to login page with error message
		} else {
			UserDTO dto = UserDTO.builder()
					.userId(userInfo.getUserId())
					.userName(userInfo.getUserName())
					.build();
			model.addAttribute("userInfo", dto);
			ra.addFlashAttribute("msg", "반갑습니다, " + userInfo.getUserName() + "님!");
			return "redirect:/"; // Redirect to home page after successful login
		}
	}

	@GetMapping("/register")
	public String registerPage() {
		return "login/register";
	}

	@PostMapping("/register")
	public String register(
			@ModelAttribute UserDTO userDTO,
			RedirectAttributes ra
	) {
		log.info("Registering user: {}", userDTO);
		if(userService.findByUserId(userDTO.getUserId()) != null) {
			log.warn("User ID already exists: {}", userDTO.getUserId());
			return "redirect:/register?error"; // Redirect to register page with error
		}
		userService.register(userDTO);
		log.info("User registered successfully: {}", userDTO.getUserName());
		return "redirect:/login"; // Redirect to login page after registration
	}
}