package com.eventdriven.processor.service;

import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.exception.IdEventDuplicateException;
import com.eventdriven.processor.mapper.OrderMapper;
import com.eventdriven.processor.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SaveOrderService implements  ISaveOrderService{

	private final IdempotencyRepository repository;

	private final OrderMapper mapper;

	@Transactional
	@Override
	public void saveOrder(OrderDto order) {
		try {
			repository.save(mapper.toEntity(order));
		}catch(DataIntegrityViolationException e)
		{
			throw new IdEventDuplicateException("duplicate event",order.eventiId());
		}
	}
}
