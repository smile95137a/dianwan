package com.one.frontend.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper
public interface UserTransactionRepository {

    // 插入新的交易记录
    @Insert("INSERT INTO user_transaction (user_id, transaction_type, amount, transaction_date) " +
            "VALUES (#{userId}, #{transactionType}, #{amount}, CURRENT_TIMESTAMP)")
    void insertTransaction(@Param("userId") Long userId,
                           @Param("transactionType") String transactionType,
                           @Param("amount") BigDecimal amount);

    // 获取某个用户在指定时间段内的交易金额（消费或储值）
    @Select("SELECT COALESCE(SUM(amount), 0) FROM user_transaction " +
            "WHERE user_id = #{userId} AND transaction_type = #{transactionType} " +
            "AND transaction_date BETWEEN #{startDate} AND #{endDate}")
    BigDecimal getTotalAmountForUserAndMonth(@Param("userId") Long userId,
                                             @Param("transactionType") String transactionType,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);
}
