package com.eventdriven.dlq.configuration;

import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.service.IOrderSupportQueryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class OrderStockConfiguration {

	@Bean
	Consumer<OrderAvailableDto> dlqStockOrder(@Qualifier("orderStockDlqSaveService")
	IOrderSupportQueryService service)
	{
		return service::saveMessage;
	}
}
