package com.eventdriven.processor.mapper;

import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.entity.IdempotencyEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

	public IdempotencyEntity toEntity(OrderDto dto)
	{
		return new IdempotencyEntity(dto.eventiId());
	}
}
