package com.one.frontend.controller;

import com.one.frontend.config.security.CustomUserDetails;
import com.one.frontend.config.security.SecurityUtils;
import com.one.frontend.model.ApiResponse;
import com.one.frontend.model.CartItem;
import com.one.frontend.request.CartItemReq;
import com.one.frontend.response.StoreProductRes;
import com.one.frontend.service.CartItemService;
import com.one.frontend.service.CartService;
import com.one.frontend.service.StoreProductService;
import com.one.frontend.util.ResponseUtils;
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
    private StoreProductService storeProductService;

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
        public ResponseEntity<ApiResponse<String>> addCartItem(@RequestBody CartItemReq cartItem) {
            try {
                //取得使用者ID
                CustomUserDetails userDetails = SecurityUtils.getCurrentUserPrinciple();
                if (userDetails == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                String userUid = userDetails.getUserUid();

                //透過userId搜尋cart
                Long cartId = cartService.getCartIdByUserId(userUid);
                if(cartId == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }else{
                    cartItem.setCartId(cartId);
                }
                //透過cartItem的store_product_id搜尋商品資訊
                StoreProductRes res = storeProductService.getStoreProductById(cartItem.getStoreProductId());
                if(res == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }else{
                    cartItem.setStoreProductName(res.getProductName());
                }

                cartItemService.addCartItem(cartItem);
                ApiResponse<String> response = ResponseUtils.success(201, "商品成功加到購物車", null);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } catch (Exception e) {
                ApiResponse<String> response = ResponseUtils.failure(500, "商品新增失敗", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
    /*
    每更新一次數量 就重新回傳一次價格存進DB並且回傳
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItem(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        try {
            CartItem cartItem = cartItemService.getCartItemById(id);
            if (cartItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.failure(404, "購物車選項不存在", null));
            }

            CartItem result = cartItemService.updateCartItemQuantity(id, quantity);

            ApiResponse<CartItem> response = ResponseUtils.success(200, "購物車已更新", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<CartItem> response = ResponseUtils.failure(500, "更新購物車發生錯誤", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




}
