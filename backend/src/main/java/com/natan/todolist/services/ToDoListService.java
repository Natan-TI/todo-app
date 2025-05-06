package com.natan.todolist.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natan.todolist.entities.ToDoList;
import com.natan.todolist.entities.User;
import com.natan.todolist.repositories.ToDoListRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ToDoListService {
	
	private final ToDoListRepository toDoListRepository;
	
	@Autowired
	public ToDoListService(ToDoListRepository toDoListRepository) {
		this.toDoListRepository = toDoListRepository;
	}
	
	public ToDoList createList(ToDoList toDoList, User user) {
		toDoList.setUser(user);
		return toDoListRepository.save(toDoList);
	}
	
	public ToDoList getListByIdOrThrow(Long id) {
		return toDoListRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lista não encontrada! ID: " + id));
	}
	
	public List<ToDoList> getListsByUserId(User user) {
		Long userId = user.getId();
		return toDoListRepository.findByUserId(userId);
	}
	
	public void deleteList(Long id) {
		if (!toDoListRepository.existsById(id)) {
			throw new EntityNotFoundException("Lista não encontrada com o ID: " + id);
		}
		toDoListRepository.deleteById(id);
	}
	
	public ToDoList updateList(Long id, ToDoList toDoList) {
		ToDoList entity = toDoListRepository.getReferenceById(id);
		updateData(entity, toDoList);
		return toDoListRepository.save(entity);
	}
	
	private void updateData(ToDoList entity, ToDoList toDoList) {
		entity.setTitle(toDoList.getTitle());
		entity.setDescription(toDoList.getDescription());
		entity.setCreatedAt(toDoList.getCreatedAt());
	}
}
