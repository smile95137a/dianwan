package com.one.frontend.repository;

import com.one.frontend.model.Cart;
import com.one.frontend.model.CartItem;
import com.one.frontend.response.CartItemRes;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartRepository {
    @Select("select b.quantity , b.unitPrice , b.totalPrice , c.imageUrl , c.specialPrice , d.product_name , c.product_name , d.size , c.size from cart a left join cart_item b on a.cart_id = b.cart_id left join store_product c on b.store_product_id = c.store_product_id  where a.user_uid = #{userUid} and b.is_pay = false")
    List<CartItemRes> getCatItem(String userUid);
    @Insert("INSERT INTO `cart` (user_id, created_at) " +
            "VALUES (#{userId}, #{createdAt})")
    void addCart(Cart cart);
    @Select("select cart_id from cart where user_uid = #{userUid}")
    Long getCartIdByUserId(String userUid);
    @Select("select b.quantity , b.unitPrice , b.totalPrice , c.imageUrl , c.specialPrice , d.product_name , c.product_name , d.size , c.size from cart a left join cart_item b on a.cart_id = b.cart_id left join store_product c on b.store_product_id = c.store_product_id  where a.user_id = #{userId} and b.is_pay = false")
    List<CartItem> findByUserUidAndIsPayFalse(Long userId);
}
