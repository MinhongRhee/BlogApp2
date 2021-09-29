package com.cos.blogapp2.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp2.handler.exception.MyNotFoundException;
import com.cos.blogapp2.util.Script;
import com.cos.blogapp2.handler.exception.MyAsyncNotFoundException;
import com.cos.blogapp2.web.dto.CMRespDto;


@ControllerAdvice
public class GlobalExceptionHandler {

	// 일반 요청
	@ExceptionHandler(value = MyNotFoundException.class)
	public @ResponseBody String error1(MyNotFoundException e) {
		System.out.println("오류 터졌어 : " + e.getMessage());
		return Script.href("/", e.getMessage());
	}
	
	// fetch 요청 (데이터를 응답받아야 할 때)
	@ExceptionHandler(value = MyAsyncNotFoundException.class)
	public @ResponseBody CMRespDto error2(MyAsyncNotFoundException e) {
		System.out.println("오류 터졌어 : " + e.getMessage());
		return new CMRespDto(-1, e.getMessage(), null);
	}
}
