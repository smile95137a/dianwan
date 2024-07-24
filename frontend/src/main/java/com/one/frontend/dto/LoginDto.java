package com.one.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDto {
    private String username;
    private String password;

    // 默认构造函数
    public LoginDto() {}

    // 带参数的构造函数


}
