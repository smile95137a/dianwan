package com.one.frontend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.one.frontend.config.security.CustomUserDetails;
import com.one.frontend.config.security.JwtTokenProvider;
import com.one.frontend.config.security.SecurityUtils;
import com.one.frontend.dto.LoginDto;
import com.one.frontend.dto.LoginResponse;
import com.one.frontend.model.User;
import com.one.frontend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	public LoginResponse login(LoginDto loginDto) throws Exception {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		CustomUserDetails userDetail = SecurityUtils.getCurrentUserPrinciple();
		String token = jwtTokenProvider.generateToken(userDetail);
		User user = userRepository.getUserByUserName(loginDto.getUsername());
		return new LoginResponse(token, user.getId(), user.getUsername(), user.getUserUid());
//		if (user.getRoleId() == 3 || user.getRoleId() == 1 || user.getRoleId() == 2) {
//		} else {
//			throw new Exception("不屬於認證會員，請先認證");
//		}
	}

}