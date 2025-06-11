package net.fullstack.class101clone.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;
import net.fullstack.class101clone.service.UserServiceImpl;
import net.fullstack.class101clone.util.CommonValidationUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
			@Valid @ModelAttribute UserDTO userDTO,
			BindingResult bindingResult,
			RedirectAttributes ra,
			Model model
	) {
		log.info("UserDTO : {}", userDTO);

		if(bindingResult.hasErrors()) {
			log.error("Validation errors occurred: {}", bindingResult.getAllErrors());
			ra.addFlashAttribute("loginErrorMsg", bindingResult.getAllErrors().get(0).getDefaultMessage());
			return "redirect:/login"; // Redirect to signup page with error message
		}

		if(CommonValidationUtil.checkSQLInjection(userDTO.getUserId()) || CommonValidationUtil.checkSQLInjection(userDTO.getUserPwd())) {
			log.warn("SQL Injection attempt detected for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("loginErrorMsg", "잘못된 접근입니다.");
			return "redirect:/login"; // Redirect to login page with error message
		}

		if(CommonValidationUtil.checkXSS(userDTO.getUserId()) || CommonValidationUtil.checkXSS(userDTO.getUserPwd())) {
			log.warn("XSS attempt detected for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("loginErrorMsg", "잘못된 접근입니다.");
			return "redirect:/login"; // Redirect to login page with error message
		}

		UserDTO userInfo = userService.login(userDTO);
		if (userInfo == null) {
			ra.addFlashAttribute("loginErrorMsg", "잘못된 아이디 또는 비밀번호입니다.");
			return "redirect:/login"; // Redirect to login page with error message
		} else {
			model.addAttribute("userInfo", userInfo);
			ra.addFlashAttribute("welcomeMsg", "반갑습니다, " + userInfo.getUserName() + "님!");
			return "redirect:/"; // Redirect to home page after successful login
		}
	}

	@GetMapping("/signup")
	public String signupPage() {
		return "login/signup";
	}

	@PostMapping("/signup")
	public String signup(
			@Valid @ModelAttribute UserDTO userDTO,
			BindingResult bindingResult,
			RedirectAttributes ra
	) {
		log.info("Registering user: {}", userDTO);
		if(bindingResult.hasErrors()) {
			log.error("Validation errors occurred: {}", bindingResult.getAllErrors());
			ra.addFlashAttribute("signupErrorMsg", bindingResult.getAllErrors().get(0).getDefaultMessage());
			return "redirect:/signup"; // Redirect to signup page with error message
		}

		if(CommonValidationUtil.checkSQLInjection(userDTO.getUserId()) || CommonValidationUtil.checkSQLInjection(userDTO.getUserPwd())) {
			log.warn("SQL Injection attempt detected for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("signupErrorMsg", "포함할 수 없는 문자가 존재합니다.");
			return "redirect:/signup"; // Redirect to login page with error message
		}

		if(CommonValidationUtil.checkXSS(userDTO.getUserId()) || CommonValidationUtil.checkXSS(userDTO.getUserPwd())) {
			log.warn("XSS attempt detected for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("signupErrorMsg", "포함할 수 없는 문자가 존재합니다.");
			return "redirect:/signup"; // Redirect to login page with error message
		}

		if(userService.existsByUserId(userDTO.getUserId())) {
			log.warn("User ID already exists: {}", userDTO.getUserId());
			ra.addFlashAttribute("signupErrorMsg", "이미 존재하는 아이디입니다.");
			return "redirect:/signup"; // Redirect to signup page with error
		}

		boolean signupFlag = userService.signup(userDTO);
		log.info("User SignUp successfully: {}", userDTO.getUserName());
		if(signupFlag) {
			ra.addFlashAttribute("signUpSuccessMsg", "회원가입이 완료되었습니다!");
			return "redirect:/login"; // Redirect to login page after registration
		} else {
			log.error("User SignUp failed for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("signupErrorMsg", "회원가입에 실패했습니다. 다시 시도해주세요.");
			return "redirect:/signup"; // Redirect to signup page with error message
		}
	}
}