package com.one.frontend.controller;

import com.one.frontend.model.Role;
import com.one.frontend.model.User;
import com.one.frontend.model.VerificationToken;
import com.one.frontend.repository.RoleRepository;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping
public class VerificationController {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/verify")
    public String verifyUser(@RequestParam String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);

        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Verification token is invalid or expired.";
        }
        Role roles = roleRepository.findByName("REGULAR_MEMBER");
        // 查找用户并升级为正式会员
        User user = userRepository.getUserBId(verificationToken.getUserId());
        if (user != null) {
            user.setRoleId(roles.getId());
            userRepository.updateUserRoleId(user);
        }

        // 删除 token
        tokenRepository.delete(verificationToken);

        return "Your account has been successfully verified!";
    }
}
