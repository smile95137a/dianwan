package com.one.frontend.controller;

import com.one.frontend.model.ApiResponse;
import com.one.frontend.model.SignIn;
import com.one.frontend.response.SignInRes;
import com.one.frontend.service.SignInService;
import com.one.frontend.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/signIn")
public class SignInController {

    @Autowired
    private SignInService service;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SignIn>>> getAllSignIns() {
        List<SignIn> signInList = service.getAllSignIns();
        ApiResponse<List<SignIn>> response = ResponseUtils.success(200, null, signInList);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/draw")
    public ResponseEntity<ApiResponse<SignInRes>> spinWheel() {
        SignInRes signInList = service.spinWheel();
        ApiResponse<SignInRes> response = ResponseUtils.success(200, null, signInList);
        return ResponseEntity.ok(response);
    }

}
