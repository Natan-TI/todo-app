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

import com.natan.todolist.dto.task.TaskCreateDTO;
import com.natan.todolist.dto.task.TaskDTO;
import com.natan.todolist.entities.Task;
import com.natan.todolist.entities.ToDoList;
import com.natan.todolist.mappers.TaskMapper;
import com.natan.todolist.services.TaskService;
import com.natan.todolist.services.ToDoListService;
import com.natan.todolist.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/lists/{listId}/tasks")
public class TaskController {
	private final TaskService taskService;
	private final UserService userService;
	private final ToDoListService listService;
	
	@Autowired
	public TaskController(TaskService taskService, UserService userService, ToDoListService listService) {
		this.listService = listService;
		this.taskService = taskService;
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity<TaskDTO> createTask(@PathVariable Long userId, @PathVariable Long listId, @Valid @RequestBody TaskCreateDTO dto) {
		ToDoList existingList = listService.getListByIdOrThrow(listId);
		
		if(!existingList.getUser().getId().equals(userId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		Task entity = TaskMapper.toEntity(dto);
		entity.setToDoList(existingList);
		
		Task saved = taskService.createTask(entity, existingList);
		TaskDTO taskDTO = TaskMapper.toDTO(saved);
		
		URI uri = ServletUriComponentsBuilder
			    .fromCurrentRequest()
			    .path("/{id}")
			    .buildAndExpand(taskDTO.getId())
			    .toUri();
		
		return ResponseEntity.created(uri).body(taskDTO);
	}
	
	@GetMapping
	public ResponseEntity<List<TaskDTO>> getAllTasks(@PathVariable Long userId, @PathVariable Long listId) {
		userService.getByIdOrThrow(userId);
		ToDoList existingList = listService.getListByIdOrThrow(listId);
		
		if(!existingList.getUser().getId().equals(userId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		List<TaskDTO> dtos = taskService.getTasksByListId(existingList).stream().map(TaskMapper::toDTO).collect(Collectors.toList());
		return ResponseEntity.ok().body(dtos);
	}
	
	@GetMapping("/{taskId}")
	public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId, @PathVariable Long listId, @PathVariable Long userId) {
		userService.getByIdOrThrow(userId);
		
		ToDoList existingList = listService.getListByIdOrThrow(listId);
		if(!existingList.getUser().getId().equals(userId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		Task existingTask = taskService.getByIdOrThrow(taskId);
		if(!existingTask.getToDoList().getId().equals(listId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		TaskDTO dto = TaskMapper.toDTO(existingTask);
		return ResponseEntity.ok().body(dto);
	}
	
	@PutMapping("/{taskId}")
	public ResponseEntity<TaskDTO> updateTask(@PathVariable Long userId, @PathVariable Long listId, @PathVariable Long taskId, @Valid @RequestBody TaskCreateDTO dto) {
		userService.getByIdOrThrow(userId);
		
		ToDoList existingList = listService.getListByIdOrThrow(listId);
		if(!existingList.getUser().getId().equals(userId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		Task existingTask = taskService.getByIdOrThrow(taskId);
		if(!existingTask.getToDoList().getId().equals(listId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		Task entity = TaskMapper.toEntity(dto);
		entity.setToDoList(existingList);
		
		Task updated = taskService.updateTask(taskId, entity);
		
		TaskDTO responseDto  = TaskMapper.toDTO(updated);
		return ResponseEntity.ok().body(responseDto);
	}
	
	@DeleteMapping("/{taskId}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long userId, @PathVariable Long listId, @PathVariable Long taskId) {
		userService.getByIdOrThrow(userId);
		
		ToDoList existingList = listService.getListByIdOrThrow(listId);
		if(!existingList.getUser().getId().equals(userId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
		}
		
		Task existingTask = taskService.getByIdOrThrow(taskId);
		if(!existingTask.getToDoList().getId().equals(listId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		taskService.deleteTask(taskId);
		return ResponseEntity.noContent().build();
	}
}
