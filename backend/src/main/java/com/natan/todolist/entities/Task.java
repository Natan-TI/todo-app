package com.natan.todolist.entities;

import java.time.LocalDate;

import com.natan.todolist.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_task")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	
	private LocalDate dueDate;
	private LocalDate createdAt = LocalDate.now();
	
	@ManyToOne
	@JoinColumn(name = "todolist_id", nullable = false)
	private ToDoList toDoList;
}
