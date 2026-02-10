package com.eventdriven.processor.stream;

import com.eventdriven.processor.dto.OrderAvailableDto;
import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.exception.IdEventDuplicateException;
import com.eventdriven.processor.exception.InvalidEventException;
import com.eventdriven.processor.mapper.OrderMapper;
import com.eventdriven.processor.repository.IdempotencyRepository;
import com.eventdriven.processor.service.IRandomAvailableService;
import com.eventdriven.processor.service.ISaveOrderService;
import com.eventdriven.processor.service.RandomAvailableService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class ProcessorConfiguration {

    private final ISaveOrderService service;

    @Bean
    public IRandomAvailableService service()
    {
        return new RandomAvailableService();
    }

    @Bean
    public Function<OrderDto, OrderAvailableDto> orderCheckAvailable(IRandomAvailableService randomAvailableService,
                                                                     Validator validator)
    {
        return checkAvailable->
        {
            Set<ConstraintViolation<OrderDto>> violations =
                    validator.validate(checkAvailable);
            if(!violations.isEmpty())
            {
                throw new InvalidEventException(violations);
            }
            service.saveOrder(checkAvailable);
            return new OrderAvailableDto(checkAvailable,randomAvailableService.isAvaiable());
        };
    }

    @Bean("processorValidation")
    public Validator validator()
    {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
