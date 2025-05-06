package com.natan.todolist.dto.login;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String tokenType = "Bearer";
}