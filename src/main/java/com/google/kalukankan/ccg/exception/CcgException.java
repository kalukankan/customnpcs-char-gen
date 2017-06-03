package com.google.kalukankan.ccg.exception;

public class CcgException extends RuntimeException {

	public CcgException() {
		super();
	}

	public CcgException(String message) {
		super(message);
	}

	public CcgException(Throwable cause) {
		super(cause);
	}

	public CcgException(String message, Throwable cause) {
		super(message, cause);
	}
}
