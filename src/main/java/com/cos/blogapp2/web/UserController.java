package com.cos.blogapp2.web;

import javax.servlet.http.HttpSession;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.blogapp2.domain.user.User;
import com.cos.blogapp2.domain.user.UserRepository;
import com.cos.blogapp2.util.SHA;
import com.cos.blogapp2.web.dto.JoinReqDto;
import com.cos.blogapp2.web.dto.LoginReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserRepository userRepository;
	private final HttpSession session;
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/";
	}
	
	@PostMapping("/login")
	public String login(LoginReqDto dto) { // username=ssar&password=1234
		
		String encPassword = SHA.encrypt(dto.getPassword());
		User principal =  userRepository.mLogin(dto.getUsername(), encPassword);
		
		if(principal == null) {
			return "redirect:/loginForm";
		}else {
			session.setAttribute("principal", principal);
			return "redirect:/";
		}
	}
	
	
	@PostMapping("/join")
	public String join(JoinReqDto dto) { // username=ssar&password=1234&email=ssar@nate.com
//		System.out.println("=============");
//		System.out.println(dto.getUsername());
//		System.out.println(dto.getPassword());
//		System.out.println(dto.getEmail());
//		System.out.println("=============");
		
		String encPasword = SHA.encrypt(dto.getPassword()); // 오른쪽 해쉬 <- 왼쪽 1234
		dto.setPassword(encPasword);
		User user = dto.toEntity();
		userRepository.save(user);
		
		return "redirect:/loginForm";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	
	@GetMapping("/user/{id}")
	public String userInfo(@PathVariable int id) {
		return "user/updateForm";
	}
}


