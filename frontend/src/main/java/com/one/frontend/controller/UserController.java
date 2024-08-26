package com.one.frontend.controller;

import com.one.frontend.model.ApiResponse;
import com.one.frontend.request.UserReq;
import com.one.frontend.response.UserRes;
import com.one.frontend.service.UserService;
import com.one.frontend.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "使用者管理", description = "與使用者相關的操作")
public class UserController {

    @Autowired
    private UserService userService;

    /*
    只能拿到自己的 不能拿別人的 使用token去帶userId 從CustomUserDetails拿到要的資訊
     */
    @Operation(summary = "通過 ID 獲取使用者", description = "根據其 ID 獲取使用者")
    @GetMapping("/{userId}")
    public ResponseEntity<com.one.frontend.model.ApiResponse<UserRes>> getUserById(
            @Parameter(description = "使用者的 ID", example = "1") @PathVariable Integer userId) {
        UserRes user = userService.getUserById(userId);
        if (user == null) {
            com.one.frontend.model.ApiResponse<UserRes> response = ResponseUtils.failure(404, null, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        com.one.frontend.model.ApiResponse<UserRes> response = ResponseUtils.success(200, null, user);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserRes>> registerUser(@RequestBody UserReq userReq) throws Exception {
        UserRes userRes = userService.registerUser(userReq);
        ApiResponse<UserRes> response = ResponseUtils.success(201, null, userRes);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
    只能拿到自己的 不能拿別人的 使用token去帶userId 從CustomUserDetails拿到要的資訊
     */
    @Operation(summary = "更新現有使用者", description = "根據 ID 更新現有的使用者")
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserRes>> updateUser(
            @Parameter(description = "使用者的 ID", example = "1") @PathVariable Integer userId,
            @Parameter(description = "要更新的使用者詳細信息") @RequestBody UserReq user) {
        UserRes existingUser = userService.getUserById(userId);
        if (existingUser != null) {
            UserRes userRes = userService.updateUser(user , userId);
            ApiResponse<UserRes> response = ResponseUtils.success(201, null, userRes);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            com.one.frontend.model.ApiResponse<UserRes> response = ResponseUtils.failure(404, null, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
