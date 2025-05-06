package com.natan.todolist.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.natan.todolist.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);
}
