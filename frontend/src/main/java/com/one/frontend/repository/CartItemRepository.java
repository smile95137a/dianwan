package com.one.frontend.repository;

import com.one.frontend.model.CartItem;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CartItemRepository {

    @Insert("INSERT INTO cart_item (cart_id, store_product_id, quantity, unit_price, total_price, product_detail_id) " +
            "VALUES (#{cartItem.cartId}, #{cartItem.storeProductId}, #{cartItem.quantity}, #{cartItem.unitPrice}, #{cartItem.totalPrice}, #{cartItem.productDetailId})")
    void addCartItem(@Param("cartItem") CartItem cartItem);

    @Select("SELECT * FROM cart_item WHERE cart_item_id = #{cartItemId}")
    CartItem findById(@Param("cartItemId") Long cartItemId);

    @Update("UPDATE cart_item SET quantity = #{cartItem.quantity}, total_price = #{cartItem.totalPrice} WHERE cart_item_id = #{cartItem.cartItemId}")
    void updateCartItem(@Param("cartItem") CartItem cartItem);


}
