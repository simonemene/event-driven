package com.eventdriven.source.dto;

public record MessageEventDto(
		String topic,
		String eventId,
		String payload
) {
}
