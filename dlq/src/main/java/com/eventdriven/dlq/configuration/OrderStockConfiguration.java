package com.eventdriven.dlq.configuration;

import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.service.IOrderStockDlqSaveService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class OrderStockConfiguration {

	@Bean
	Consumer<OrderAvailableDto> dlqStockOrder(IOrderStockDlqSaveService service)
	{
		return service::saveMessage;
	}
}
