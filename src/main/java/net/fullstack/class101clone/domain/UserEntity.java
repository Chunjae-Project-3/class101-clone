package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_user")
public class UserEntity {
	@Id
	@Column(name = "user_id", length = 20, nullable = false, updatable = false)
	private String userId; // 회원 아이디
	@Column(name = "user_pwd", length = 255, nullable = false, updatable = true)
	private String userPwd; // 회원 비밀번호
	@Column(name = "user_name", length = 50, nullable = false, updatable = true)
	private String userName; // 회원 이름
	@Column(name = "user_created_at", nullable=false)
	private LocalDateTime userCreatedAt; // 회원 가입일

	@PrePersist
	public void prePersist() {
		this.userCreatedAt = this.userCreatedAt == null ? LocalDateTime.now() : this.userCreatedAt;
	}
}
