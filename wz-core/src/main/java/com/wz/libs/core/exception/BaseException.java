package com.wz.libs.core.exception;

public class BaseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public BaseException() {
		super();
	}
	
	public BaseException(String msg) {
		super(msg);
	}
	
	public BaseException(Throwable ex) {
		super(ex);
	}
	
	public BaseException(String msg,Throwable ex) {
		super(msg,ex);
	}

}
