package net.fullstack.class101clone.service;

import net.fullstack.class101clone.domain.UserEntity;
import net.fullstack.class101clone.dto.UserDTO;

public interface UserServiceIf {
	boolean existsByUserId(String userId);

	UserDTO login(UserDTO userDTO);

	boolean signup(UserDTO userDTO);
}
