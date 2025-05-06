package com.natan.todolist.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.natan.todolist.entities.ToDoList;

@Repository
public interface ToDoListRepository extends JpaRepository<ToDoList, Long>{
	
	List<ToDoList> findByUserId(Long userId);
}
