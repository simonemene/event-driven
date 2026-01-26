package com.eventdriven.source.savemessage;

import com.eventdriven.source.SourceApplicationTests;
import com.eventdriven.source.dto.OrderDto;
import com.eventdriven.source.entity.MessageEntity;
import com.eventdriven.source.enums.StatusEnum;
import com.eventdriven.source.properties.CloudConfig;
import com.eventdriven.source.repository.MessageRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@EmbeddedKafka
@AutoConfigureTestDatabase
public class SaveMessageIntegrationTest extends SourceApplicationTests
{
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MessageRespository messageRespository;

	@Autowired
	private CloudConfig config;


	@Test
	public void saveMessage() throws JsonProcessingException {
		//given
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


	}
}
