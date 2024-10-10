package com.one.onekuji.repository;

import com.one.onekuji.model.VendorOrderEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VendorOrderRepository {

    @Insert("INSERT INTO vendor_order (vendor_order, order_no, error_code, error_message) " +
            "VALUES (#{vendorOrder}, #{orderNo}, #{errorCode}, #{errorMessage})")
    void insert(VendorOrderEntity vendorOrder);

    @Select("SELECT * FROM vendor_order WHERE vendorOrder = #{vendorOrder}")
    VendorOrderEntity findById(String vendorOrder);

    @Select("SELECT * FROM vendor_order")
    List<VendorOrderEntity> findAll();

    @Update("UPDATE vendor_order SET orderNo = #{orderNo}, errorCode = #{errorCode}, errorMessage = #{errorMessage} " +
            "WHERE vendorOrder = #{vendorOrder}")
    void update(VendorOrderEntity vendorOrder);

    @Delete("DELETE FROM vendor_order WHERE vendorOrder = #{vendorOrder}")
    void delete(String vendorOrder);
}
