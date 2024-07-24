package com.one.onekuji.dto;

public class LoginDto {
    private String username;
    private String password;

    // 默认构造函数
    public LoginDto() {}

    // 带参数的构造函数
    public LoginDto(String usernameOrEmail, String password) {
        this.username = usernameOrEmail;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
