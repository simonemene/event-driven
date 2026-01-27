package com.eventdriven.dlq.dlq;

import com.eventdriven.dlq.DlqApplicationTests;
import com.eventdriven.dlq.configuration.OrderDlqConfiguration;
import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.mapper.OrderDlqMapper;
import com.eventdriven.dlq.repository.OrderDlqRepository;
import com.eventdriven.dlq.scheduled.AlarmOrderDlqMessageScheduled;
import com.eventdriven.dlq.service.OrderDlqSaveService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
				"spring.cloud.stream.bindings.dqlOrder-in-0.destination=order-topic.dlq",
				"spring.cloud.config.enabled=false",
				"spring.cloud.function.definition=dqlOrder",
				"spring.config.import=",
				"spring.scheduled.time=100"
		})
@AutoConfigureTestDatabase
@Import({TestChannelBinderConfiguration.class, OrderDlqConfiguration.class})
public class OrderDlqIntegrationTest {

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
		//when
		inputDestination.send(MessageBuilder.withPayload(order).build(),
				"order-topic.dlq");
		//then
		String name = jdbcClient.sql("SELECT NAME FROM ORDER_DLQ")
				.query()
				.singleValue()
				.toString();
		Assertions.assertEquals(name,order.nameOrder());
		Assertions.assertEquals("12a",order.id());

		Awaitility.await().atMost(Duration.ofSeconds(2))
				.pollInterval(Duration.ofMillis(200))
				.untilAsserted(()->
				{
                   org.assertj.core.api.Assertions.assertThat(capturedOutput.getOut())
						   .contains("[ORDER] ALARM: " + order.id());
					boolean value = jdbcClient.sql("SELECT NOTIFICATION FROM ORDER_DLQ WHERE ID_EVENT = ?")
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
					OrderDlqSaveService.class,
					OrderDlqMapper.class,
					AlarmOrderDlqMessageScheduled.class
			})
	public static class main
	{

	}
}
