package com.one.frontend.controller;

import com.one.frontend.model.ApiResponse;
import com.one.frontend.model.Role;
import com.one.frontend.model.User;
import com.one.frontend.model.VerificationToken;
import com.one.frontend.repository.RoleRepository;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.repository.VerificationTokenRepository;
import com.one.frontend.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/verify/{token}")
    public ResponseEntity<ApiResponse<String>> verifyUser(@PathVariable String token) {
        try {
            VerificationToken verificationToken = tokenRepository.findByToken(token);

            if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                return ResponseEntity.ok(ResponseUtils.failure(500, "驗證失敗", "驗證碼可能失效，請聯繫客服"));
            }
            Role roles = roleRepository.findByName("驗證會員");
            // 查找用户并升级为正式会员
            User user = userRepository.getUserBId(verificationToken.getUserId());
            if (user != null) {
                user.setRoleId(roles.getId());
                userRepository.updateUserRoleId(user);
            }

            // 删除 token
            tokenRepository.delete(verificationToken);

            return ResponseEntity.ok(ResponseUtils.success(200, null, "你已驗證成功，恭喜加入再來一抽一番賞的會員"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
