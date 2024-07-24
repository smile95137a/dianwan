package com.one.onekuji.controller;

import com.one.onekuji.dto.JWTAuthResponse;
import com.one.onekuji.dto.LoginDto;
import com.one.onekuji.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    // Login REST API
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticate(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken("Bearer " + token);

        return ResponseEntity.ok(jwtAuthResponse);
    }
}