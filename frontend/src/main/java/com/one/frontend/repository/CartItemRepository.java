package com.one.frontend.repository;

import com.one.frontend.model.CartItem;
import com.one.frontend.request.CartItemReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartItemRepository {

    @Insert("INSERT INTO cart_item (cart_id, store_product_id,store_product_name, quantity, unit_price, total_price , is_pay , image_url) " +
            "VALUES (#{cartItem.cartId}, #{cartItem.storeProductId},#{cartItem.storeProductName}, #{cartItem.quantity}, #{cartItem.unitPrice}, #{cartItem.totalPrice} , fasle , #{cartItem.imageUrl}")
    void addCartItem(@Param("cartItem") CartItemReq cartItem);

    @Select("SELECT * FROM cart_item WHERE cart_item_id = #{cartItemId}")
    CartItem findById(@Param("cartItemId") Long cartItemId);

    @Update("UPDATE cart_item SET quantity = #{cartItem.quantity}, total_price = #{cartItem.totalPrice} WHERE cart_item_id = #{cartItem.cartItemId}")
    void updateCartItem(@Param("cartItem") CartItem cartItem);


    List<CartItem> findByUserUidAndIsPayFalse(String userUid);
}
