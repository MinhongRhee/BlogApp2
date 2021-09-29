package com.cos.blogapp2.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class LoginReqDto {
	private String username;
	private String password;
}
