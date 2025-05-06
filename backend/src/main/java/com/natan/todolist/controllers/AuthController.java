package com.natan.todolist.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natan.todolist.dto.login.LoginRequestDTO;
import com.natan.todolist.dto.login.LoginResponseDTO;
import com.natan.todolist.security.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO credentials) {
		Authentication authentication = authManager.authenticate(
			new UsernamePasswordAuthenticationToken(
	            credentials.getEmail(),
	            credentials.getPassword()
	        )
	    );
		
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(userDetails);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }
}
