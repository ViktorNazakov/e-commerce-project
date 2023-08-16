package com.example.ecommerce.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDto {

    private String token;

    private String tokenType = "Bearer ";

    private String role;

    private String username;

    public AuthResponseDto(String token, String username,String role) {
        this.username = username;
        this.token = token;
        this.role = role;
    }
}
