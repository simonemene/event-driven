package com.eventdriven.dlq.service;

import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.mapper.OrderDlqMapper;
import com.eventdriven.dlq.repository.OrderDlqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderDlqSaveService implements IOrderDlqSaveService{

	private final OrderDlqRepository repository;

	private final OrderDlqMapper orderDlqMapper;

	@Override
	public void saveMessage(OrderDto message) {
		log.info("Save message {}: ", message.nameOrder());
		repository.save(orderDlqMapper.toEntity(message));
	}
}
