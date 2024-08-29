package com.one.frontend.controller;

import com.one.frontend.config.security.CustomUserDetails;
import com.one.frontend.config.security.SecurityUtils;
import com.one.frontend.model.ApiResponse;
import com.one.frontend.response.CartItemRes;
import com.one.frontend.service.CartService;
import com.one.frontend.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/query/{userUid}")
    public ResponseEntity<ApiResponse<List<CartItemRes>>> getCatItem(@PathVariable String userUid){
        CustomUserDetails userDetails = SecurityUtils.getCurrentUserPrinciple();

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String authenticatedUserId = userDetails.getUserUid();

        if (!authenticatedUserId.equals(userUid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<CartItemRes> cartItems = cartService.getCatItem(userUid);
        ApiResponse<List<CartItemRes>> response = ResponseUtils.success(200, null, cartItems);
        return ResponseEntity.ok(response);
    }

}
