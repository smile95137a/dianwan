package com.one.frontend.controller;

import com.one.frontend.dto.JWTAuthResponse;
import com.one.frontend.dto.LoginDto;
import com.one.frontend.dto.LoginResponse;
import com.one.frontend.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    // Login REST API
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticate(@RequestBody LoginDto loginDto){
        LoginResponse loginResponse = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken("Bearer " + loginResponse.getToken());
        jwtAuthResponse.setUserId(loginResponse.getId());
        jwtAuthResponse.setUsername(loginResponse.getUsername());

        return ResponseEntity.ok(jwtAuthResponse);
    }


    @GetMapping("/oauth2/google/success")
    public ResponseEntity<JWTAuthResponse> googleLoginSuccess(@AuthenticationPrincipal OidcUser oidcUser) {
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String googleId = oidcUser.getSubject();

        // 在服务层中处理 Google 登录
        LoginResponse loginResponse = authService.googleLogin(email, name, googleId);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken("Bearer " + loginResponse.getToken());
        jwtAuthResponse.setUserId(loginResponse.getId());
        jwtAuthResponse.setUsername(loginResponse.getUsername());

        return ResponseEntity.ok(jwtAuthResponse);
    }
}