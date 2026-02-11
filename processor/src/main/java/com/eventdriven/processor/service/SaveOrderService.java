package com.eventdriven.processor.service;

import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.mapper.OrderMapper;
import com.eventdriven.processor.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaveOrderService implements  ISaveOrderService{

	private final IdempotencyRepository repository;

	private final OrderMapper mapper;

	@Override
	public void saveOrder(OrderDto order) {
		try {
			repository.save(mapper.toEntity(order));
		}catch(DataIntegrityViolationException e)
		{
			log.warn("Duplicate key: {}",order.eventiId());
		}
	}
}
