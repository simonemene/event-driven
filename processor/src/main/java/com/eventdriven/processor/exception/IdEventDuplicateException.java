package com.eventdriven.processor.exception;

public class IdEventDuplicateException extends RuntimeException {
	public IdEventDuplicateException(String message) {
		super(message);
	}
}
