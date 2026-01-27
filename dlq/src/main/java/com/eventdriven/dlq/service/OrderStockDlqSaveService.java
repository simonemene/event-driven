package com.eventdriven.dlq.service;

import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.mapper.OrderStockDlqMapper;
import com.eventdriven.dlq.repository.OrderStockDlqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderStockDlqSaveService implements IOrderStockDlqSaveService{

	private final OrderStockDlqRepository repository;

	private final OrderStockDlqMapper mapper;

	@Override
	public void saveMessage(OrderAvailableDto message) {
		log.info("Save message {} ", message.order().nameOrder());
		repository.save(mapper.toEntity(message));


	}
}
