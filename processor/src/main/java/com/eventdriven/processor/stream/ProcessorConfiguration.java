package com.eventdriven.processor.stream;

import com.eventdriven.processor.dto.OrderAvailableDto;
import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.exception.InvalidEventException;
import com.eventdriven.processor.service.IRandomAvailableService;
import com.eventdriven.processor.service.RandomAvailableService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.function.Function;

@Configuration
public class ProcessorConfiguration {

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
            return new OrderAvailableDto(checkAvailable,randomAvailableService.isAvaiable());
        };
    }

    @Bean
    public Validator validator()
    {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
