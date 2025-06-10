package net.fullstack.class101clone.service;

import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;

public interface UserServiceIf {
	boolean authenticate(String userId, String userPwd);

	UserEntity findByUserId(String userId);

	void register(UserDTO userDTO);
}
