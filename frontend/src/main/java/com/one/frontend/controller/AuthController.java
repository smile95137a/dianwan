package com.one.frontend.controller;

import com.one.frontend.dto.JWTAuthResponse;
import com.one.frontend.dto.LoginDto;
import com.one.frontend.dto.LoginResponse;
import com.one.frontend.service.AuthService;
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
    public ResponseEntity<JWTAuthResponse> authenticate(@RequestBody LoginDto loginDto){
        LoginResponse loginResponse = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(loginResponse.getToken());
        jwtAuthResponse.setUserId(loginResponse.getId());
        jwtAuthResponse.setUsername(loginResponse.getUsername());

        return ResponseEntity.ok(jwtAuthResponse);
    }


    @GetMapping("/oauth2/google/success")
    public void googleLoginSuccess(HttpServletResponse response, @AuthenticationPrincipal OidcUser oidcUser) throws IOException {
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String googleId = oidcUser.getSubject();
        String authorizationCode = oidcUser.getIdToken().getTokenValue();

        LoginResponse loginResponse = authService.googleLogin(email, name, googleId);
        String accessToken = loginResponse.getToken();

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.sendRedirect("https://c01b-2402-7500-4dc-948-7df7-96b-239b-ae80.ngrok-free.app/oauth2/callback?code=" + authorizationCode);
    }


    @PostMapping("/oauth2/callback")
    public ResponseEntity<?> handleOAuth2Callback(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");

        if (code == null) {
            return ResponseEntity.badRequest().body("Authorization code is missing");
        }

        try {
            String tokenEndpoint = "https://oauth2.googleapis.com/token";
            String clientId = AuthController.clientId;
            String clientSecret = AuthController.clientSecret;
            String redirectUri = AuthController.redirectUri;

            String requestBody = String.format(
                    "code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
                    code, clientId, clientSecret, redirectUri
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(tokenEndpoint, HttpMethod.POST, request, Map.class);
            Map<String, Object> responseBody = response.getBody();

            String accessToken = (String) responseBody.get("access_token");
            String idToken = (String) responseBody.get("id_token");

            String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);
            HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);
            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, userInfoRequest, Map.class);
            Map<String, Object> userInfo = userInfoResponse.getBody();

            // Return the response with access token and user info
            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "userId", userInfo.get("sub"),
                    "username", userInfo.get("name")
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing OAuth2 callback");
        }
    }

}