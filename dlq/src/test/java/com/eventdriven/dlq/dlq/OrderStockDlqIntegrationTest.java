package com.eventdriven.dlq.dlq;

import com.eventdriven.dlq.configuration.OrderStockConfiguration;
import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.mapper.OrderStockDlqMapper;
import com.eventdriven.dlq.service.OrderStockDlqSaveService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;

@SpringBootTest(properties =
		{
				"spring.cloud.stream.bindings.dlqStockOrder-in-0.destination=stock-topic.dlq",
				"spring.cloud.config.enabled=false",
				"spring.cloud.function.definition=dlqStockOrder",
				"spring.config.import="
		})
@AutoConfigureTestDatabase
@Import({ TestChannelBinderConfiguration.class, OrderStockConfiguration.class,
		OrderStockDlqSaveService.class, OrderStockDlqMapper.class })
public class OrderStockDlqIntegrationTest {


	@Autowired
	private JdbcClient jdbcClient;

	@Autowired
	private InputDestination inputDestination;

	@Test
	public void readAndSave()
	{
		//given
		OrderDto order =
				new OrderDto("phone",new BigDecimal("12.12"));
		OrderAvailableDto availableDto =
				new OrderAvailableDto(order,"IN_STOCK");
		//when
		inputDestination.send(MessageBuilder.withPayload(availableDto).build(),
				"stock-topic.dlq");
		//then
		String name = jdbcClient.sql("SELECT NAME FROM ORDER_STOCK_DLQ")
				.query()
				.singleValue()
				.toString();
		Assertions.assertEquals("phone",order.nameOrder());

	}

	@EnableJpaRepositories(basePackages = "com.eventdriven.dlq.repository")
	@EntityScan(basePackages ="com.eventdriven.dlq.entity" )
	@SpringBootApplication
	public static class main
	{

	}
}

