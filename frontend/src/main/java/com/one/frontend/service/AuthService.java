package com.one.frontend.service;


import com.one.frontend.dto.LoginDto;
import com.one.frontend.dto.LoginResponse;
import com.one.frontend.model.User;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.util.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    public AuthService(
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.getUserByUserName(loginDto.getUsername());

        return new LoginResponse(token, Long.valueOf(user.getId()), user.getUsername());
    }

        public LoginResponse googleLogin(String email, String name, String googleId) {
            User user = userRepository.findByGoogleId(googleId);
            System.out.println(googleId);
            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setUsername(name);
                user.setGoogleId(googleId);
                user.setPassword(passwordEncoder.encode("default_password")); // 设置默认密码或随机密码
                user.setCreatedAt(LocalDateTime.now());
                userRepository.createGoogleUser(user);
            }
            User userRes = userRepository.getUserByEmail(email);

            String jwt = jwtTokenProvider.generateToken(userRes.getId().toString());

            return new LoginResponse(jwt, Long.valueOf(userRes.getId()), userRes.getUsername());
        }
}