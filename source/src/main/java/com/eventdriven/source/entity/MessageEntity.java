package com.eventdriven.source.entity;

import com.eventdriven.source.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@Entity
@Table(name = "MESSAGE")
public class MessageEntity {

	public MessageEntity(String eventId,
			String topic,
			StatusEnum status,
			Instant sendTimestamp,
			int attempts,
			String payload)
	{
		this.eventId = eventId;
		this.topic = topic;
		this.status = status;
		this.sendTimestamp = sendTimestamp;
		this.attempts = attempts;
		this.payload = payload;
	}

	protected MessageEntity()
	{

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String eventId;

	private String topic;

	@Lob
	private String payload;

	@Enumerated(EnumType.STRING)
	private StatusEnum status;

	@Column(name = "SEND_TIMESTAMP")
	private Instant sendTimestamp;

	@CreatedDate
	@Column(name = "CREATE_TIMESTAMP")
	private Instant createTimestamp;

	private int attempts;
}
