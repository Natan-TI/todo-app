package com.natan.todolist.dto.todolist;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ToDoListDTO {
	private Long id;
	private String title;
	private String description;
	private LocalDate createdAt;
}
