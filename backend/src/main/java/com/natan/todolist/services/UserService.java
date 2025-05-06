package com.natan.todolist.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.natan.todolist.entities.User;
import com.natan.todolist.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
	
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User createUser(User user) {
		String hashed = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);
		return userRepository.save(user);
	}
	
	public Optional<User> findById(Long id){
		return userRepository.findById(id);
	}
	
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User getByIdOrThrow(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> 
					new EntityNotFoundException("Usuário não encontrado com o ID: " + id)
				);
	}
	
	public User update(Long id, User user) {
		User entity = userRepository.getReferenceById(id);
		updateData(entity, user);
		if (!user.getPassword().equals(entity.getPassword())) {
            entity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
		return userRepository.save(entity);
	}
	
	public void deleteById(Long id) {
		if (!userRepository.existsById(id)) {
	        throw new EntityNotFoundException("Usuário não encontrado com o ID: " + id);
	    }
		userRepository.deleteById(id);
	}
	
	private void updateData(User entity, User user) {
		entity.setName(user.getName());
		entity.setEmail(user.getEmail());
		entity.setPassword(user.getPassword());
	}
}
