package com.cos.blogapp2.web.dto;

import com.cos.blogapp2.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class JoinReqDto {
	private String username;
	private String password;
	private String email;
	
	public User toEntity() {
		// 장점 : 순서안지켜도 됨, 넣고 싶은 것만 넣을 수 있다.
		User user = User.builder()
				.password(password)
				.username(username)
				.email(email)
				.build();
		return user;
	}
}



