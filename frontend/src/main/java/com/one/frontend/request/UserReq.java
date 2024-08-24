package com.one.frontend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReq {
    private String username;
    private String password;
    private String natId;
    private String nickname;
    private String email;
    private String phoneNumber;
    private String address;
}
