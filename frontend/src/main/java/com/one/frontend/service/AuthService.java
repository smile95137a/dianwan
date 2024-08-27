package com.one.frontend.service;


import com.one.frontend.config.security.CustomUserDetails;
import com.one.frontend.config.security.JwtTokenProvider;
import com.one.frontend.config.security.SecurityUtils;
import com.one.frontend.dto.LoginDto;
import com.one.frontend.dto.LoginResponse;
import com.one.frontend.model.User;
import com.one.frontend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public LoginResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetail = SecurityUtils.getCurrentUserPrinciple();
        String token = jwtTokenProvider.generateToken(userDetail);
        User user = userRepository.getUserByUserName(loginDto.getUsername());

        return new LoginResponse(token, user.getId(), user.getUsername());
    }

        public LoginResponse googleLogin(String email, String name, String googleId) {
            User user = userRepository.findByGoogleId(googleId);
            System.out.println(googleId);
            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setUsername(name);
                user.setPassword(passwordEncoder.encode("default_password")); // 设置默认密码或随机密码
                user.setCreatedAt(LocalDateTime.now());
                userRepository.createGoogleUser(user);
            }
            User userRes = userRepository.getUserByEmail(email).get();
            var userDetail = SecurityUtils.getCurrentUserPrinciple();
            String jwt = jwtTokenProvider.generateToken(userDetail);

            return new LoginResponse(jwt, Long.valueOf(userRes.getId()), userRes.getUsername());
        }
}