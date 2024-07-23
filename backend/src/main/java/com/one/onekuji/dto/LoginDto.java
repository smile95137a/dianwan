package com.one.onekuji.dto;

public class LoginDto {
    private String usernameOrEmail;
    private String password;

    // 默认构造函数
    public LoginDto() {}

    // 带参数的构造函数
    public LoginDto(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    // Getter 和 Setter 方法
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
