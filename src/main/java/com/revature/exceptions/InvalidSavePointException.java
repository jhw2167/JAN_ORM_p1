package com.revature.exceptions;

public class InvalidSavePointException extends Exception {

	public InvalidSavePointException(String string) {
		System.out.println(string);
	}

	private static final long serialVersionUID = 1L;

}
