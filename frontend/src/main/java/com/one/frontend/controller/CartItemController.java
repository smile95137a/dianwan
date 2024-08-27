package com.one.frontend.controller;

import com.one.frontend.config.security.CustomUserDetails;
import com.one.frontend.model.ApiResponse;
import com.one.frontend.model.CartItem;
import com.one.frontend.repository.StoreProductRepository;
import com.one.frontend.service.CartItemService;
import com.one.frontend.service.CartService;
import com.one.frontend.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartItem")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
        public ResponseEntity<ApiResponse<String>> addCartItem(@RequestBody CartItem cartItem , Authentication authentication) {
            try {
                //取得使用者ID
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                Long userId = userDetails.getId();

                //透過userId搜尋cart
                Long cartId = cartService.getCartIdByUserId(userId);
                cartItem.setCartId(cartId);
                cartItemService.addCartItem(cartItem);
                ApiResponse<String> response = ResponseUtils.success(201, "商品成功加到購物車", null);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } catch (Exception e) {
                ApiResponse<String> response = ResponseUtils.failure(500, "商品新增失敗", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<String>> updateCartItem(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        try {
            CartItem cartItem = cartItemService.getCartItemById(id);
            if (cartItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.failure(404, "購物車選項不存在", null));
            }

            cartItemService.updateCartItemQuantity(id, quantity);

            ApiResponse<String> response = ResponseUtils.success(200, "購物車已更新", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = ResponseUtils.failure(500, "更新購物車發生錯誤", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




}
