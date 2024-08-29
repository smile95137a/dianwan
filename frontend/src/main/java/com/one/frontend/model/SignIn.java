package com.one.frontend.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignIn {
    @Column(name ="id")
    private Long id;
    @Column(name ="sliver_price")
    private BigDecimal sliverPrice;
    @Column(name ="probability")
    private Double probability;
    @Column(name ="number")
    private String number;
    @Column(name ="created_date")
    private LocalDateTime createdDate;
    @Column(name ="update_date")
    private LocalDateTime updateDate;

}
