package com.eventdriven.sink.stream;

import com.eventdriven.sink.dto.OrderAvaiableDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SinkConfiguration {

    @Bean
    public Consumer<OrderAvaiableDto> consumer()
    {
        return order->
        {
             System.out.println(order);
        };
    }
}
