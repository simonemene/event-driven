package com.eventdriven.dlq.configuration;

import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.service.IOrderDlqSaveService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class OrderDlqConfiguration {

	@Bean
	public Consumer<OrderDto> dqlOrder(IOrderDlqSaveService service)
	{
		return service::saveMessage;
	}
}
