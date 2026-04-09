package com.javaweb.customException;

public class FieldRequiredException extends RuntimeException {

	public FieldRequiredException(String messager) {
		super(messager);
	}
	
}
