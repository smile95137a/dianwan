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
	public boolean addCartItem(Long cartId, CartItemReq req) {
	    try {
	        StoreProductRes productRes = storeProductService.getStoreProductByProductCode(req.getProductCode());
	        if (productRes == null) {
	            throw new RuntimeException("Product not found while adding cart item");
	        }

	        // 确定使用 specialPrice 还是 regular price
	        BigDecimal unitPrice = productRes.getIsSpecialPrice() != null && productRes.getIsSpecialPrice()
	                ? productRes.getSpecialPrice()
	                : productRes.getPrice();

	        CartItem existingCartItem = cartItemRepository.findByCartIdAndStoreProductId(cartId, productRes.getStoreProductId());

	        if (existingCartItem != null) {
	            int newQuantity = existingCartItem.getQuantity() + req.getQuantity();
	            BigDecimal newTotalPrice = unitPrice.multiply(BigDecimal.valueOf(newQuantity));

	            existingCartItem.setQuantity(newQuantity);
	            existingCartItem.setTotalPrice(newTotalPrice);

	            cartItemRepository.updateCartItem(existingCartItem);
	        } else {
	            var cartItem = CartItem.builder()
	                    .cartId(cartId)
	                    .storeProductId(productRes.getStoreProductId())
	                    .quantity(req.getQuantity())
	                    .unitPrice(unitPrice)
	                    .totalPrice(unitPrice.multiply(BigDecimal.valueOf(req.getQuantity())))
	                    .isSelected(true)
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
