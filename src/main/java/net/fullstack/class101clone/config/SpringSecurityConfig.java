package net.fullstack.class101clone.config;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {
	private final CorsConfig corsConfig;
	private final AuthenticationConfiguration authenticationConfiguration;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence rawPassword) {
				return hashWithSHA256(rawPassword.toString());
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				String hashedAttemptedPassword = hashWithSHA256(rawPassword.toString());
				return hashedAttemptedPassword.equals(encodedPassword);
			}

			private String hashWithSHA256(String input) {
				try {
					MessageDigest md = MessageDigest.getInstance("SHA-256");
					byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
					StringBuilder hexString = new StringBuilder();
					for (byte b : hash) {
						String hex = Integer.toHexString(0xff & b);
						if (hex.length() == 1) hexString.append('0');
						hexString.append(hex);
					}
					return hexString.toString();
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.headers(headers -> headers
						.defaultsDisabled()
						.frameOptions(frame -> frame.sameOrigin())
				)
				.authorizeHttpRequests(
						authorz -> authorz
								.requestMatchers("/user/mypage").authenticated()
								.anyRequest().permitAll()
				)
				.csrf(csrf -> csrf.disable())
				.formLogin(form -> form
						.loginPage("/login") // 커스텀 로그인 페이지 경로
						.loginProcessingUrl("/api/auth/login") // 로그인 form의 action과 일치
						.defaultSuccessUrl("/") // 로그인 성공 시 이동할 경로
						.permitAll()
				)
				.logout(logout -> logout
						.logoutUrl("/logout") // 로그아웃 요청 URL
						.logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 경로
						.invalidateHttpSession(true) // 세션 무효화
						.deleteCookies("JSESSIONID") // 쿠키 삭제
				)
				.addFilter(corsConfig.corsFilter())
				.sessionManagement(sessionManagement -> sessionManagement
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
						.maximumSessions(1)
						.expiredUrl("/login")
						.and()
						.invalidSessionUrl("/login")
						.sessionFixation().newSession()
				)
		;

		return http.build();
	}

}
