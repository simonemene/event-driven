package com.eventdriven.sink.stream;

import com.eventdriven.sink.dto.OrderAvaiableDto;
import com.eventdriven.sink.mapper.OrderMapper;
import com.eventdriven.sink.repository.OrderAvaiableRepository;
import com.eventdriven.sink.service.ConvertOrderToOrderAvaiableService;
import com.eventdriven.sink.service.IConvertOrderService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SinkConfiguration {

    @Bean
    public Consumer<@Valid OrderAvaiableDto> consumer(IConvertOrderService service)
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
            OrderAvaiableRepository orderAvaiableRepository)
    {
        return new ConvertOrderToOrderAvaiableService(orderMapper,orderAvaiableRepository);
    }
}
