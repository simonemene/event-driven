package com.eventdriven.dlq.configuration;

import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.dto.OrderDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class DlqConfiguration {

	@Bean
	public Consumer<OrderDto> dqlOrder(OrderDto order)
	{
		return null;
	}

	@Bean Consumer<OrderAvailableDto> dlqStockOrder(OrderAvailableDto orderAvailable)
	{
		return null;
	}
}
