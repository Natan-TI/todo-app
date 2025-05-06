package com.natan.todolist.mappers;

import com.natan.todolist.dto.task.TaskCreateDTO;
import com.natan.todolist.dto.task.TaskDTO;
import com.natan.todolist.entities.Task;

public class TaskMapper {
	public static TaskDTO toDTO(Task task) {
		TaskDTO dto = new TaskDTO();
		dto.setId(task.getId());
		dto.setTitle(task.getTitle());
		dto.setDescription(task.getDescription());
		dto.setCreatedAt(task.getCreatedAt());
		dto.setDueDate(task.getDueDate());
		dto.setStatus(task.getStatus());
		dto.setToDoListId(task.getToDoList().getId());
		return dto;
	}
	
	public static Task toEntity(TaskCreateDTO dto) {
		Task task = new Task();
		task.setTitle(dto.getTitle());
		task.setDescription(dto.getDescription());
		task.setStatus(dto.getStatus());
		task.setDueDate(dto.getDueDate());
		return task;
	}
}
