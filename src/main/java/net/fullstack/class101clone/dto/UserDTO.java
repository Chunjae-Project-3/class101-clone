package net.fullstack.class101clone.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDTO {
	@Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "아이디는 6~20자의 영문 대소문자와 숫자만 사용할 수 있습니다.")
	private String userId;
	@Pattern(
			regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d])[^\\s]{8,20}$",
			message = "비밀번호는 8~20자의 영문, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다."
	)
	private String userPwd;
	@Pattern(regexp = "^[가-힣a-zA-Z0-9]{3,}$|^$", message = "이름은 3글자 이상이어야 하며 특수문자를 포함할 수 없습니다.")
	private String userName;
	private LocalDateTime userCreatedAt;
}