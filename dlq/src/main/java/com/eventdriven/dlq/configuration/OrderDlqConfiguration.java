package com.eventdriven.dlq.configuration;

import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.service.IOrderSupportQueryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class OrderDlqConfiguration {

	@Bean
	public Consumer<OrderDto> dqlOrder(
			@Qualifier("orderDlqSaveService")
			IOrderSupportQueryService service)
	{
		return service::saveMessage;
	}
}
