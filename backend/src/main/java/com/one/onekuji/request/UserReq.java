package com.one.onekuji.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserReq {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String address;
}
