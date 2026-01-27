package com.eventdriven.dlq.mapper;

import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.entity.OrderStockDlqEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderStockDlqMapper {

	public OrderStockDlqEntity toEntity(OrderAvailableDto orderDto)
	{
		return new OrderStockDlqEntity(
				orderDto.order().nameOrder(),orderDto.order().costOrder(),orderDto.avaiable(),orderDto.notification()
		);
	}
	public OrderAvailableDto toDto(OrderStockDlqEntity orderDlqEntity)
	{
		return new OrderAvailableDto(new OrderDto(orderDlqEntity.getIdEvent(),
				orderDlqEntity.getName(),
				orderDlqEntity.getCost()),
				orderDlqEntity.getAvailable());
	}

	public List<OrderAvailableDto> toListDto(List<OrderStockDlqEntity> listEntity)
	{
		return listEntity.stream().map(this::toDto).toList();
	}

}
