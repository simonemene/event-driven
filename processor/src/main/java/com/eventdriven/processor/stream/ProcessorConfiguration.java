package com.eventdriven.processor.stream;

import com.eventdriven.processor.dto.OrderAvailableDto;
import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.service.IRandomAvailableService;
import com.eventdriven.processor.service.ISaveOrderService;
import com.eventdriven.processor.service.RandomAvailableService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
            return new OrderAvailableDto(checkAvailable,randomAvailableService.isAvaiable());
        };
    }
}
