package com.eventdriven.dlq.service;

import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.entity.OrderStockDlqEntity;
import com.eventdriven.dlq.mapper.OrderStockDlqMapper;
import com.eventdriven.dlq.repository.OrderStockDlqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderStockDlqSaveService implements IOrderSupportQueryService<OrderAvailableDto>{

	private final OrderStockDlqRepository repository;

	private final OrderStockDlqMapper mapper;

	@Override
	public List<OrderAvailableDto> getElmentDlq() {
		return mapper.toListDto(repository.findByNotificationFalse());
	}

	@Override
	public void saveMessage(OrderAvailableDto message) {
		log.info("Save message {} ", message.order().nameOrder());
		repository.save(mapper.toEntity(message));
	}

	@Override
	public void notification(OrderAvailableDto element) {
		OrderStockDlqEntity order = mapper.toEntity(element);
		repository.save(new OrderStockDlqEntity
				(order.getName(),order.getCost(),order.getAvailable(),true));
	}
}
