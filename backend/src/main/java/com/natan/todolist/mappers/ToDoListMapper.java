package com.natan.todolist.mappers;

import com.natan.todolist.dto.todolist.ToDoListCreateDTO;
import com.natan.todolist.dto.todolist.ToDoListDTO;
import com.natan.todolist.entities.ToDoList;

public class ToDoListMapper {
	public static ToDoListDTO toDTO(ToDoList toDoList) {
		ToDoListDTO dto = new ToDoListDTO();
		dto.setId(toDoList.getId());
		dto.setTitle(toDoList.getTitle());
		dto.setDescription(toDoList.getDescription());
		dto.setCreatedAt(toDoList.getCreatedAt());
		return dto;
	}
	
	public static ToDoList toEntity(ToDoListCreateDTO dto) {
		ToDoList entity = new ToDoList();
		entity.setTitle(dto.getTitle());
		entity.setDescription(dto.getDescription());
		return entity;
	}
}
