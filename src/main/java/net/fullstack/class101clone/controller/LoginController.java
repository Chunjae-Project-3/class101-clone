package net.fullstack.class101clone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String loginPage() {
		return "login/login";
	}

	@GetMapping("/register")
	public String registerPage() {
		return "login/register";
	}
}