package com.one.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private Long id;
    private String username;

    public LoginResponse(String token, Long id, String username) {
        this.token = token;
        this.id = id;
        this.username = username;
    }
}