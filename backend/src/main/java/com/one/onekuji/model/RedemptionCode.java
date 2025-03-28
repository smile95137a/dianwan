package com.one.onekuji.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "redemption_codes")
public class RedemptionCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, unique = true, nullable = false)
    private String code;

    @Column(name = "is_redeemed", nullable = false)
    private boolean isRedeemed = false;

    @Column(name = "redeemed_at")
    private LocalDateTime redeemedAt;

    @Column(name = "user_id")
    private Long userId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isRedeemed() {
        return isRedeemed;
    }

    public void setRedeemed(boolean redeemed) {
        isRedeemed = redeemed;
    }

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(LocalDateTime redeemedAt) {
        this.redeemedAt = redeemedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
