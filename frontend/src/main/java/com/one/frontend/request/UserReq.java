package com.one.frontend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReq {

    private Long userId;
    private String userName;
    private String password;
    private String natId;
    private String nickName;
    private String email;
    private String phoneNumber;
    private String address;
}
