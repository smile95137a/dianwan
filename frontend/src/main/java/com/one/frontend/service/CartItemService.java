package com.one.frontend.service;

import com.one.frontend.model.CartItem;
import com.one.frontend.model.StoreProduct;
import com.one.frontend.repository.CartItemRepository;
import com.one.frontend.repository.StoreProductRepository;
import com.one.frontend.request.CartItemReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    public void addCartItem(CartItemReq cartItem) {
        cartItemRepository.addCartItem(cartItem);
    }

    public CartItem updateCartItemQuantity(Long cartItemId, Integer quantity) {
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
        CartItem result = cartItemRepository.findById(cartItemId);
        return result;
    }

    public CartItem getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }

    public List<CartItem> findByUserUidAndIsPayFalse(String userUid) {
        return cartItemRepository.findByUserUidAndIsPayFalse(userUid);
    }

    public void updateIsPayToTrue(CartItem item) {
    }
}
