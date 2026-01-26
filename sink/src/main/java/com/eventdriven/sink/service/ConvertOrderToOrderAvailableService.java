package com.eventdriven.sink.service;

import com.eventdriven.sink.dto.OrderAvailableDto;
import com.eventdriven.sink.entity.OrderAvailableEntity;
import com.eventdriven.sink.exception.InvalidEventException;
import com.eventdriven.sink.mapper.OrderMapper;
import com.eventdriven.sink.repository.OrderAvaiableRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class ConvertOrderToOrderAvailableService implements IConvertOrderService{

    private final OrderMapper mapper;

    private final OrderAvaiableRepository repository;

    private final Validator validator;

    @Transactional
    @Override
    public void convertAndSave(OrderAvailableDto order) {
        Set<ConstraintViolation<OrderAvailableDto>> violations =
                validator.validate(order);
        if(!violations.isEmpty())
        {
            throw new InvalidEventException(violations);
        }
        OrderAvailableEntity entity = mapper.toEntity(order);
        log.info("Saving order {}",order.order().name());
        repository.save(entity);
    }
}
