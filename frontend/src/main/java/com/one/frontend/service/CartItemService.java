package com.one.frontend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.one.frontend.model.CartItem;
import com.one.frontend.repository.CartItemRepository;
import com.one.frontend.request.CartItemReq;
import com.one.frontend.response.StoreProductRes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService {

	private final CartItemRepository cartItemRepository;

	private final StoreProductService storeProductService;

    @Transactional(rollbackFor = Exception.class)
    public boolean addCartItem(CartItemReq req) {
        try {
            StoreProductRes productRes = storeProductService.getStoreProductById(req.getStoreProductId());
            if (productRes == null) {
                throw new RuntimeException("Product not found while adding cart item");
            }

            CartItem existingCartItem = cartItemRepository.findByCartIdAndStoreProductId(req.getCartId(), req.getStoreProductId());

            if (existingCartItem != null) {
                int newQuantity = existingCartItem.getQuantity() + req.getQuantity();
                BigDecimal newTotalPrice = productRes.getPrice().multiply(BigDecimal.valueOf(newQuantity));

                existingCartItem.setQuantity(newQuantity);
                existingCartItem.setTotalPrice(newTotalPrice);

                cartItemRepository.updateCartItem(existingCartItem);
            } else {
                var cartItem = CartItem.builder()
                        .cartId(req.getCartId())
                        .storeProductId(productRes.getStoreProductId())
                        .storeProductName(productRes.getProductName())
                        .quantity(req.getQuantity())
                        .unitPrice(productRes.getPrice())
                        .totalPrice(productRes.getPrice().multiply(BigDecimal.valueOf(req.getQuantity())))
                        .build();

                cartItemRepository.addCartItem(cartItem);
            }

            return true;

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while adding cart item", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean removeCartItem(Long cartId, Long cartItemId) {
        try {
            cartItemRepository.deleteCartItem(cartId, cartItemId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove cart item", e);
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public boolean removeCartItems(List<Long> cartItemIds, Long cartId) {
        try {
            cartItemRepository.deleteCartItems(cartId, cartItemIds);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove cart item", e);
        }
    }
    
    
    public List<CartItem> findByCartIdAndCartItemList(Long cartId, List<Long> cartItemIds) {
        return cartItemRepository.findByCartIdAndCartItemList(cartId, cartItemIds);
    }

}
