package com.natan.todolist.mappers;

import com.natan.todolist.dto.user.UserCreateDTO;
import com.natan.todolist.dto.user.UserDTO;
import com.natan.todolist.entities.User;

public class UserMapper {
	public static UserDTO toDTO(User user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		return dto;
	}
	
	public static User toEntity(UserCreateDTO dto) {
		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		return user;
	}
}
