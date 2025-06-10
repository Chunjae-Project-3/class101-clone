package net.fullstack.class101clone.config;

import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.repository.login.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {
	private final UserRepository userRepository;
	private final CorsConfig corsConfig;
	private final AuthenticationConfiguration authenticationConfiguration;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
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
				.addFilter(corsConfig.corsFilter())
				.sessionManagement(sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

}
