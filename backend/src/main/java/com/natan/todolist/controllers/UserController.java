package com.natan.todolist.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.natan.todolist.dto.user.UserCreateDTO;
import com.natan.todolist.dto.user.UserDTO;
import com.natan.todolist.entities.User;
import com.natan.todolist.mappers.UserMapper;
import com.natan.todolist.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
		User user = UserMapper.toEntity(userCreateDTO);
		user = userService.createUser(user);
		UserDTO userDTO = UserMapper.toDTO(user);
		URI uri = ServletUriComponentsBuilder
			    .fromCurrentRequest()
			    .path("/{id}")
			    .buildAndExpand(userDTO.getId())
			    .toUri();
		return ResponseEntity.created(uri).body(userDTO);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
		User user = userService.getByIdOrThrow(id);
		UserDTO dto = UserMapper.toDTO(user);
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<UserDTO> getByEmail(@PathVariable String email) {
		Optional<User> userOptional = userService.findByEmail(email);
		if(userOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		UserDTO dto = UserMapper.toDTO(userOptional.get());
		return ResponseEntity.ok().body(dto);
	}
	
	@PutMapping(value = "{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserCreateDTO userCreateDTO) {
		User entity = UserMapper.toEntity(userCreateDTO);
		User userUpdated = userService.update(id, entity);
		UserDTO dto = UserMapper.toDTO(userUpdated);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		userService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
