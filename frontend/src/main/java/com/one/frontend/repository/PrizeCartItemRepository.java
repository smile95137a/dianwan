package com.one.frontend.repository;

import com.one.frontend.model.PrizeCartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PrizeCartItemRepository {
    @Delete("DELETE FROM prize_cart_item WHERE cart_id = #{cartId} AND prize_cart_item_id = #{prizeCartItemId}")
    public void deleteCartItem(Long cartId, Long prizeCartItemId);

    @Select("select * from prize_cart_item where prize_cart_item_id = #{prizeCartItemId}")
    public PrizeCartItem findById(Long prizeCartItemId);
    @Select({
            "<script>",
            "SELECT * FROM prize_cart_item",
            "WHERE cart_id = #{cartId}",
            "AND prize_cart_item_id IN",
            "<foreach item='item' index='index' collection='prizeCartItemIds' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    List<PrizeCartItem> findByCartIdAndCartItemList(@Param("cartId")Long cartId, @Param("prizeCartItemIds")List<Long> prizeCartItemIds);
    @Delete({
            "<script>",
            "DELETE FROM prize_cart_item",
            "WHERE cart_id = #{cartId}",
            "AND cart_item_id IN",
            "<foreach item='item' collection='prizeCartItemIds' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    void deleteCartItems(@Param("cartId")Long cartId, @Param("prizeCartItemIds")List<Long> prizeCartItemIds);


    @Insert({
            "<script>",
            "INSERT INTO prize_cart_item (cart_id, product_detail_id,sliver_price, is_selected, size) VALUES ",
            "<foreach collection='cartItemList' item='item' separator=','>",
            "(#{item.cartId}, #{item.productDetailId}, #{item.sliverPrice}, #{item.isSelected}, #{item.size})",
            "</foreach>",
            "</script>"
    })
    void insertBatch(@Param("cartItemList") List<PrizeCartItem> cartItemList);
}
