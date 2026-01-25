package com.eventdriven.sink.service;

import com.eventdriven.sink.dto.OrderAvailableDto;
import com.eventdriven.sink.entity.OrderAvailableEntity;
import com.eventdriven.sink.mapper.OrderMapper;
import com.eventdriven.sink.repository.OrderAvaiableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ConvertOrderToOrderAvailableService implements IConvertOrderService{

    private final OrderMapper mapper;

    private final OrderAvaiableRepository repository;

    @Transactional
    @Override
    public void convertAndSave(OrderAvailableDto order) {
        OrderAvailableEntity entity = mapper.toEntity(order);
        repository.save(entity);
    }
}
