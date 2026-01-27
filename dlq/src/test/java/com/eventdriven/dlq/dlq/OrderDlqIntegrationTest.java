package com.eventdriven.dlq.dlq;

import com.eventdriven.dlq.DlqApplicationTests;
import com.eventdriven.dlq.configuration.OrderDlqConfiguration;
import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.mapper.OrderDlqMapper;
import com.eventdriven.dlq.repository.OrderDlqRepository;
import com.eventdriven.dlq.service.OrderDlqSaveService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
				"spring.cloud.stream.bindings.dqlOrder-in-0.destination=order-topic.dlq",
				"spring.cloud.config.enabled=false",
				"spring.cloud.function.definition=dqlOrder",
				"spring.config.import="
		})
@AutoConfigureTestDatabase
@Import({TestChannelBinderConfiguration.class, OrderDlqConfiguration.class,
		OrderDlqSaveService.class, OrderDlqMapper.class })
public class OrderDlqIntegrationTest {

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
		//when
		inputDestination.send(MessageBuilder.withPayload(order).build(),
				"order-topic.dlq");
		//then
		String name = jdbcClient.sql("SELECT NAME FROM ORDER_DLQ")
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
