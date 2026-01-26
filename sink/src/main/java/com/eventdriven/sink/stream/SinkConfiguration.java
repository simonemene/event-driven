package com.eventdriven.sink.stream;

import com.eventdriven.sink.dto.OrderAvailableDto;
import com.eventdriven.sink.mapper.OrderMapper;
import com.eventdriven.sink.repository.OrderAvaiableRepository;
import com.eventdriven.sink.service.ConvertOrderToOrderAvailableService;
import com.eventdriven.sink.service.IConvertOrderService;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SinkConfiguration {

    @Bean
    public Consumer<@Valid OrderAvailableDto> consumer(IConvertOrderService service)
    {
        return service::convertAndSave;
    }

    @Bean
    public OrderMapper mapper()
    {
        return new OrderMapper();
    }

    @Bean
    public IConvertOrderService convertService(
            OrderMapper orderMapper,
            OrderAvaiableRepository orderAvaiableRepository,
            Validator validator)
    {
        return new ConvertOrderToOrderAvailableService(orderMapper,orderAvaiableRepository,validator);
    }


    @Bean
    public Validator validator()
    {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
