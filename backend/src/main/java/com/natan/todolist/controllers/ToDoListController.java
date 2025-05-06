package com.natan.todolist.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

import com.natan.todolist.dto.todolist.ToDoListCreateDTO;
import com.natan.todolist.dto.todolist.ToDoListDTO;
import com.natan.todolist.entities.ToDoList;
import com.natan.todolist.entities.User;
import com.natan.todolist.mappers.ToDoListMapper;
import com.natan.todolist.services.ToDoListService;
import com.natan.todolist.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/lists")
public class ToDoListController {
	
	private ToDoListService listService;
	private UserService userService;
	
	@Autowired
	public ToDoListController(ToDoListService listService, UserService userService) {
		this.listService = listService;
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity<ToDoListDTO> createList(@PathVariable Long userId, @Valid @RequestBody ToDoListCreateDTO dto) {
		User user = userService.getByIdOrThrow(userId);
		ToDoList entity = ToDoListMapper.toEntity(dto);
		entity = listService.createList(entity, user);
		ToDoListDTO listDTO = ToDoListMapper.toDTO(entity);
		URI uri = ServletUriComponentsBuilder
			    .fromCurrentRequest()
			    .path("/{id}")
			    .buildAndExpand(listDTO.getId())
			    .toUri();
		return ResponseEntity.created(uri).body(listDTO);
	}
	
	@GetMapping
	public ResponseEntity<List<ToDoListDTO>> getAll(@PathVariable Long userId){
		User user = userService.getByIdOrThrow(userId);
		
		List<ToDoListDTO> dtos = listService.getListsByUserId(user).stream().map(ToDoListMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok().body(dtos);
	}
	
	@GetMapping(value = "/{listId}")
	public ResponseEntity<ToDoListDTO> getListById(@PathVariable Long userId, @PathVariable Long listId) {
		User user = userService.getByIdOrThrow(userId);
		ToDoList entity = listService.getListByIdOrThrow(listId);
		
		if(!entity.getUser().getId().equals(userId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		ToDoListDTO dto = ToDoListMapper.toDTO(entity);
		return ResponseEntity.ok().body(dto);
	}
	
	@PutMapping(value = "/{listId}")
	public ResponseEntity<ToDoListDTO> updateList(@PathVariable Long userId, @PathVariable Long listId, @Valid @RequestBody ToDoListCreateDTO dto) {
		User user = userService.getByIdOrThrow(userId);
		ToDoList existing = listService.getListByIdOrThrow(listId);
		
		if(!existing.getUser().getId().equals(userId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		ToDoList toUpdate = ToDoListMapper.toEntity(dto);
		toUpdate.setUser(user);
		
		ToDoList listUpdated = listService.updateList(listId, toUpdate);
		ToDoListDTO listDTO = ToDoListMapper.toDTO(listUpdated);
		return ResponseEntity.ok().body(listDTO);
	}
	
	@DeleteMapping("/{listId}")
	public ResponseEntity<Void> deleteList(@PathVariable Long userId, @PathVariable Long listId) {
		User user = userService.getByIdOrThrow(userId);
		ToDoList existing = listService.getListByIdOrThrow(listId);
		
		if(!existing.getUser().getId().equals(userId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		existing.setUser(user);
		
		listService.deleteList(listId);
		return ResponseEntity.noContent().build();
	}
}
