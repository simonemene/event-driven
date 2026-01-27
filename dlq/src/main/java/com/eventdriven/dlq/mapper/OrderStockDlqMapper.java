package com.eventdriven.dlq.mapper;

import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.entity.OrderStockDlqEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderStockDlqMapper {

	public OrderStockDlqEntity toEntity(OrderAvailableDto orderDto)
	{
		return new OrderStockDlqEntity(orderDto.order().nameOrder(),orderDto.order().costOrder(),orderDto.avaiable());
	}
}
