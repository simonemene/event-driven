package com.eventdriven.processor.stream;

import com.eventdriven.processor.dto.OrderAvaiableDto;
import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.service.IRandomAvaiableService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class ProcessorConfiguration {

    private final IRandomAvaiableService service;

    @Bean
    public Function<OrderDto, OrderAvaiableDto> orderCheckAvaiable()
    {
        return checkAvaiable->
        {
            return new OrderAvaiableDto(checkAvaiable,service.isAvaiable());
        };
    }
}
