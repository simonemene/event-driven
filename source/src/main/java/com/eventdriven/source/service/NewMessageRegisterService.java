package com.eventdriven.source.service;

import com.eventdriven.source.dto.MessageEventDto;
import com.eventdriven.source.dto.OrderDto;
import com.eventdriven.source.entity.MessageEntity;
import com.eventdriven.source.mapper.MessageMapper;
import com.eventdriven.source.savemessage.MessageRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class NewMessageRegisterService implements INewMessageArrivedService{

	private final MessageRespository repository;

	private final MessageMapper mapper;

	private final ObjectMapper objectMapper;


	@Transactional
	@Override
	public void newMessageArrived(OrderDto order) {
		try {
			String event = UUID.randomUUID().toString();
			repository.save(
					mapper.toDto(new MessageEventDto("", event,
							createPayload(order,event))));
		}catch(Exception e)
		{
			log.error("JSON serialization failed",e);
		}
	}

	private String createPayload(OrderDto orderDto,String event) throws JsonProcessingException {
			ObjectNode node = objectMapper.createObjectNode();
			node.put("id",event);
			node.put("name", orderDto.name());
			node.put("cost", orderDto.cost());
		return objectMapper.writeValueAsString(node);
	}
}
