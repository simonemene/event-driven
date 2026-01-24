package com.eventdriven.sink.service;

import com.eventdriven.sink.dto.OrderAvaiableDto;
import com.eventdriven.sink.entity.OrderAvaiableEntity;
import com.eventdriven.sink.mapper.OrderMapper;
import com.eventdriven.sink.repository.OrderAvaiableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ConvertOrderToOrderAvaiableService implements IConvertOrderService{

    private final OrderMapper mapper;

    private final OrderAvaiableRepository repository;

    @Transactional
    @Override
    public void convertAndSave(OrderAvaiableDto order) {
        OrderAvaiableEntity entity = mapper.toEntity(order);
        repository.save(entity);
    }
}
