package com.one.frontend.controller;

import com.one.frontend.dto.JWTAuthResponse;
import com.one.frontend.dto.LoginDto;
import com.one.frontend.dto.LoginResponse;
import com.one.frontend.model.ApiResponse;
import com.one.frontend.model.User;
import com.one.frontend.service.AuthService;
import com.one.frontend.util.ResponseUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private static  String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private static String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private static String redirectUri;

    private final String tokenEndpoint = "https://oauth2.googleapis.com/token";

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JWTAuthResponse>> authenticate(@RequestBody LoginDto loginDto) throws Exception {
        LoginResponse loginResponse = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(loginResponse.getToken());

        User user = new User();
        user.setUsername(loginResponse.getUsername());
        user.setUserUid(loginResponse.getUserUid());
        jwtAuthResponse.setUser(user);
        ApiResponse<JWTAuthResponse> response = ResponseUtils.success(200, null, jwtAuthResponse);

        return ResponseEntity.ok(response);
    }


}