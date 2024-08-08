package com.one.frontend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User 模型，表示系統中的用戶信息")
public class User {

    public enum UserType {
        USER, // 正式会员
        EXP_USER // 体验会员
    }

    @Schema(description = "用戶的 ID", example = "1")
    private Integer id;

    @Schema(description = "用戶名", example = "john_doe")
    private String username;

    @Schema(description = "用戶密碼", example = "password123")
    private String password;

    @Schema(description = "用戶的暱稱", example = "John")
    private String nickname;

    @Schema(description = "用戶的電子郵件地址", example = "john.doe@example.com")
    private String email;

    @Schema(description = "用戶的電話號碼", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "用戶的地址", example = "123 Main Street, City, Country")
    private String address;

    @Schema(description = "用戶記錄的創建時間", example = "2023-07-01T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "用戶記錄的最後更新時間", example = "2023-07-20T08:45:23")
    private LocalDateTime updatedAt;

    private Set<Role> roles = new HashSet<>();

    @Schema(description = "用戶的儲值餘額", example = "1000.00")
    private BigDecimal balance;

    @Schema(description = "用戶的紅利點數", example = "500")
    private BigDecimal bonus;

    @Schema(description = "最後儲值時間", example = "2023-07-25T10:30:00")
    private LocalDateTime lastTopUpTime;

    @Schema(description = "用户类型", example = "REGULAR")
    private UserType userType;

    @Schema(description = "用户角色 ID", example = "2")
    private Integer roleId;

    @Schema(description = "用戶狀態" , example = "Y等於啟用 N等於未啟用")
    private String status;

    private String googleId;

    private Long drawCount;
}
