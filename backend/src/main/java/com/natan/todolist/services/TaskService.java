package com.natan.todolist.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natan.todolist.entities.Task;
import com.natan.todolist.entities.ToDoList;
import com.natan.todolist.entities.User;
import com.natan.todolist.repositories.TaskRepository;
import com.natan.todolist.repositories.ToDoListRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {
	
	private final TaskRepository taskRepository;
	private final ToDoListRepository toDoListRepository;
	
	@Autowired
	public TaskService(TaskRepository taskRepository, ToDoListRepository toDoListRepository) {
		this.taskRepository = taskRepository;
		this.toDoListRepository = toDoListRepository;
	}
	
	public Task createTask(Task task, ToDoList toDoList) {
		task.setToDoList(toDoList);
		return taskRepository.save(task);
	}
	
	public List<Task> getTasksByListId(ToDoList toDoList){
		Long toDoListId = toDoList.getId();
		return taskRepository.findByToDoListId(toDoListId);
	}
	
	public Task getByIdOrThrow(Long id) {
		return taskRepository.findById(id)
				.orElseThrow(() -> 
					new EntityNotFoundException("Tarefa não encontrado com o ID: " + id)
				);
	}
	
	public void deleteTask(Long id) {
		if (!taskRepository.existsById(id)) {
			throw new EntityNotFoundException("Tarefa não encontrada com o ID: " + id);
		}
		taskRepository.deleteById(id);
	}
	
	public Task updateTask(Long id, Task task) {
		Task entity = taskRepository.getReferenceById(id);
		updateData(entity, task);
		return taskRepository.save(entity);
	}
	
	private void updateData(Task entity, Task task) {
		entity.setTitle(task.getTitle());
		entity.setDescription(task.getDescription());
		entity.setCreatedAt(task.getCreatedAt());
		entity.setDueDate(task.getDueDate());
		entity.setStatus(task.getStatus());
	}
}
