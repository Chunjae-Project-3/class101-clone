package net.fullstack.class101clone.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;
import net.fullstack.class101clone.service.UserServiceImpl;
import net.fullstack.class101clone.util.CommonValidationUtil;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequiredArgsConstructor
public class LoginController {
	private final UserServiceImpl userService;
	private final HttpSession httpSession;

	@GetMapping("/login")
	public String loginPage() {
		return "login/login";
	}

	@PostMapping("/login")
	public String login(
			@Valid @ModelAttribute UserDTO userDTO,
			HttpServletRequest req,
			BindingResult bindingResult,
			RedirectAttributes ra,
			Model model
	) {
		log.info("UserDTO : {}", userDTO);

		HttpSession session = req.getSession();

		if (bindingResult.hasErrors()) {
			log.error("Validation errors occurred: {}", bindingResult.getAllErrors());
			ra.addFlashAttribute("loginErrorMsg", bindingResult.getAllErrors().get(0).getDefaultMessage());
			return "redirect:/login"; // Redirect to signup page with error message
		}

		if (CommonValidationUtil.checkSQLInjection(userDTO.getUserId()) || CommonValidationUtil.checkSQLInjection(userDTO.getUserPwd())) {
			log.warn("SQL Injection attempt detected for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("loginErrorMsg", "잘못된 접근입니다.");
			return "redirect:/login"; // Redirect to login page with error message
		}

		if (CommonValidationUtil.checkXSS(userDTO.getUserId()) || CommonValidationUtil.checkXSS(userDTO.getUserPwd())) {
			log.warn("XSS attempt detected for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("loginErrorMsg", "잘못된 접근입니다.");
			return "redirect:/login"; // Redirect to login page with error message
		}

		UserDTO userInfo = userService.login(userDTO);
		if (userInfo == null) {
			ra.addFlashAttribute("loginErrorMsg", "잘못된 아이디 또는 비밀번호입니다.");
			return "redirect:/login"; // Redirect to login page with error message
		} else {
			session.setAttribute("loginId", userInfo.getUserId());
			model.addAttribute("userInfo", userInfo);
			session.setAttribute("userInfo", userInfo); // Store user info in session
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
		if (bindingResult.hasErrors()) {
			log.error("Validation errors occurred: {}", bindingResult.getAllErrors());
			ra.addFlashAttribute("signupErrorMsg", bindingResult.getAllErrors().get(0).getDefaultMessage());
			return "redirect:/signup"; // Redirect to signup page with error message
		}

		if (CommonValidationUtil.checkSQLInjection(userDTO.getUserId()) || CommonValidationUtil.checkSQLInjection(userDTO.getUserPwd())) {
			log.warn("SQL Injection attempt detected for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("signupErrorMsg", "포함할 수 없는 문자가 존재합니다.");
			return "redirect:/signup"; // Redirect to login page with error message
		}

		if (CommonValidationUtil.checkXSS(userDTO.getUserId()) || CommonValidationUtil.checkXSS(userDTO.getUserPwd())) {
			log.warn("XSS attempt detected for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("signupErrorMsg", "포함할 수 없는 문자가 존재합니다.");
			return "redirect:/signup"; // Redirect to login page with error message
		}

		if (userService.existsByUserId(userDTO.getUserId())) {
			log.warn("User ID already exists: {}", userDTO.getUserId());
			ra.addFlashAttribute("signupErrorMsg", "이미 존재하는 아이디입니다.");
			return "redirect:/signup"; // Redirect to signup page with error
		}

		boolean signupFlag = userService.signup(userDTO);
		log.info("User SignUp successfully: {}", userDTO.getUserName());
		if (signupFlag) {
			ra.addFlashAttribute("signUpSuccessMsg", "회원가입이 완료되었습니다!");
			return "redirect:/login"; // Redirect to login page after registration
		} else {
			log.error("User SignUp failed for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("signupErrorMsg", "회원가입에 실패했습니다. 다시 시도해주세요.");
			return "redirect:/signup"; // Redirect to signup page with error message
		}
	}

	@GetMapping("/quit")
	public String quit(
			HttpServletRequest req,
			RedirectAttributes ra
	) {
		log.info("Processing user quit request.");
		UserDTO userInfo = (UserDTO) req.getSession().getAttribute("userInfo");
		if (userInfo == null) {
			log.warn("User not logged in, cannot process quit request.");
			ra.addFlashAttribute("quitErrorMsg", "로그인이 필요합니다.");
			return "redirect:/login"; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
		}

		boolean quitFlag = userService.quit(userInfo);
		if (quitFlag) {
			log.info("User quit successfully: {}", userInfo.getUserId());
			req.getSession().invalidate(); // 세션 무효화
			ra.addFlashAttribute("quitSuccessMsg", "회원 탈퇴가 완료되었습니다.");
			return "redirect:/"; // 홈 페이지로 리다이렉트
		} else {
			log.error("User quit failed for userId: {}", userInfo.getUserId());
			ra.addFlashAttribute("quitErrorMsg", "회원 탈퇴에 실패했습니다. 다시 시도해주세요.");
			return "redirect:/mypage"; // 마이페이지로 리다이렉트
		}
	}

	@GetMapping("/mypage")
	public String myPage(
			HttpServletRequest req,
			RedirectAttributes ra,
			Model model
	) {
		UserDTO userInfo = (UserDTO) req.getSession().getAttribute("userInfo");
		log.info(userInfo == null ? "User not logged in." : "User is logged in: {}", userInfo);
		if (userInfo == null) {
			log.warn("User not logged in, redirecting to login page.");
			ra.addFlashAttribute("loginRequired", "로그인이 필요합니다.");
			return "redirect:/login"; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
		}

		model.addAttribute("userInfo", userInfo);
		return "login/mypage"; // 마이페이지 뷰로 이동
	}

	@PostMapping("/mypage/edit")
	public String editUserInfo(
			@RequestParam String userName,
			@RequestParam String originalPwd,
			@RequestParam String newPwd,
			@RequestParam String newPwdConfirm,
			HttpServletRequest req,
			RedirectAttributes ra,
			Model model
	) {
		UserDTO userInfo = (UserDTO) req.getSession().getAttribute("userInfo");

		log.info("userName: {}, originalPwd: {}, newPwd: {}, newPwdConfirm: {}", userName, originalPwd, newPwd, newPwdConfirm);

		if (userInfo == null) {
			log.warn("User not logged in, cannot edit user info.");
			ra.addFlashAttribute("editErrorMsg", "로그인이 필요합니다.");
			return "redirect:/login"; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
		}

		if (!newPwd.equals(newPwdConfirm)) {
			log.error("New password and confirmation do not match.");
			ra.addFlashAttribute("editErrorMsg", "새 비밀번호와 확인이 일치하지 않습니다.");
			return "redirect:/mypage"; // 마이페이지로 리다이렉트
		}

		if (
				CommonValidationUtil.checkSQLInjection(userName)
						|| CommonValidationUtil.checkSQLInjection(originalPwd)
						|| CommonValidationUtil.checkSQLInjection(newPwd)
						|| CommonValidationUtil.checkSQLInjection(newPwdConfirm)
		) {
			log.warn("SQL Injection attempt detected for userId: {}", userInfo.getUserId());
			ra.addFlashAttribute("editErrorMsg", "포함할 수 없는 문자가 존재합니다.");
			return "redirect:/mypage"; // 마이페이지로 리다이렉트
		}
		if (
				CommonValidationUtil.checkXSS(userName)
						|| CommonValidationUtil.checkXSS(originalPwd)
						|| CommonValidationUtil.checkXSS(newPwd)
						|| CommonValidationUtil.checkXSS(newPwdConfirm)
		) {
			log.warn("XSS attempt detected for userId: {}", userInfo.getUserId());
			ra.addFlashAttribute("editErrorMsg", "포함할 수 없는 문자가 존재합니다.");
			return "redirect:/mypage"; // 마이페이지로 리다이렉트
		}

		String userId = userInfo.getUserId();

		UserDTO updatedUser = UserDTO.builder()
				.userId(userId)
				.userName(userName)
				.userPwd(originalPwd) // 원래 비밀번호로 인증
				.build();

		updatedUser = userService.updateUserInfo(updatedUser, newPwd);

		if (updatedUser == null) {
			log.error("Failed to update user info for userId: {}", userId);
			ra.addFlashAttribute("editErrorMsg", "회원 정보 수정에 실패했습니다. 다시 시도해주세요.");
			return "redirect:/mypage"; // 마이페이지로 리다이렉트
		}
		HttpSession session = req.getSession();
		session.setAttribute("userInfo", updatedUser);
		model.addAttribute("userInfo", updatedUser);
		return "redirect:/mypage"; // 마이페이지 뷰로 이동
	}
}