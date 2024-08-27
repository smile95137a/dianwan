package com.one.frontend.service;

import com.one.frontend.model.CartItem;
import com.one.frontend.model.StoreProduct;
import com.one.frontend.repository.CartItemRepository;
import com.one.frontend.repository.StoreProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    public void addCartItem(CartItem cartItem) {
        cartItemRepository.addCartItem(cartItem);
    }

    public void updateCartItemQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem == null) {
            throw new IllegalArgumentException("購物車選項不存在");
        }

        StoreProduct storeProduct = storeProductRepository.findId(cartItem.getStoreProductId());
        if (storeProduct == null) {
            throw new IllegalArgumentException("商品不存在");
        }

        BigDecimal unitPrice = storeProduct.getPrice();
        BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(quantity));

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(totalPrice);
        cartItemRepository.updateCartItem(cartItem);
    }

    public CartItem getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }
}
