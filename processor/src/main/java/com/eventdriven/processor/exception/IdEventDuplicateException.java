package com.eventdriven.processor.exception;

public class IdEventDuplicateException extends RuntimeException {

	private String event;
	public IdEventDuplicateException(String message,String event) {
		super(message);
		this.event = event;
	}
}
