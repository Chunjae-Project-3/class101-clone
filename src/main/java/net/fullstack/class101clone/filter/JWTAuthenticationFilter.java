//package net.fullstack.class101clone.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import net.fullstack.class101clone.dto.UserDTO;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/auth/login", "POST");
//	private final AuthenticationManager authenticationManager;
//
//	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
//		super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
//		this.authenticationManager = authenticationManager;
//	}
//
//	@Override
//	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//		ObjectMapper om = new ObjectMapper();
//		UserDTO loginRequest = null;
//		try {
//			loginRequest = om.readValue(request.getInputStream(), UserDTO.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//				loginRequest.getUserName(), loginRequest.getUserPwd()
//		);
//
//		Authentication auth = authenticationManager.authenticate(authenticationToken);
//		UserDTO principalDetails = (UserDTO) auth.getPrincipal();
//
//		return auth;
//	}
//}
