package net.fullstack.class101clone.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDTO {
	private String userId;
	private String userPwd;
	private String userName;
	private LocalDateTime userCreatedAt;
}