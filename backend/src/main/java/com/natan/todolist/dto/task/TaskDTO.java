package com.natan.todolist.dto.task;

import java.time.LocalDate;

import com.natan.todolist.enums.Status;

import lombok.Data;

@Data
public class TaskDTO {
	private Long id;
	private String title;
	private String description;
	private Status status;
	private LocalDate dueDate;
	private LocalDate createdAt;
	private Long toDoListId;
}
