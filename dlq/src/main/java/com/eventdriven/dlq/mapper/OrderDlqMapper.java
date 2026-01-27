package com.eventdriven.dlq.mapper;

import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.entity.OrderDlqEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDlqMapper {

	public OrderDlqEntity toEntity(OrderDto orderDto)
	{
		return new OrderDlqEntity(orderDto.id(),orderDto.nameOrder(),orderDto.costOrder(),
				orderDto.notification());
	}

	public OrderDto toDto(OrderDlqEntity orderDlqEntity)
	{
		return new OrderDto(orderDlqEntity.getIdEvent(),orderDlqEntity.getName(),orderDlqEntity.getCost());
	}

	public List<OrderDto> toListDto(List<OrderDlqEntity> listEntity)
	{
		return listEntity.stream().map(this::toDto).toList();
	}
}
