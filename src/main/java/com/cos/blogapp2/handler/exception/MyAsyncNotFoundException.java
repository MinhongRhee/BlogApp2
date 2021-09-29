package com.cos.blogapp2.handler.exception;

/**
 * 
 * @author 이민홍 2021.09.16
 * 1. id를 못찾았을 때 사용
 * 
 */
public class MyAsyncNotFoundException extends RuntimeException {

	public MyAsyncNotFoundException(String msg) {
		super(msg);
	}
}
