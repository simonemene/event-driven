package com.eventdriven.dlq.mapper;

import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.entity.OrderDlqEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderDlqMapper {

	public OrderDlqEntity toEntity(OrderDto orderDto)
	{
		return new OrderDlqEntity(orderDto.nameOrder(),orderDto.costOrder());
	}
}
