package com.eventdriven.processor.stream;

import com.eventdriven.processor.dto.OrderAvailableDto;
import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.service.IRandomAvailableService;
import com.eventdriven.processor.service.RandomAvailableService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ProcessorConfiguration {

    @Bean
    public IRandomAvailableService service()
    {
        return new RandomAvailableService();
    }

    @Bean
    public Function<OrderDto, OrderAvailableDto> orderCheckAvailable(IRandomAvailableService randomAvailableService)
    {
        return checkAvaiable->
        {
            return new OrderAvailableDto(checkAvaiable,randomAvailableService.isAvaiable());
        };
    }
}
