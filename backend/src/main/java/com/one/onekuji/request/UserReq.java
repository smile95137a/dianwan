package com.one.onekuji.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReq {

	private Long userId;
	private String username;
	private String password;
	private String natId;
	private String nickName;
	private String email;
	private String phoneNumber;
	private String addressName;
	private String city; 
	private String area;
	private String address;
	private String lineId;
}
