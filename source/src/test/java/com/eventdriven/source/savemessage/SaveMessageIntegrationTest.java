package com.eventdriven.source.savemessage;

import com.eventdriven.source.SourceApplicationTests;
import com.eventdriven.source.dto.OrderDto;
import com.eventdriven.source.entity.MessageEntity;
import com.eventdriven.source.enums.StatusEnum;
import com.eventdriven.source.properties.CloudConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
		objectNode.put("name",orderDto.name());
		objectNode.put("cost",orderDto.cost());
		String orderJson = objectMapper.writeValueAsString(objectNode);
		//when
		restTemplate.postForEntity("/api/source",orderDto,Void.class);
		//then
		List<MessageEntity> message = messageRespository.findTop10ByStatusInOrderByCreateTimestampAsc(
				List.of(StatusEnum.NEW));
		Assertions.assertThat(message.size()).isEqualTo(1);
		MessageEntity message1 = message.stream().filter(mess->mess.getTopic().equals(config.nameBridge())).findFirst()
				.get();
		JsonNode json = objectMapper.readTree(message1.getPayload());
		JsonNode jsonCheck = objectMapper.readTree(orderJson);
		Assertions.assertThat(json.get("cost")).isEqualTo(jsonCheck.get("cost"));
		Assertions.assertThat(json.get("name")).isEqualTo(jsonCheck.get("name"));
		Assertions.assertThat(message1.getStatus()).isEqualTo(StatusEnum.NEW);
		Assertions.assertThat(message1.getCreateTimestamp()).isBeforeOrEqualTo(Instant.now());
		Assertions.assertThat(message1.getAttempts()).isEqualTo(0);

		Awaitility.await().atMost(Duration.ofSeconds(5))
				.pollInterval(Duration.ofMillis(100))
				.untilAsserted(()->
				{
					List<MessageEntity> messageSend = messageRespository.findTop10ByStatusInOrderByCreateTimestampAsc(
							List.of(StatusEnum.SEND));
					Assertions.assertThat(messageSend.size()).isEqualTo(1);
					MessageEntity messageSend1 = messageSend.stream().filter(mess->mess.getTopic().equals(config.nameBridge())).findFirst()
							.get();
					JsonNode saveJson = objectMapper.readTree(messageSend1.getPayload());
					Assertions.assertThat(saveJson.hasNonNull("id")).isTrue();
					Assertions.assertThatCode(()-> UUID.fromString(saveJson.get("id").asText())).doesNotThrowAnyException();
					Assertions.assertThat(messageSend1.getStatus()).isEqualTo(StatusEnum.SEND);
					Assertions.assertThat(messageSend1.getCreateTimestamp()).isBeforeOrEqualTo(Instant.now());
					Assertions.assertThat(messageSend1.getAttempts()).isEqualTo(0);

				});
	}
}
