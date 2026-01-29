package com.eventdriven.dlq.dlq;

import com.eventdriven.dlq.configuration.EnableSchedulingConfiguration;
import com.eventdriven.dlq.configuration.OrderStockConfiguration;
import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.mapper.OrderStockDlqMapper;
import com.eventdriven.dlq.scheduled.AlarmOrderStockDlqMessageScheduled;
import com.eventdriven.dlq.service.OrderStockDlqSaveService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;
import java.time.Duration;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(properties =
		{
				"spring.cloud.stream.bindings.dlqStockOrder-in-0.destination=stock-topic.dlq",
				"spring.cloud.config.enabled=false",
				"spring.cloud.function.definition=dlqStockOrder",
				"spring.config.import=",
				"spring.scheduled.time=100"
		})
@AutoConfigureTestDatabase
@Import({ TestChannelBinderConfiguration.class, OrderStockConfiguration.class, EnableSchedulingConfiguration.class })
public class OrderStockDlqIntegrationTest {

	@Autowired
	private JdbcClient jdbcClient;

	@Autowired
	private InputDestination inputDestination;

	@Test
	public void readAndSave(CapturedOutput capturedOutput)
	{
		//given
		OrderDto order =
				new OrderDto("12a","phone",new BigDecimal("12.12"));
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
		Assertions.assertEquals("12a",order.id());

		Awaitility.await().atMost(Duration.ofSeconds(2))
				.pollInterval(Duration.ofMillis(200))
				.untilAsserted(()->
				{
					org.assertj.core.api.Assertions.assertThat(capturedOutput.getOut())
							.contains("[ORDER IN STOCK] ALARM: " + order.id());
					boolean value = jdbcClient.sql("SELECT NOTIFICATION FROM ORDER_STOCK_DLQ WHERE ID_EVENT = ?")
							.param(1,"12a")
							.query(Boolean.class)
							.optional()
							.orElse(false);
					Assertions.assertTrue(value);
				});

	}

	@EnableScheduling
	@EnableJpaRepositories(basePackages = "com.eventdriven.dlq.repository")
	@EntityScan(basePackages ="com.eventdriven.dlq.entity" )
	@SpringBootApplication(scanBasePackageClasses =
			{
					OrderStockDlqSaveService.class,
	                OrderStockDlqMapper.class,
					AlarmOrderStockDlqMessageScheduled.class
			})
	public static class main
	{

	}
}

