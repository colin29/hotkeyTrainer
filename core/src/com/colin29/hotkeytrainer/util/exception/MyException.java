package com.colin29.hotkeytrainer.util.exception;

public class MyException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ErrorCode code;
	public MyException(ErrorCode code){
		this.code = code;
	}


}
