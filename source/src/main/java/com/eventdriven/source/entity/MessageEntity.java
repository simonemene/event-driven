package com.eventdriven.source.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "MESSAGE")
public class MessageEntity {

	public MessageEntity(String eventId,
			String topic,
			String status,
			Instant sendTimestamp,
			Instant createTimestamp,
			int attempts,
			String payload)
	{
		this.eventId = eventId;
		this.topic = topic;
		this.status = status;
		this.sendTimestamp = sendTimestamp;
		this.createTimestamp = createTimestamp;
		this.attempts = attempts;
		this.payload = payload;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String eventId;

	private String topic;

	@Lob
	private String payload;

	private String status;

	@Column(name = "SEND_TIMESTAMP")
	private Instant sendTimestamp;

	@Column(name = "CREATE_TIMESTAMP")
	private Instant createTimestamp;

	private int attempts;
}
