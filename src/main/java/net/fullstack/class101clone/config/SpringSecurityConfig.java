//package net.fullstack.class101clone.config;
//
//import lombok.RequiredArgsConstructor;
//import net.fullstack.class101clone.filter.JWTAuthenticationFilter;
//import net.fullstack.class101clone.repository.login.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SpringSecurityConfig {
//	// 이 클래스는 스프링 시큐리티 설정을 위한 클래스입니다.
//	// 필요한 경우 추가적인 설정을 여기에 구현할 수 있습니다.
//	// 예를 들어, 사용자 인증, 권한 부여 등을 설정할 수 있습니다.
//	// 현재는 기본적인 설정만 포함되어 있으며, 필요에 따라 확장할 수 있습니다.
//	// 예시로, JWT 인증 필터나 CORS 설정 등을 추가할 수 있습니다.
//	private final UserRepository userRepository;
//	private final CorsConfig corsConfig;
//	private final AuthenticationConfiguration authenticationConfiguration;
//
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	public AuthenticationManager authenticationManager() throws Exception {
//		return authenticationConfiguration.getAuthenticationManager();
//	}
//
//	@Bean
//	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests(
//						authorz -> authorz
//								.requestMatchers("/api/v1/home", "/api/v1/join", "/api/v1/login").permitAll()
//								.anyRequest().authenticated())
//				.csrf(csrf -> csrf.disable())
//				.formLogin(form -> form.disable())
//				.addFilter(corsConfig.corsFilter())
//				// TODO: ADD JWT authentication Filter Setting
//				.addFilterAt(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//				// TODO: ADD JWT authorization Filter Setting
//				.addFilter(jwtAuthorizationFilter())
//				.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//		return http.build();
//	}
//
//	@Bean // 인증
//	public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//		JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authenticationManager());
//		return filter;
//	}
//
//	@Bean // 인가
//	public JWTAuthorizationFilter jwtAuthorizationFilter() throws Exception {
//		return new JWTAuthorizationFilter(authenticationManager(), myUserRepository);
//	}
//
//
//}
