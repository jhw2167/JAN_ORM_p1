package com.revature.exceptions;

public class InvalidColumnNameException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5612712094379013327L;

	public InvalidColumnNameException(String msg) {
		super(msg);
	}
}
