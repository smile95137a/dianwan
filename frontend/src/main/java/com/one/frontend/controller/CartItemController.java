package com.one.frontend.controller;

import com.one.frontend.model.ApiResponse;
import com.one.frontend.model.CartItem;
import com.one.frontend.repository.StoreProductRepository;
import com.one.frontend.service.CartItemService;
import com.one.frontend.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartItem")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Operation(summary = "添加购物车项", description = "将商品添加到购物车中")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addCartItem(@RequestBody CartItem cartItem) {
        try {
            cartItemService.addCartItem(cartItem);
            ApiResponse<String> response = ResponseUtils.success(201, "商品已成功添加到购物车", null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<String> response = ResponseUtils.failure(500, "添加商品到购物车时发生错误", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "更新购物车项", description = "根据购物车项 ID 更新购物车中的商品数量")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<String>> updateCartItem(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        try {
            CartItem cartItem = cartItemService.getCartItemById(id);
            if (cartItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.failure(404, "购物车项不存在", null));
            }

            // 处理数量更新
            cartItemService.updateCartItemQuantity(id, quantity);

            ApiResponse<String> response = ResponseUtils.success(200, "购物车项已更新", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = ResponseUtils.failure(500, "更新购物车项时发生错误", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




}
