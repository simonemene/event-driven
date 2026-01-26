package com.eventdriven.source.circuitbreaker;

import com.eventdriven.source.SourceApplicationTests;
import com.eventdriven.source.dto.OrderDto;
import com.eventdriven.source.entity.MessageEntity;
import com.eventdriven.source.enums.StatusEnum;
import com.eventdriven.source.properties.CloudConfig;
import com.eventdriven.source.savemessage.MessageRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Sql(scripts = "classpath:/delete.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@EmbeddedKafka
@AutoConfigureTestDatabase
public class CircuitBreakerIntegrationTest extends SourceApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MessageRespository messageRespository;

	@Autowired
	private CloudConfig config;

	@MockitoBean
	private KafkaTemplate<String,String> kafkaTemplate;

	@Test
	public void saveMessage()
			throws JsonProcessingException{
		//given
		Mockito.when(kafkaTemplate.send(Mockito.any(),Mockito.any(),Mockito.any())).thenThrow(new KafkaException("error"));

		OrderDto orderDto = new OrderDto("phone",new BigDecimal("10.21"));
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode objectNode = objectMapper.createObjectNode();
		ObjectNode root = objectNode.putObject("order");
		root.put("name",orderDto.nameOrder());
		root.put("cost",orderDto.costOrder());
		String orderJson = objectMapper.writeValueAsString(objectNode);
		//when
		restTemplate.postForEntity("/api/source",orderDto,Void.class);
		//then
		List<MessageEntity> message = messageRespository.findTop10ByStatusInOrderByCreateTimestampAsc(
				List.of(StatusEnum.NEW));
		Assertions.assertThat(message.size()).isEqualTo(1);
		MessageEntity message1 = message.stream().filter(mess->mess.getTopic().equals(config.nameBridge())).findFirst()
				.get();
		Assertions.assertThat(message1.getPayload()).isEqualTo(orderJson);
		Assertions.assertThat(message1.getStatus()).isEqualTo(StatusEnum.NEW);
		Assertions.assertThat(message1.getCreateTimestamp()).isBeforeOrEqualTo(Instant.now());
		Assertions.assertThat(message1.getAttempts()).isEqualTo(0);

		Awaitility.await().atMost(Duration.ofSeconds(500))
				.pollInterval(Duration.ofMillis(100))
				.untilAsserted(()->
				{
					List<MessageEntity> messageSend = messageRespository.findTop10ByStatusInOrderByCreateTimestampAsc(
							List.of(StatusEnum.FAILED));
					Assertions.assertThat(messageSend.size()).isEqualTo(1);
					MessageEntity messageSend1 = messageSend.stream().filter(mess->mess.getTopic().equals(config.nameBridge())).findFirst()
							.get();
					Assertions.assertThat(messageSend1.getPayload()).isEqualTo(orderJson);
					Assertions.assertThat(messageSend1.getStatus()).isEqualTo(StatusEnum.FAILED);
					Assertions.assertThat(messageSend1.getCreateTimestamp()).isBeforeOrEqualTo(Instant.now());
					Assertions.assertThat(messageSend1.getAttempts()).isGreaterThan(0);

				});
	}

	@Test
	public void parkingMessage() throws JsonProcessingException {
		//given
		Mockito.when(kafkaTemplate.send(Mockito.any(),Mockito.any(),Mockito.any())).thenThrow(new KafkaException("error"));


		OrderDto orderDto = new OrderDto("phone",new BigDecimal("10.21"));
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode objectNode = objectMapper.createObjectNode();
		ObjectNode root = objectNode.putObject("order");
		root.put("name",orderDto.nameOrder());
		root.put("cost",orderDto.costOrder());
		String orderJson = objectMapper.writeValueAsString(objectNode);
		//1
		//when
		restTemplate.postForEntity("/api/source",orderDto,Void.class);
		//then
		List<MessageEntity> message = messageRespository.findTop10ByStatusInOrderByCreateTimestampAsc(
				List.of(StatusEnum.NEW));
		Assertions.assertThat(message.size()).isEqualTo(1);
		MessageEntity message1 = message.stream().filter(mess->mess.getTopic().equals(config.nameBridge())).findFirst()
				.get();
		Assertions.assertThat(message1.getPayload()).isEqualTo(orderJson);
		Assertions.assertThat(message1.getStatus()).isEqualTo(StatusEnum.NEW);
		Assertions.assertThat(message1.getCreateTimestamp()).isBeforeOrEqualTo(Instant.now());
		Assertions.assertThat(message1.getAttempts()).isEqualTo(0);

		Awaitility.await().atMost(Duration.ofSeconds(2))
				.pollInterval(Duration.ofMillis(100))
				.untilAsserted(()->
				{
					List<MessageEntity> messageSend = messageRespository.findTop10ByStatusInOrderByCreateTimestampAsc(
							List.of(StatusEnum.PARKING));
					Assertions.assertThat(messageSend.size()).isEqualTo(1);
					MessageEntity messageSend1 = messageSend.stream().filter(mess->mess.getTopic().equals(config.nameBridge())).findFirst()
							.get();
					Assertions.assertThat(messageSend1.getPayload()).isEqualTo(orderJson);
					Assertions.assertThat(messageSend1.getStatus()).isEqualTo(StatusEnum.PARKING);
					Assertions.assertThat(messageSend1.getCreateTimestamp()).isBeforeOrEqualTo(Instant.now());
					Assertions.assertThat(messageSend1.getAttempts()).isEqualTo(4);

				});
	}
}
