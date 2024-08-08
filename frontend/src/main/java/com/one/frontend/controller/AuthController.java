package com.one.frontend.controller;

import com.one.frontend.dto.JWTAuthResponse;
import com.one.frontend.dto.LoginDto;
import com.one.frontend.dto.LoginResponse;
import com.one.frontend.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public void googleLoginSuccess(HttpServletResponse response, @AuthenticationPrincipal OidcUser oidcUser) throws IOException, IOException {
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String googleId = oidcUser.getSubject();

        // 处理 Google 登录并获取 JWT
        LoginResponse loginResponse = authService.googleLogin(email, name, googleId);

        // 重定向到前端应用，附带 accessToken
        String redirectUrl = "http://localhost:5173/home";
        response.sendRedirect(redirectUrl);
    }
}