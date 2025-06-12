package net.fullstack.class101clone.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.dto.UserDTO;
import net.fullstack.class101clone.service.UserServiceImpl;
import net.fullstack.class101clone.util.CommonValidationUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequiredArgsConstructor
public class LoginController {
	private final UserServiceImpl userService;

	@GetMapping("/login")
	public String loginPage(
			HttpServletRequest req
	) {
		HttpSession session = req.getSession();

		// 이미 로그인된 사용자 정보가 있는지 확인
		UserDTO userInfo = (UserDTO) session.getAttribute("userInfo");
		if(userInfo != null) {
			log.info("User is already logged in: {}", userInfo);
			return "redirect:/";
		}

		return "login/login";
	}

	@PostMapping("/login")
	public String login(
			@Valid @ModelAttribute UserDTO userDTO,
			BindingResult bindingResult,
			HttpServletRequest req,
			RedirectAttributes ra,
			Model model
	) {
		log.info("UserDTO : {}", userDTO);

		HttpSession session = req.getSession();

		if (bindingResult.hasErrors()) {
			log.error("Validation errors occurred: {}", bindingResult.getAllErrors());
			ra.addFlashAttribute("loginErrorMsg", bindingResult.getAllErrors().get(0).getDefaultMessage());
			return "redirect:/login";
		}

		if (CommonValidationUtil.checkXSS(userDTO.getUserId()) || CommonValidationUtil.checkXSS(userDTO.getUserPwd())) {
			log.warn("XSS attempt detected for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("loginErrorMsg", "잘못된 접근입니다.");
			return "redirect:/login";
		}

		UserDTO userInfo = userService.login(userDTO);
		if (userInfo == null) {
			ra.addFlashAttribute("loginErrorMsg", "잘못된 아이디 또는 비밀번호입니다.");
			return "redirect:/login";
		} else {
			session.setAttribute("loginId", userInfo.getUserId());
			model.addAttribute("userInfo", userInfo);
			session.setAttribute("userInfo", userInfo);
			ra.addFlashAttribute("welcomeMsg", "반갑습니다, " + userInfo.getUserName() + "님!");
			return "redirect:/";
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
			return "redirect:/signup";
		}

		if (CommonValidationUtil.checkXSS(userDTO.getUserId()) || CommonValidationUtil.checkXSS(userDTO.getUserPwd())) {
			log.warn("XSS attempt detected for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("signupErrorMsg", "포함할 수 없는 문자가 존재합니다.");
			return "redirect:/signup";
		}

		if (userService.existsByUserId(userDTO.getUserId())) {
			log.warn("User ID already exists: {}", userDTO.getUserId());
			ra.addFlashAttribute("signupErrorMsg", "이미 존재하는 아이디입니다.");
			return "redirect:/signup";
		}

		boolean signupFlag = userService.signup(userDTO);
		log.info("User SignUp successfully: {}", userDTO.getUserName());
		if (signupFlag) {
			ra.addFlashAttribute("signupSuccessMsg", "회원가입이 완료되었습니다!");
			return "redirect:/login";
		} else {
			log.error("User SignUp failed for userId: {}", userDTO.getUserId());
			ra.addFlashAttribute("signupErrorMsg", "회원가입에 실패했습니다. 다시 시도해주세요.");
			return "redirect:/signup";
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
			return "redirect:/login";
		}

		boolean quitFlag = userService.quit(userInfo);
		if (quitFlag) {
			log.info("User quit successfully: {}", userInfo.getUserId());
			req.getSession().invalidate();
			ra.addFlashAttribute("quitSuccessMsg", "회원 탈퇴가 완료되었습니다.");
			return "redirect:/";
		} else {
			log.error("User quit failed for userId: {}", userInfo.getUserId());
			ra.addFlashAttribute("quitErrorMsg", "회원 탈퇴에 실패했습니다. 다시 시도해주세요.");
			return "redirect:/mypage";
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
			return "redirect:/login";
		}

		model.addAttribute("userInfo", userInfo);
		return "login/mypage";
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
			return "redirect:/login";
		}

		ra.addFlashAttribute("activeEditTab", "show active");

		// originalPwd 와 실제 데이터베이스에 있는 값이 일치하는지 확인
		if (!userService.verifyPassword(userInfo.getUserId(), originalPwd)) {
			log.error("Original password is incorrect for user: {}", userInfo.getUserId());
			ra.addFlashAttribute("editErrorMsg", "현재 비밀번호가 일치하지 않습니다.");
			return "redirect:/mypage";
		}
		// 새 비밀번호와 확인이 일치하는지 확인
		if (newPwd != null && !newPwd.isEmpty() && !newPwd.equals(newPwdConfirm)) {
			log.error("New password and confirmation do not match.");
			ra.addFlashAttribute("editErrorMsg", "새 비밀번호와 확인이 일치하지 않습니다.");
			return "redirect:/mypage";
		}

		// 비밀번호가 UserDTO 의 패턴을 따르는지 확인
		if (newPwd != null && !newPwd.isEmpty() && !newPwd.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d])[^\\s]{8,20}$")) {
			log.error("New password does not meet the required pattern.");
			ra.addFlashAttribute("editErrorMsg", "비밀번호는 8~20자의 영문, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.");
			return "redirect:/mypage";
		}

		if (
				CommonValidationUtil.checkXSS(userName)
						|| CommonValidationUtil.checkXSS(originalPwd)
						|| CommonValidationUtil.checkXSS(newPwd)
						|| CommonValidationUtil.checkXSS(newPwdConfirm)
		) {
			log.warn("XSS attempt detected for userId: {}", userInfo.getUserId());
			ra.addFlashAttribute("editErrorMsg", "포함할 수 없는 문자가 존재합니다.");
			return "redirect:/mypage";
		}

		String userId = userInfo.getUserId();

		UserDTO updatedUser = UserDTO.builder()
				.userId(userId)
				.userName(userName)
				.userPwd(originalPwd)
				.build();

		updatedUser = userService.updateUserInfo(updatedUser, newPwd);

		if (updatedUser == null) {
			log.error("Failed to update user info for userId: {}", userId);
			ra.addFlashAttribute("editErrorMsg", "회원 정보 수정에 실패했습니다. 다시 시도해주세요.");
			return "redirect:/mypage";
		}

		log.info("User info updated successfully for userId: {}", userId);
		HttpSession session = req.getSession();
		session.setAttribute("userInfo", updatedUser);
		ra.addFlashAttribute("editSuccessMsg", "회원 정보 수정을 완료했습니다.");
		model.addAttribute("userInfo", updatedUser);
		return "redirect:/mypage";
	}
}