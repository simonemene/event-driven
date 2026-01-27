package com.eventdriven.source.mapper;

import com.eventdriven.source.dto.MessageEventDto;
import com.eventdriven.source.entity.MessageEntity;
import com.eventdriven.source.enums.StatusEnum;
import com.eventdriven.source.properties.CloudConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageMapper {

	private final CloudConfig cloudConfig;

	public MessageEntity toDto(MessageEventDto dto)
	{
		return new MessageEntity(
				dto.eventId(),
				cloudConfig.nameBridge(),
				StatusEnum.NEW,
				null,
				0,
				dto.payload()
		);
	}
}
