package com.eventdriven.source.mapper;

import com.eventdriven.source.dto.MessageEventDto;
import com.eventdriven.source.entity.MessageEntity;
import com.eventdriven.source.enums.StatusEnum;
import com.eventdriven.source.properties.CloudConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class MessageMapper {

	private final CloudConfig cloudConfig;

	public MessageEntity toDto(MessageEventDto dto)
	{
		return new MessageEntity(
				UUID.randomUUID().toString(),
				cloudConfig.nameBridge(),
				StatusEnum.NEW.getValue(),
				null,
				Instant.now(),
				0,
				dto.payload()
		);
	}
}
