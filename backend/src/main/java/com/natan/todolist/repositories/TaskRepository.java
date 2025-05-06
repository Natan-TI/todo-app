package com.natan.todolist.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.natan.todolist.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findByToDoListId(Long toDoListId);
}
