package com.eventdriven.processor.stream;

import com.eventdriven.processor.dto.OrderAvaiableDto;
import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.service.IRandomAvaiableService;
import com.eventdriven.processor.service.RandomAvaiableService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ProcessorConfiguration {

    @Bean
    public IRandomAvaiableService service()
    {
        return new RandomAvaiableService();
    }

    @Bean
    public Function<OrderDto, OrderAvaiableDto> orderCheckAvaiable(IRandomAvaiableService randomAvaiableService)
    {
        return checkAvaiable->
        {
            return new OrderAvaiableDto(checkAvaiable,randomAvaiableService.isAvaiable());
        };
    }
}
