package com.one.onekuji.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User 模型，表示系統中的用戶信息")
public class User {

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

    private Set<Role> roles;
}
